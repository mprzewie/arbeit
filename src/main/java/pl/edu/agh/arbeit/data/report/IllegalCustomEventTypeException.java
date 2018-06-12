package pl.edu.agh.arbeit.data.report;

public class IllegalCustomEventTypeException extends Exception {
    public final String message = "Wrong event type has been passed. Custom event type can only be START or STOP.";
}
