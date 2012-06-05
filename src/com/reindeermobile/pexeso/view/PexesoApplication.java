package com.reindeermobile.pexeso.view;

import com.reindeermobile.reindeerutils.mvp.ReindeerBootstrap;

import android.app.Application;

public class PexesoApplication extends Application {

	@Override
	public void onCreate() {
		ReindeerBootstrap.init(this);
	}
	
}
