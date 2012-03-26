package ascensionplugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Clock {

    private static final long ONE_SEC = TimeUnit.SECONDS.toNanos(1);

    /** Our timing mechanism. */
    private ScheduledExecutorService timer;

    /** Is the clock running. */
    private boolean ticking;

    public Clock() {
        timer = Executors.newSingleThreadScheduledExecutor();
        ticking = false;
    }

    /**
     * Start the clock and execute the given task periodically.
     *
     * @param task Task to execute.
     * @param frameRate Framerate, measured in frames per second.
     * @param speed Clock speed.
     */
    public void start(final Runnable task, final double frameRate,
        final double speed) {

        if (isTicking()) {
            stop();
        }

        // Calculate periodic delay.
        long delay = (long) ((ONE_SEC / frameRate) / Math.abs(speed));
        timer.scheduleWithFixedDelay(task, 0, delay, TimeUnit.NANOSECONDS);
        ticking = true;
    }

    /** Stop the clock. */
    public void stop() {
        timer.shutdownNow();
        ticking = false;
        timer = Executors.newSingleThreadScheduledExecutor();
    }

    public boolean isTicking() {
        return ticking;
    }

}
