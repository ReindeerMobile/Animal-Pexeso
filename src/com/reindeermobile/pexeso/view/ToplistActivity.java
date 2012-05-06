package com.reindeermobile.pexeso.view;

import com.reindeermobile.pexeso.R;
import com.reindeermobile.pexeso.controller.DatabaseController;
import com.reindeermobile.pexeso.view.adapter.ToplistListViewAdapter;
import com.reindeermobile.reindeerutils.mvp.Presenter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ToplistActivity extends Activity {
	public static final String TAG = "ToplistActivity";

	private ListView recordListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_toplist);

		this.initLayout();

		Presenter.getInst()
				.sendModelMessage(DatabaseController.GET_RECORD_LIST);
	}

	private void initLayout() {
		this.recordListView = (ListView) findViewById(R.id.listView1);
		this.recordListView.setAdapter(new ToplistListViewAdapter(this));
	}
}
