package vladyslava.prazhmovska.dbrgr.core.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Exceptions {

    public interface CheckedRunnable {
        void run() throws Exception;
    }

    public static void rethrow(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error("[Exceptions.rethrow] exception occurred: ", e);
            throw new RuntimeException(e);
        }
    }
}
