package pl.edu.agh.arbeit.tracker.trackers;

import com.google.common.eventbus.EventBus;

public abstract class AsyncTracker implements Tracker {

    protected final EventBus bus = new EventBus();
    private final long pingTime;
    private final Thread trackingThread = new Thread(this::performTracking);


    public AsyncTracker(Long pingTime) {
        this.pingTime = pingTime;
    }

    @Override
    public void start() {
        trackingThread.start();
    }

    @Override
    public void stop() {
        trackingThread.interrupt();
    }

    private void performTracking(){
        while (true){
            try {
                actOnStatus();
                Thread.sleep(pingTime * 1000);
            } catch (InterruptedException e){
                stopTracking();
                return;
            }
        }
    }

    protected abstract void actOnStatus();
    protected abstract void stopTracking();

    public Long getPingTime() {
        return pingTime;
    }

    @Override
    public EventBus getBus() {
        return bus;
    }
}
