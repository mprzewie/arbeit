package pl.edu.agh.arbeit.tracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppTracker {
    private CopyOnWriteArrayList<String> applicationsToTrack;

    public AppTracker(CopyOnWriteArrayList<String> applicationsToTrack) {
        this.applicationsToTrack = applicationsToTrack;
    }

    public void track() throws IOException, InterruptedException {

        Thread appTrackThread = new Thread() {
            public void run() {
                while(true) {
                    try {
                        String line;
                        Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
                        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        while ((line = input.readLine()) != null) {
                            for (String appToTrack : applicationsToTrack) {
                                if (line.contains(appToTrack)) {
                                    //Not print but add to some class
                                    System.out.println(new Date().toString() + " App: " + appToTrack + " is running");
                                    break;
                                }
                            }
                        }
                        input.close();
                        Thread.sleep(1000);
                    }catch (Exception e){

                    }
                }
            }
        };
        appTrackThread.start();
    }

    public void addAppToTrack(String appToTrack){
        this.applicationsToTrack.add(appToTrack);
    }

    public static void main(String [] args) throws IOException, InterruptedException {
        AppTracker appTracker = new AppTracker(new CopyOnWriteArrayList<String>());
        appTracker.track();

        Scanner sc = new Scanner(System.in);
        String input = "";
        while(true){
            input = sc.nextLine();
            input = input.replace("\n","");
            appTracker.addAppToTrack(input);
        }

    }
}
