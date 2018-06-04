package pl.edu.agh.arbeit.tracker.trackers;

import com.google.common.eventbus.EventBus;

import java.time.Duration;

public abstract class AsyncTracker implements Tracker {

    protected final EventBus bus = new EventBus();
    private final Duration pingTime;
    private final Thread trackingThread = new Thread(this::performTracking);


    public AsyncTracker(Duration pingTime) {
        this.pingTime = pingTime;
    }

    @Override
    public void start() {
        trackingThread.start();
    }

    @Override
    public void stop() {
        trackingThread.interrupt();
        stopTracking();
    }

    private void performTracking(){
        while (true){
            try {
                actOnStatus();
                Thread.sleep(pingTime.getSeconds() * 1000);
            } catch (InterruptedException e){
                break;
            }
        }
    }

    protected abstract void actOnStatus();
    protected abstract void stopTracking();

    public Duration getPingTime() {
        return pingTime;
    }

    @Override
    public EventBus getBus() {
        return bus;
    }
}
