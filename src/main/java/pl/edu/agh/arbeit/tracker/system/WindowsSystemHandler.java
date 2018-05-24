package pl.edu.agh.arbeit.tracker.system;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import pl.edu.agh.arbeit.tracker.events.EventType;
import pl.edu.agh.arbeit.tracker.events.SystemEvent;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class WindowsSystemHandler implements SystemHandler {
    @Override
    public Set<String> getRunningApplications() {
        HashSet<String> result = new HashSet<>();
        try {
            String line;
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                // lines are of format:
                // PROGRAM_NAME PID SESSION_NAME SESSION MEM USAGE
                // so we only care about the first column
                result.add(line.split(" ")[0]);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



    private interface Psapi extends StdCallLibrary {
        Psapi INSTANCE = (Psapi) Native.loadLibrary("Psapi", Psapi.class);

        WinDef.DWORD GetModuleBaseNameW(Pointer hProcess, Pointer hModule, byte[] lpBaseName, int nSize);
    }

    @Override
    public String getFocusedApplicationName() {
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
            if (processHandle==null)
                return ""; //System cannot return focused app name as screen is locked => None app is foreground
            byte[] filename = new byte[512];
            Psapi.INSTANCE.GetModuleBaseNameW(processHandle.getPointer(), Pointer.NULL, filename, filename.length);
            String focusedName = new String(filename);
            focusedName = focusedName.replace("\0","");
            if (focusedName.endsWith("wwahost.exe")) { // Metro App
                // There is no stable API to get the current Metro app
                // But you can guestimate the name form the current directory of the process
                // To query this, see:
                // http://stackoverflow.com/questions/16110936/read-other-process-current-directory-in-c-sharp
                throw new NotImplementedException();
            }
            return focusedName;
        } else {
            //UNIX code
            throw new NotImplementedException();
        }    }


}
