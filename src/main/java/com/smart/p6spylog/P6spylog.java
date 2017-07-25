package com.smart.p6spylog;

import com.p6spy.engine.common.P6Util;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * Created by Administrator on 2017/7/25.
 */
public class P6spylog implements MessageFormattingStrategy {
    @Override
    public String formatMessage(final int connectionId,final String now,final long elapsed,
                                final String category,final String prepared,final  String sql) {
        return now + "|" + elapsed + "|" + category + "|connection " + connectionId //+ "|" + P6Util.singleLine(prepared)
                + "|" + sql;//P6Util.singleLine(sql);

    }
}
