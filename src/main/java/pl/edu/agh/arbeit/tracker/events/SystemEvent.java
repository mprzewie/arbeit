package pl.edu.agh.arbeit.tracker.events;

/**
 * @author marcin on 4/8/18
 **/
public class SystemEvent extends Event {

    private final EventType type;

    public SystemEvent(EventType type) {
        this.type = type;
    }

    @Override
    public String getTopic() {
        return "system";
    }

    @Override
    public EventType getType() {
        return type;
    }
}
