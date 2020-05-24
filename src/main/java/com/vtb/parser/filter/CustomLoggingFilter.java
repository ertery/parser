package com.vtb.parser.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

public class CustomLoggingFilter extends AbstractMatcherFilter<ILoggingEvent> {

    Level level;
    String loggerName;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (event.getLevel().equals(level) || event.getLoggerName().equals(loggerName)) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public void start() {
        if (this.level != null) {
            super.start();
        }
    }
}
