package pl.edu.agh.arbeit.tracker.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class MouseTracker  {
        private int secondsSinceLastMoveNoticed=0;
        public MouseTracker(int intervalInSeconds) throws AWTException {
            ActionListener actionListener = new ActionListener() {

                Point lastMouseLocation;

                @Override
                public void actionPerformed(ActionEvent e) {
                    Point currentMouseLocation = MouseInfo.getPointerInfo().getLocation();
                    if (!currentMouseLocation.equals(lastMouseLocation)) {
                        secondsSinceLastMoveNoticed=0;
                        lastMouseLocation = currentMouseLocation;
                    }
                    else{
                        secondsSinceLastMoveNoticed+=intervalInSeconds;
                    }
                }
            };
            Timer timer = new Timer(intervalInSeconds*1000, actionListener);
            timer.start();
        }

        public int getSecondsSinceLastMoveNoticed() {
            return secondsSinceLastMoveNoticed;
        }
    }

