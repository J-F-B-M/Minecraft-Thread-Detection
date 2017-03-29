package logging;

import provider.IObservable;
import provider.IObserver;

import java.util.logging.Logger;

/**
 * Created by Joachim Brehmer on 29.03.2017.
 */
public class LogObserver implements IObserver {
    private final Logger log;

    public LogObserver(String loggerName) {
        log = Logger.getLogger(loggerName);
    }

    @Override
    public void update(IObservable observable, Object argument) {
        log.info("Observable produced output: " + argument.toString());
    }
}
