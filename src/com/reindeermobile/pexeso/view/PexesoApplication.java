package com.reindeermobile.pexeso.view;

import android.app.Application;

import com.appbrain.AppBrain;
import com.reindeermobile.pexeso.entity.Record;
import com.reindeermobile.reindeerorm.EntityManagerFactory;
import com.reindeermobile.reindeerutils.mvp.ReindeerBootstrap;

public class PexesoApplication extends Application {

	@Override
	public void onCreate() {
		EntityManagerFactory.init(this, Record.class);
		ReindeerBootstrap.init(this);
		AppBrain.initApp(this);
	}
	
}
