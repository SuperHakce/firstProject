package com.smart.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/18.
 */
@Entity
@Table(name="T_EVENT")
public class SchoolEvent implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVENT_ID")
    private String eventId;

    @Column(name = "SCHOOL_CODE")
    private int schoolCode;

    @Column(name = "SCHOOL_NAME")
    private String schoolName;

    @Column(name = "EVENT_TEXT")
    private String eventText;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "START_DATE")
    private Date startDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "IS_CHECK")
    private int isCheck;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(int schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getIsCheck(){
        return isCheck;
    }

    public void setIsCheck(int isCheck){
        this.isCheck = isCheck;
    }

}
