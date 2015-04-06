package net.ttddyy.dsproxy.support;

import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.QueryCountHolder;
import net.ttddyy.dsproxy.listener.CommonsLogLevel;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.util.Collections;
import java.util.List;

/**
 * @author Tadaya Tsuyukubo
 * @since 1.3
 */
public abstract class AbstractQueryCountLoggingRequestListener implements ServletRequestListener {
    private QueryCountLogFormatter logFormatter = new DefaultQueryCountLogFormatter();
    private boolean writeAsJson = false;

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        // No-op
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

        List<String> dsNames = QueryCountHolder.getDataSourceNamesAsList();
        Collections.sort(dsNames);

        for (String dsName : dsNames) {
            QueryCount count = QueryCountHolder.get(dsName);
            String logEntry;
            if (this.writeAsJson) {
                logEntry = logFormatter.getLogMessageAsJson(dsName, count);
            } else {
                logEntry = logFormatter.getLogMessage(dsName, count);
            }
            writeLog(sre, logEntry);
        }

        QueryCountHolder.clear();
    }

    protected abstract void writeLog(ServletRequestEvent servletRequestEvent, String logEntry);

    public void setLogFormatter(QueryCountLogFormatter logFormatter) {
        this.logFormatter = logFormatter;
    }

    public void setWriteAsJson(boolean writeAsJson) {
        this.writeAsJson = writeAsJson;
    }

}
