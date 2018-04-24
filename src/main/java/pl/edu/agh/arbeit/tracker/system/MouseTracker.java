package pl.edu.agh.arbeit.tracker.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class MouseTracker  {
        private int secondsSinceLastMoveNoticed=0;
        private Point lastMouseLocation = getCurrentMouseLocation();
        public MouseTracker(int intervalInSeconds) throws AWTException {
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(!checkAndUpdateLocation()){
                        secondsSinceLastMoveNoticed+=intervalInSeconds;
                    }
                }
            };
            Timer timer = new Timer(intervalInSeconds*1000, actionListener);
            timer.start();
        }

        public int getSecondsSinceLastMoveNoticed() {
            checkAndUpdateLocation();
            return secondsSinceLastMoveNoticed;
        }

        private Point getCurrentMouseLocation(){
            return MouseInfo.getPointerInfo().getLocation();
        }

        private boolean checkAndUpdateLocation(){
            if (!lastMouseLocation.equals(getCurrentMouseLocation())) {
                secondsSinceLastMoveNoticed=0;
                lastMouseLocation = getCurrentMouseLocation();
                return true;
            }
            return false;
        }
    }

