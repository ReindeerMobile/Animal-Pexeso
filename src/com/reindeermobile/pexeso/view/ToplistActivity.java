package com.reindeermobile.pexeso.view;

import com.reindeermobile.pexeso.R;
import com.reindeermobile.pexeso.controller.DatabaseController;
import com.reindeermobile.pexeso.view.adapter.ToplistListViewAdapter;
import com.reindeermobile.reindeerutils.mvp.Presenter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ToplistActivity extends Activity implements OnClickListener {
	public static final String TAG = "ToplistActivity";

	private ListView recordListView;
	private Button clearButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_toplist);

		this.initLayout();

		Presenter.getInst()
				.sendModelMessage(DatabaseController.GET_RECORD_LIST);
	}

	@Override
	public void onClick(View view) {
		if (view != null) {
			switch (view.getId()) {
			case R.id.buttonClear:
				Presenter.getInst().sendModelMessage(DatabaseController.CLEAR_RECORD_LIST);
				break;
			default:
				Log.w(TAG, "onClick - unknown view");
				break;
			}
		}
	}
	
	private void initLayout() {
		this.recordListView = (ListView) findViewById(R.id.listView1);
		this.recordListView.setAdapter(new ToplistListViewAdapter(this));
		this.clearButton = (Button) findViewById(R.id.buttonClear);
		this.clearButton.setOnClickListener(this);
	}
}
