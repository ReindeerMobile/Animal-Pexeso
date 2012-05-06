package com.reindeermobile.pexeso.entity;

import com.reindeermobile.reindeerutils.db.BaseDbEntity;
import com.reindeermobile.reindeerutils.db.DbAdapterFactory.Column;
import com.reindeermobile.reindeerutils.db.DbAdapterFactory.Table;

import android.os.Parcel;
import android.os.Parcelable;

@Table(name = "am_record")
public class Record extends BaseDbEntity implements Parcelable {
	@Column
	String name;

	@Column
	Float time;

	@Column
	Integer clicks;

	@Column
	Integer level;

	public final String getName() {
		return this.name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final Float getTime() {
		return this.time;
	}

	public final void setTime(Float time) {
		this.time = time;
	}

	public final Integer getClicks() {
		return this.clicks;
	}

	public final void setClicks(Integer clicks) {
		this.clicks = clicks;
	}

	public final Integer getLevel() {
		return this.level;
	}

	public final void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeFloat(this.time);
		dest.writeInt(this.level);
		dest.writeInt(this.clicks);
	}

}
