package com.smart.dao;

        import com.smart.domain.SchoolEvent;
        import com.smart.domain.User;
        import com.smart.domain.UserLearningHistory;
        import com.smart.exception.exception;
        import com.smart.exception.schoolNameNotNull;
        import com.sun.rowset.internal.Row;
        import org.apache.log4j.Logger;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.jdbc.core.JdbcTemplate;
        import org.springframework.jdbc.core.RowCallbackHandler;
        import org.springframework.stereotype.Repository;
        import org.springframework.transaction.annotation.Transactional;
        import org.springframework.transaction.interceptor.TransactionAspectSupport;
        import org.springframework.transaction.support.TransactionCallback;
        import org.springframework.transaction.support.TransactionTemplate;
        import org.springframework.util.Assert;

        import javax.xml.bind.ValidationException;
        import java.sql.ResultSet;
        import java.sql.SQLException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;
        import java.util.UUID;

/**
 * Created by Administrator on 2017/7/18.
 */
@Repository
public class SchoolEventDao {
        private JdbcTemplate jdbcTemplate;
        private Logger logger = Logger.getLogger(SchoolEventDao.class);
        private  int test;
        private schoolNameNotNull mySchoolNameNotNull = new schoolNameNotNull();
        public boolean ticketIsUsed(String ticket){
                String sqlFindTicket = "SELECT * FROM t_ticket WHERE ticket_id = ? ";
                final List<Integer> events = new ArrayList();
                jdbcTemplate.query(sqlFindTicket, new Object[]{ticket}, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet resultSet) throws SQLException {
                                events.add(100);
                        }
                });
                if(events.size() > 0){
                        return true;
                }else{
                        return false;
                }
        }
        /**
         * 幂等性模拟-页面
         * @param method
         * @param eventId
         * @param change
         * @param ticket
         */
        public int testIdempotentForEvent(String method,String eventId,int change, final String ticket){
                boolean ticketIsUsed = ticketIsUsed(ticket);
                if(ticketIsUsed == true){
                        int schoolCode = jdbcTemplate.queryForObject("SELECT school_code FROM t_event WHERE event_id = ? ",int.class,eventId);
                        return schoolCode;
                }else{
                        //原子操作
                        jdbcTemplate.update("INSERT INTO t_ticket(ticket_id)VALUES(?) ",ticket);
                        int schoolCode = jdbcTemplate.queryForObject("SELECT school_code FROM t_event WHERE event_id = ? ",int.class,eventId);
                        jdbcTemplate.update("UPDATE t_event SET school_code = ? WHERE event_id = ? ",schoolCode - change,eventId);
                        return schoolCode - change;
                }
        }
        /**
         * 增加 schoolEvent-页面
         * @param schoolEvent
         */
        public void addSchoolEventToMysql(SchoolEvent schoolEvent){
                String sqlAddEvent = " insert into t_event(event_id,school_code,school_name," +
                        "event_text,start_date,end_date,is_check)values(?,?,?,?,?,?,?) ";
                Object args[] = {schoolEvent.getEventId(),schoolEvent.getSchoolCode(),schoolEvent.getSchoolName(),
                schoolEvent.getEventText(),schoolEvent.getStartDate(), schoolEvent.getEndDate(),schoolEvent.getIsCheck()};
                jdbcTemplate.update(sqlAddEvent, args);
        }

        /**
         * 遇到自定义 exception 异常类回滚-TestNG
         * 批量增加 SchoolEvent
         * @param schoolEvents
         */
        @Transactional(rollbackFor = {exception.class})
        public void addSchoolEventByListToMysql(final List<SchoolEvent> schoolEvents){
                String sqlAddEventByList = "INSERT INTO t_event(event_id,school_code, " +
                        "school_name,event_text,start_date,end_date,is_check) " +
                        "VALUES(?,?,?,?,?,?,?)";
                List<Object[]> batch = new ArrayList<Object[]>();
                for(SchoolEvent schoolEvent:schoolEvents){
                        try{
                                logger.debug("" + mySchoolNameNotNull.checkSchoolName(schoolEvent.getSchoolName()) + "");
                                Object[] one = {schoolEvent.getEventId(),schoolEvent.getSchoolCode(),
                                        schoolEvent.getSchoolName(),schoolEvent.getEventText(),schoolEvent.getStartDate(),
                                        schoolEvent.getEndDate(),schoolEvent.getIsCheck()};
                                jdbcTemplate.update(sqlAddEventByList,one);
                        }catch (exception e){
                                logger.debug("Find A User-defined exception Like schoolName = " + schoolEvent.getSchoolName());
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                e.printStackTrace();
                        }
                }
        }

        /**
         * 分页查询测试-TestNG
         * @param pageNo
         * @return
         */
        public Page findSchoolEventByPage(int pageNo,int pageSize){
                String sqlCount = "SELECT count(*) from t_event ";
                String sqlFindPage = "SELECT * FROM t_event order by event_id limit ?,? ";
                Page page = new Page();
                int numberOfCount = jdbcTemplate.queryForObject(sqlCount,Integer.class);
                int numberOfOnePage = page.getPageSize();
                int numberOfPage = numberOfCount / numberOfOnePage;
                try{
                        numberOfPage = numberOfCount / numberOfOnePage;
                }catch(ArithmeticException e){
                        logger.error("Division by zero");
                }finally{
                        logger.info("Maybe have ArithmeticException");
                }
                if(pageNo <= 0 || pageNo > (numberOfPage + 1)){
                        pageNo = 1;
                        logger.debug("PageNo bigger than Mysql");
                }
                int start = (pageNo - 1) * numberOfOnePage;
                final int end = numberOfOnePage;
                final List<SchoolEvent> events = new ArrayList();
                jdbcTemplate.query(sqlFindPage, new Object[]{start,end}, new RowCallbackHandler() {

                        public void processRow(ResultSet resultSet) throws SQLException {
                                SchoolEvent schoolEvent = new SchoolEvent();
                                schoolEvent.setEventId(resultSet.getString("event_id"));
                                schoolEvent.setSchoolCode(resultSet.getInt("school_code"));
                                schoolEvent.setSchoolName(resultSet.getString("school_name"));
                                schoolEvent.setEventText(resultSet.getString("event_text"));
                                schoolEvent.setStartDate(resultSet.getDate("start_date"));
                                schoolEvent.setEndDate(resultSet.getDate("end_date"));
                                schoolEvent.setIsCheck(resultSet.getInt("is_check"));
                                events.add(schoolEvent);
                        }
                });
                page.setData(events);
                page.setPageSize(numberOfOnePage);
                page.setStart(start);
                page.setTotalCount(numberOfCount);
                logger.debug("PageNumber = " + pageNo + " !");
                logger.debug("PageSize = " + page.getData().size() + "!");
                for(SchoolEvent s :events){
                        logger.debug("eventId = " + s.getEventId() + " schoolCode = " + s.getSchoolCode() + " schoolName = " + s.getSchoolName()
                                + " eventText = " + s.getEventText() + "isCheck = " + s.getIsCheck());
                }
                return page;
        }

        /**
         * JSP 分页，正式运行启动方式-页面
         * @param pageNo
         * @param searchName
         * @return
         */
        public Page findSchoolEventByPage(int pageNo,String searchName,int pageSize){
                String sqlCount = "SELECT count(*) from t_event WHERE event_id LIKE ? ";
                String sqlFindPage = "SELECT * FROM t_event WHERE event_id LIKE ? order by event_id limit ?,? ";
                String name = "%" + searchName + "%";
                Page page = new Page();
                int numberOfCount = jdbcTemplate.queryForObject(sqlCount,Integer.class,name);
                int numberOfPage = numberOfCount / pageSize;
                try{
                        numberOfPage = numberOfCount / pageSize;
                }catch(ArithmeticException e){
                        logger.error("Division by zero");
                }finally{
                        logger.info("Maybe have ArithmeticException");
                }
                if(pageNo <= 0 || pageNo > (numberOfPage + 1)){
                        pageNo = 1;
                        logger.debug("PageNo bigger than Mysql");
                }
                int start = (pageNo - 1) * pageSize;
                final int end = pageSize;
                final List<SchoolEvent> events = new ArrayList();
                jdbcTemplate.query(sqlFindPage, new Object[]{name,start,end}, new RowCallbackHandler() {

                        public void processRow(ResultSet resultSet) throws SQLException {
                                SchoolEvent schoolEvent = new SchoolEvent();
                                schoolEvent.setEventId(resultSet.getString("event_id"));
                                schoolEvent.setSchoolCode(resultSet.getInt("school_code"));
                                schoolEvent.setSchoolName(resultSet.getString("school_name"));
                                schoolEvent.setEventText(resultSet.getString("event_text"));
                                schoolEvent.setStartDate(resultSet.getDate("start_date"));
                                schoolEvent.setEndDate(resultSet.getDate("end_date"));
                                schoolEvent.setIsCheck(resultSet.getInt("is_check"));
                                events.add(schoolEvent);
                        }
                });
                page.setData(events);
                page.setPageSize(pageSize);
                page.setStart(start);
                page.setTotalCount(numberOfCount);
                logger.debug("PageNumber = " + pageNo + " !");
                logger.debug("PageSize = " + page.getData().size() + "!");
                for(SchoolEvent s :events){
                        logger.debug("eventId = " + s.getEventId() + " schoolCode = " + s.getSchoolCode() + " schoolName = " + s.getSchoolName() +
                                " eventText = " + s.getEventText() + "isCheck" + s.getIsCheck());
                }
                return page;
        }
        /**
         * 批量删除测试-TestNG
         * @param event_id
         */
        public void deleteEventGroup(String event_id){
                String sqlFindEvent = "SELECT event_id FROM t_event WHERE event_id LIKE ? ";
                String eventId = "%" + event_id + "%";
                final List<String> eventIdS = new ArrayList();
                jdbcTemplate.query(sqlFindEvent, new Object[]{eventId}, new RowCallbackHandler() {
                        public void processRow(ResultSet resultSet) throws SQLException {
                                eventIdS.add(resultSet.getString("event_id"));
                        }
                });
                logger.debug("Need To Delete " + eventIdS.size() + "Lines");
                for(String deleteS: eventIdS){
                        String sqlDeleteEvent = "DELETE FROM t_event WHERE event_id = ? ";
                        jdbcTemplate.update(sqlDeleteEvent,deleteS);
                }
        }

        /**
         * 查找Event-页面
         * @param userName
         * @return
         */
        public Page findEventByUserName(final String userName){
                String sqlFindEvent = "SELECT user_name FROM t_user WHERE user_name LIKE ? ";
                final String sqlFindEventByUnion = "SELECT * FROM t_event JOIN t_history ON t_event.school_name " +
                        "= t_history.school_name WHERE t_history.user_name = ? ";
                String userNameAdd = "%" + userName + "%";
                final List<SchoolEvent> events = new ArrayList();
                test = 0;
               jdbcTemplate.query(sqlFindEvent, new Object[]{userNameAdd}, new RowCallbackHandler() {

                       public void processRow(ResultSet resultSet) throws SQLException {
                               String userNameS = resultSet.getString("user_name");
                               String userNameTest = "admin";
                               jdbcTemplate.query(sqlFindEventByUnion, new Object[]{userNameS}, new RowCallbackHandler() {
                                       @Override
                                       public void processRow(ResultSet resultSet) throws SQLException {
                                               SchoolEvent schoolEvent = new SchoolEvent();
                                               schoolEvent.setEventId(resultSet.getString("event_id"));
                                               schoolEvent.setSchoolCode(resultSet.getInt("school_code"));
                                               schoolEvent.setSchoolName(resultSet.getString("school_name"));
                                               schoolEvent.setEventText(resultSet.getString("event_text"));
                                               schoolEvent.setStartDate(resultSet.getDate("start_date"));
                                               schoolEvent.setEndDate(resultSet.getDate("end_date"));
                                               schoolEvent.setIsCheck(resultSet.getInt("is_check"));
                                               test ++;
                                               events.add(schoolEvent);
                                       }
                               });
                       }
               });
                Page page = new Page();
                page.setData(events);
                page.setPageSize(10);
                page.setStart(1);
                page.setTotalCount(test);
                return page;
        }

        /**
         * 全部反选
         */
        public void invertCheck(){
                String sqlFindAll = "SELECT event_id,is_check FROM t_event ";
                final String sqlUpdateCheck = "UPDATE t_event SET is_check = ? WHERE event_id = ? ";
                List<SchoolEvent> schoolEvents = new ArrayList<SchoolEvent>();
                jdbcTemplate.query(sqlFindAll, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet resultSet) throws SQLException {
                                SchoolEvent schoolEvent = new SchoolEvent();
                                schoolEvent.setEventId(resultSet.getString("event_id"));
                                schoolEvent.setIsCheck(resultSet.getInt("is_check"));
                                int isCheck = 0;
                                if(schoolEvent.getIsCheck() == 0){
                                        isCheck = 1;
                                }else{
                                        isCheck = 0;
                                }
                                jdbcTemplate.update(sqlUpdateCheck,isCheck,schoolEvent.getEventId());
                        }
                });
        }

        /**
         * 全选与全不选
         * @param who
         */
        public void cancelCheckAllAndCheckAll(final int who){
                String sqlFindAll = "SELECT event_id,is_check FROM t_event ";
                final String sqlUpdateCheck = "UPDATE t_event SET is_check = ? WHERE event_id = ? ";
                List<SchoolEvent> schoolEvents = new ArrayList<SchoolEvent>();
                jdbcTemplate.query(sqlFindAll, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet resultSet) throws SQLException {
                                int isCheck = 0;
                                if(who == 0){//全选
                                        isCheck = 1;
                                }
                                SchoolEvent schoolEvent = new SchoolEvent();
                                schoolEvent.setEventId(resultSet.getString("event_id"));
                                schoolEvent.setIsCheck(resultSet.getInt("is_check"));
                                jdbcTemplate.update(sqlUpdateCheck,isCheck,schoolEvent.getEventId());
                        }
                });
        }

        /**
         * 本页反选
         * @param pageNo
         * @param searchName
         * @param pageSize
         */
        public void invertCheckThisPage(int pageNo,String searchName,int pageSize){
                String sqlCount = "SELECT count(*) from t_event WHERE event_id LIKE ? ";
                String sqlFindPage = "SELECT * FROM t_event WHERE event_id LIKE ? order by event_id limit ?,? ";
                final String sqlUpdateCheck = "UPDATE t_event SET is_check = ? WHERE event_id = ? ";
                String name = "%" + searchName + "%";
                int start = (pageNo - 1) * pageSize;
                final int end = pageSize;
                //final List<SchoolEvent> schoolEvents = new ArrayList();
                jdbcTemplate.query(sqlFindPage, new Object[]{name,start,end}, new RowCallbackHandler() {

                        public void processRow(ResultSet resultSet) throws SQLException {
                                SchoolEvent schoolEvent = new SchoolEvent();
                                schoolEvent.setEventId(resultSet.getString("event_id"));
                                //schoolEvent.setSchoolCode(resultSet.getInt("school_code"));
                                //schoolEvent.setSchoolName(resultSet.getString("school_name"));
                                //schoolEvent.setEventText(resultSet.getString("event_text"));
                                //schoolEvent.setStartDate(resultSet.getDate("start_date"));
                                //schoolEvent.setEndDate(resultSet.getDate("end_date"));
                                schoolEvent.setIsCheck(resultSet.getInt("is_check"));
                                //schoolEvents.add(schoolEvent);
                                int isCheck = 0;
                                if(schoolEvent.getIsCheck() == 0){
                                        isCheck = 1;
                                }else{
                                        isCheck = 0;
                                }
                                jdbcTemplate.update(sqlUpdateCheck,isCheck,schoolEvent.getEventId());
                        }
                });
        }

        /**
         * 本页全选与全不选择
         * @param pageNo
         * @param searchName
         * @param pageSize
         * @param who
         */
        public void cancelCheckAllAndCheckAllThisPage(int pageNo,String searchName,int pageSize,final int who){
                String sqlCount = "SELECT count(*) from t_event WHERE event_id LIKE ? ";
                String sqlFindPage = "SELECT * FROM t_event WHERE event_id LIKE ? order by event_id limit ?,? ";
                final String sqlUpdateCheck = "UPDATE t_event SET is_check = ? WHERE event_id = ? ";
                String name = "%" + searchName + "%";
                int start = (pageNo - 1) * pageSize;
                final int end = pageSize;
                //final List<SchoolEvent> schoolEvents = new ArrayList();
                jdbcTemplate.query(sqlFindPage, new Object[]{name,start,end}, new RowCallbackHandler() {

                        public void processRow(ResultSet resultSet) throws SQLException {
                                SchoolEvent schoolEvent = new SchoolEvent();
                                schoolEvent.setEventId(resultSet.getString("event_id"));
                                //schoolEvent.setSchoolCode(resultSet.getInt("school_code"));
                                //schoolEvent.setSchoolName(resultSet.getString("school_name"));
                                //schoolEvent.setEventText(resultSet.getString("event_text"));
                                //schoolEvent.setStartDate(resultSet.getDate("start_date"));
                                //schoolEvent.setEndDate(resultSet.getDate("end_date"));
                                schoolEvent.setIsCheck(resultSet.getInt("is_check"));
                                //schoolEvents.add(schoolEvent);
                                int isCheck = 0;
                                if(who == 0){
                                        isCheck = 1;
                                }else{
                                        isCheck = 0;
                                }
                                jdbcTemplate.update(sqlUpdateCheck,isCheck,schoolEvent.getEventId());
                        }
                });
        }

        /**
         * 全部删除选中
         */
        public void deleteAllChecked(){
                String sqlDeleteAllChecked = "DELETE FROM t_event WHERE is_check = 1 ";
                jdbcTemplate.batchUpdate(sqlDeleteAllChecked);
        }
        @Autowired
        public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
                this.jdbcTemplate = jdbcTemplate;
        }
}
