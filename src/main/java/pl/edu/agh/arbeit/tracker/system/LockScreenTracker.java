package pl.edu.agh.arbeit.tracker.system;

import com.sun.jna.platform.win32.*;

import static com.sun.jna.Native.getLastError;

/**
 * Created by Albert Mosiałek on 10.05.2018.
 */
public class LockScreenTracker implements WinUser.WindowProc{

    public boolean isScreenLocked() {
        return isScreenLocked;
    }

    boolean isScreenLocked = false;

    public LockScreenTracker() {
        // define new window class
        String windowClass = new String("MyWindowClass");
        WinDef.HMODULE hInst = Kernel32.INSTANCE.GetModuleHandle("");

        WinUser.WNDCLASSEX wClass = new WinUser.WNDCLASSEX();
        wClass.hInstance = hInst;
        wClass.lpfnWndProc = LockScreenTracker.this;
        wClass.lpszClassName = windowClass;

        // register window class
        User32.INSTANCE.RegisterClassEx(wClass);
        getLastError();

        // create new window
        WinDef.HWND hWnd = User32.INSTANCE
                .CreateWindowEx(
                        User32.WS_EX_TOPMOST,
                        windowClass,
                        "My hidden helper window, used only to catch the windows events",
                        0, 0, 0, 0, 0,
                        null, // WM_DEVICECHANGE contradicts parent=WinUser.HWND_MESSAGE
                        null, hInst, null);

        getLastError();

        Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hWnd,
                Wtsapi32.NOTIFY_FOR_THIS_SESSION);


        getLastError();

        WinUser.MSG msg = new WinUser.MSG();
        while (User32.INSTANCE.GetMessage(msg, hWnd, 0, 0) != 0) {
            User32.INSTANCE.TranslateMessage(msg);
            User32.INSTANCE.DispatchMessage(msg);
        }

        Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(hWnd);
        User32.INSTANCE.UnregisterClass(windowClass, hInst);
        User32.INSTANCE.DestroyWindow(hWnd);

        System.out.println("program exit!");
    }

    @Override
    public WinDef.LRESULT callback(WinDef.HWND hwnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
        switch (uMsg) {
            case WinUser.WM_CREATE: {
                onCreate(wParam, lParam);
                return new WinDef.LRESULT(0);
            }
            case WinUser.WM_DESTROY: {
                User32.INSTANCE.PostQuitMessage(0);
                return new WinDef.LRESULT(0);
            }
            case WinUser.WM_SESSION_CHANGE: {
                this.onSessionChange(wParam, lParam);
                return new WinDef.LRESULT(0);
            }

            default:
                return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
        }
    }
    protected void onSessionChange(WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
        switch (wParam.intValue()) {

            case Wtsapi32.WTS_SESSION_LOGON: {
                break;
            }
            case Wtsapi32.WTS_SESSION_LOGOFF: {
                break;
            }
            case Wtsapi32.WTS_SESSION_LOCK: {
                isScreenLocked=true;
                break;
            }
            case Wtsapi32.WTS_SESSION_UNLOCK: {
                isScreenLocked=false;
                break;
            }
        }
    }
  



    protected void onCreate(WinDef.WPARAM wParam, WinDef.LPARAM lParam) {

    }

}
