package com.reindeermobile.pexeso.view;

import com.reindeermobile.pexeso.entity.Record;
import com.reindeermobile.reindeerorm.EntityManagerFactory;
import com.reindeermobile.reindeerutils.mvp.ReindeerBootstrap;

import android.app.Application;

public class PexesoApplication extends Application {

	@Override
	public void onCreate() {
		EntityManagerFactory.init(this, Record.class);
		ReindeerBootstrap.init(this);
	}
	
}
