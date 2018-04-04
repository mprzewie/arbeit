package pl.edu.agh.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class AppTracker {
    private String appToTrack;

    public AppTracker(String appToTrack) {
        this.appToTrack = appToTrack;
    }

    public void track() throws IOException, InterruptedException {

        while(true) {
            String line;
            String pidInfo ="";
            Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = input.readLine()) != null) {
                pidInfo += line;
            }

            input.close();

            if (pidInfo.contains(this.appToTrack)) {
                System.out.println(new Date().toString() + " App: " + this.appToTrack + " is running");
            }
            Thread.sleep(1000);
        }
    }

    public static void main(String [] args) throws IOException, InterruptedException {
        AppTracker appTracker = new AppTracker("Steam.exe");
        appTracker.track();
    }
}
