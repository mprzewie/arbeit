package pl.edu.agh.arbeit.tracker.system;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class KeyboardTracker extends Thread{
    WinUser.HHOOK hhk;
    LocalDateTime lastCheckTimestamp = LocalDateTime.now();
    public void run(){
        User32 lib = User32.INSTANCE;
        WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        WinUser.LowLevelKeyboardProc keyboardHook = (int nCode, WinDef.WPARAM wParam, WinUser.KBDLLHOOKSTRUCT info) -> {
            lastCheckTimestamp = LocalDateTime.now();
            return lib.CallNextHookEx(hhk, nCode, wParam, null);
        };
        hhk = lib.SetWindowsHookEx(13, keyboardHook, hMod, 0);
        int result;
        WinUser.MSG msg = new WinUser.MSG();
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                break;
            } else {
                lib.TranslateMessage(msg);
                lib.DispatchMessage(msg);
            }
        }
        lib.UnhookWindowsHookEx(hhk);
    }

    public Duration getTimeSinceLastKeyPressed() {
        return Duration.between(lastCheckTimestamp, LocalDateTime.now());
    }
}
