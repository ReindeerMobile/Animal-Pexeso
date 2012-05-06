package com.reindeermobile.pexeso.view.adapter;

import com.reindeermobile.pexeso.R;
import com.reindeermobile.pexeso.controller.DatabaseController;
import com.reindeermobile.pexeso.entity.Record;
import com.reindeermobile.reindeerutils.mvp.Presenter;
import com.reindeermobile.reindeerutils.mvp.ViewHandler;
import com.reindeermobile.reindeerutils.mvp.ViewHandler.IViewTask;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ToplistListViewAdapter extends BaseAdapter {
	public static final String TAG = "ToplistListViewAdapter";

	private List<Record> recordList;
	private Context context;
	private ViewHandler viewHandler;
	private RecordListItemHolder recordListItemHolder;

	static class RecordListItemHolder {
		TextView nameTextView;
		TextView clickTextView;
		TextView timeTextView;
	}

	public ToplistListViewAdapter(Context context) {
		super();
		this.context = context;
		this.viewHandler = new ViewHandler(TAG);
		this.recordList = new ArrayList<Record>();

		this.initTasks();

		Presenter.getInst().subscribeToServices(this.viewHandler,
				DatabaseController.SEND_RECORD_LIST);
	}

	public int getCount() {
		return this.recordList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.recordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Record record = this.recordList.get(position);
		String name = record.getName();
		int time = record.getClicks();

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.listitem_toplist, parent,
					false);

			recordListItemHolder = new RecordListItemHolder();
			recordListItemHolder.nameTextView = (TextView) convertView
					.findViewById(R.id.textViewName);
			recordListItemHolder.clickTextView = (TextView) convertView
					.findViewById(R.id.textViewClick);
			recordListItemHolder.timeTextView = (TextView) convertView
					.findViewById(R.id.textViewTime);

			convertView.setTag(recordListItemHolder);
		} else {
			recordListItemHolder = (RecordListItemHolder) convertView.getTag();
		}

		recordListItemHolder.nameTextView.setText(name);
		recordListItemHolder.clickTextView.setText(String.valueOf(time));
		recordListItemHolder.timeTextView.setText(String.valueOf(time));
		return convertView;
	}
	
	private void initTasks() {
		this.viewHandler.registerViewTask(DatabaseController.SEND_RECORD_LIST,
				new IViewTask() {
					@Override
					public void execute(Object obj, Bundle bundle) {
						ArrayList<Record> newRecords = bundle
								.getParcelableArrayList(DatabaseController.RECORD_LIST);
						if (newRecords != null) {
							recordList = newRecords;
							notifyDataSetChanged();
						}
					}
				});
	}

}
