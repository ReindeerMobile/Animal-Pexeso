package com.reindeermobile.pexeso.controller;

import com.reindeermobile.pexeso.entity.Record;
import com.reindeermobile.reindeerutils.db.DbAdapterFactory;
import com.reindeermobile.reindeerutils.db.IDatabaseAdapter;
import com.reindeermobile.reindeerutils.mvp.AbstractController;
import com.reindeermobile.reindeerutils.mvp.MessageObject;
import com.reindeermobile.reindeerutils.mvp.Presenter;
import com.reindeermobile.reindeerutils.mvp.Presenter.ControllerServices;
import com.reindeermobile.reindeerutils.mvp.Presenter.ViewServices;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class DatabaseController extends AbstractController {
	public static final String RECORD_LIST = "RECORD_LIST";

	public static final String SAVE_RECORD = "SAVE_RECORD";
	public static final String GET_RECORD_LIST = "GET_RECORD_LIST";
	public static final String CLEAR_RECORD_LIST = "CLEAR_RECORD_LIST";

	public static final String SAVE_RECORD_OK = "SAVE_RECORD_OK";
	public static final String SEND_RECORD_LIST = "SEND_RECORD_LIST";

	@ControllerServices
	public static final String[] CONTROLLER_SERVICES = new String[] {
			SAVE_RECORD, GET_RECORD_LIST, CLEAR_RECORD_LIST };

	@ViewServices
	public static final String[] VIEW_SERVICES = new String[] { SAVE_RECORD_OK,
			SEND_RECORD_LIST };

	private IDatabaseAdapter<Record> databaseAdapter;

	@Override
	public void init(Context context) {
		DbAdapterFactory.INSTANCE.init(Record.class);

		this.databaseAdapter = DbAdapterFactory.createInstance(Record.class,
				context, "am_pexeso_database", 1);

		this.initTasks();
	}

	@Override
	protected void initTasks() {
		super.registerTask(SAVE_RECORD, new ContollerTask() {
			@Override
			public void execute(Callback sender, MessageObject messageObject) {
				if (messageObject != null
						&& messageObject.hasData(Record.class)) {
					Record record = (Record) messageObject.getData();
					Log.d(TAG, "execute - before persist: " + record);
					databaseAdapter.insert(record);
					Log.d(TAG, "execute - after persist: " + record);
				}
			}
		});

		super.registerTask(CLEAR_RECORD_LIST, new ContollerTask() {
			@Override
			public void execute(Callback sender, MessageObject messageObject) {
				Log.d(TAG, "execute - clear");
				databaseAdapter.clear();
				sendRecords();
			}
		});

		super.registerTask(GET_RECORD_LIST, new ContollerTask() {
			@Override
			public void execute(Callback sender, MessageObject messageObject) {
				sendRecords();
			}
		});
	}

	private void sendRecords() {
		ArrayList<Record> records = (ArrayList<Record>) databaseAdapter.list();

		if (records != null) {
			Collections.sort(records);
		}

		Log.d(TAG, "sendRecords - records: " + records);

		Bundle messageBundle = new Bundle();
		messageBundle.putParcelableArrayList(RECORD_LIST, records);

		Presenter.getInst().sendViewMessage(SEND_RECORD_LIST, messageBundle);

	}
}
