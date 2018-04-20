package pl.edu.agh.arbeit.tracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import pl.edu.agh.arbeit.tracker.events.ApplicationEvent;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.system.SystemHandler;
import pl.edu.agh.arbeit.tracker.system.WindowsSystemHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Albert on 06.04.2018.
 */

// TODO the whole logic of whether the app is running or not should be stored here
public class Application {
    private final String name;
    private final String programName;
    private final SystemHandler handler;

    public Application(String name, String programName) {
        this.name = name;
        this.programName = programName;
        this.handler = new WindowsSystemHandler();
    }

    public Application(String name, String programName, SystemHandler handler) {
        this.name = name;
        this.programName = programName;
        this.handler = handler;
    }

    public boolean isRunning() {
        return handler.getRunningApplications().contains(programName);
    }
    
    public ApplicationEvent getCurrentStateEvent(){
        if(isRunning()){
            if(isActive()) return new ApplicationEvent(EventType.ACTIVE, this);
            else return new ApplicationEvent(EventType.PASSIVE, this);
        } else return new ApplicationEvent(EventType.STOP, this);

    }

    public boolean isActive() {
        return programName.equals(handler.getFocusedApplicationName());
    }

    public String getName() {
        return name;
    }

    public String getProgramName() {
        return programName;
    }
}




