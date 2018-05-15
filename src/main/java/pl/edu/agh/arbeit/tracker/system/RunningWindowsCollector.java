package pl.edu.agh.arbeit.tracker.system;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.util.HashSet;
import java.util.Set;

public class RunningWindowsCollector {
    public interface User extends StdCallLibrary {
        User INSTANCE = (User) Native.loadLibrary("user32", User.class);

        interface WNDENUMPROC extends StdCallCallback {
            boolean callback(WinDef.HWND hWnd, Pointer arg);
        }

        boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer arg);

        int GetWindowTextA(WinDef.HWND hWnd, byte[] lpString, int nMaxCount);
    }

    public interface PsApi extends StdCallLibrary {

        int GetModuleFileNameExA(WinNT.HANDLE process, WinNT.HANDLE module ,
                                 byte[] name, int i);

    }

    public Set<String> getRunningWindowsNames() {
        final User user32 = User.INSTANCE;
        Set<String> runningApps = new HashSet<>();
        user32.EnumWindows(new User.WNDENUMPROC() { //enums through all windows

            /**
             *  this function is called automatically for every window running in Windows.
             * @param hWnd
             * @param userData
             * @return
             */
            public boolean callback(WinDef.HWND hWnd, Pointer userData) {
                byte[] windowText = new byte[512];
                user32.GetWindowTextA(hWnd, windowText, 512);
                String wText = Native.toString(windowText);
                byte[] name = new byte[1024];
                PsApi psapi = (PsApi) Native.loadLibrary("psapi", PsApi.class);
                IntByReference pid = new IntByReference();
                User32.INSTANCE.GetWindowThreadProcessId(hWnd, pid);
                WinNT.HANDLE process = Kernel32.INSTANCE.OpenProcess(0x0400 | 0x0010, false, pid.getValue());
                psapi.GetModuleFileNameExA(process, null, name, 1024);
                String nameString= Native.toString(name);
                String[] nameStringSplitted =nameString.split("\\\\");
                if(!nameString.contains("explorer.exe") && !nameString.equals("") && !nameString.contains("svchost.exe") && !wText.equals("Default IME")) //filtering system windows
                    runningApps.add(nameStringSplitted[nameStringSplitted.length-1]);
                return true;
            }
        }, null);
        return runningApps;
    }

}
