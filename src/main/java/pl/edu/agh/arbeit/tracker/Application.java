package pl.edu.agh.arbeit.tracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

/**
 * Created by Albert on 06.04.2018.
 */

// TODO the whole logic of whether the app is running or not should be stored here
public class Application {
    private final String name;
    private final String programName;

    public Application(String name, String programName) {
        this.name = name;
        this.programName = programName;
    }

    public boolean isRunning() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                if (line.contains(programName)) {
                    return true;
                }
            }
            input.close();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ApplicationEvent stateEvent(){
        if(isActive()) return new ApplicationEvent(EventType.ACTIVE, this);
        else return new ApplicationEvent(EventType.PASSIVE, this);
    }

    //TODO
    public boolean isActive() {
        return false;
    }

    public String getName() {
        return name;
    }

    public String getProgramName() {
        return programName;
    }



    public interface Psapi extends StdCallLibrary {
        Psapi INSTANCE = (Psapi) Native.loadLibrary("Psapi", Psapi.class);

        WinDef.DWORD GetModuleBaseNameW(Pointer hProcess, Pointer hModule, byte[] lpBaseName, int nSize);
    }

    //TODO: This code is copypasted from SO.
    public void trackFocusedWindow(){
    if (Platform.isWindows()) {
        final int PROCESS_VM_READ = 0x0010;
        final int PROCESS_QUERY_INFORMATION = 0x0400;
        final User32 user32 = User32.INSTANCE;
        final Kernel32 kernel32 = Kernel32.INSTANCE;
        final Psapi psapi = Psapi.INSTANCE;
        WinDef.HWND windowHandle = user32.GetForegroundWindow();
        IntByReference pid = new IntByReference();
        user32.GetWindowThreadProcessId(windowHandle, pid);
        WinNT.HANDLE processHandle = kernel32.OpenProcess(PROCESS_VM_READ | PROCESS_QUERY_INFORMATION, true, pid.getValue());

        byte[] filename = new byte[512];
        Psapi.INSTANCE.GetModuleBaseNameW(processHandle.getPointer(), Pointer.NULL, filename, filename.length);
        String name = new String(filename);
        System.out.println(name);
        if (name.endsWith("wwahost.exe")) { // Metro Application
            // There is no stable API to get the current Metro app
            // But you can guestimate the name form the current directory of the process
            // To query this, see:
            // http://stackoverflow.com/questions/16110936/read-other-process-current-directory-in-c-sharp
        }
    }
    }

    }




