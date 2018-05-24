package pl.edu.agh.arbeit.tracker.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.Duration;

public class MouseTracker  {
        private LocalDateTime lastMoveNoticedTime = LocalDateTime.now();
        private Point lastMouseLocation = getCurrentMouseLocation();

        public MouseTracker(Duration timeToBecomePassive) {
            ActionListener actionListener = e -> {
                checkAndUpdateLocation();

            };
            Timer timer = new Timer((int)timeToBecomePassive.getSeconds() * 1000, actionListener);
            timer.start();
        }

        public Duration getTimeSinceLastMoveNoticed() {
            checkAndUpdateLocation();
            return Duration.between(lastMoveNoticedTime, LocalDateTime.now());
        }

        private Point getCurrentMouseLocation(){
            return MouseInfo.getPointerInfo().getLocation();
        }

        private boolean checkAndUpdateLocation(){
            if (!lastMouseLocation.equals(getCurrentMouseLocation())) {
                lastMoveNoticedTime = LocalDateTime.now();
                lastMouseLocation = getCurrentMouseLocation();
                return true;
            }
            return false;
        }
    }

