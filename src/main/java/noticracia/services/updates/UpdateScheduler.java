package noticracia.services.updates;

import noticracia.core.Noticracia;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateScheduler {

    private final Noticracia noticracia;
    private final String politicalCandidate;
    private final String informationSourceName;
    private final long interval;
    private ScheduledExecutorService scheduler;

    public UpdateScheduler(Noticracia noticracia, String politicalCandidate, String informationSourceName, long interval) {
        this.noticracia = noticracia;
        this.politicalCandidate = politicalCandidate;
        this.informationSourceName = informationSourceName;
        this.interval = interval;
    }

    public void start() {
        if (scheduler != null && !scheduler.isShutdown()) {
            stop();
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() ->
                        noticracia.updateObservers(politicalCandidate, informationSourceName),
                0,
                interval,
                TimeUnit.MILLISECONDS
        );
    }

    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }
}