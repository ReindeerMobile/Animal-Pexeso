package com.reindeermobile.pexeso.view;

import org.andengine.entity.scene.Scene;

public class SplashScene extends Scene {
    MemoryActivity activity;

    public SplashScene() {
        activity = MemoryActivity.getSharedInstance();
    }
}
