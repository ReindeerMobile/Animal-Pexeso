package com.reindeermobile.pexeso.entity;

import com.reindeermobile.reindeerorm.annotations.Table;
import com.reindeermobile.reindeerorm.annotations.AutoIncrement;
import com.reindeermobile.reindeerorm.annotations.Column;
import com.reindeermobile.reindeerorm.annotations.Id;
import com.reindeermobile.reindeerorm.annotations.NativeNamedQueries;
import com.reindeermobile.reindeerorm.annotations.NativeNamedQuery;

import android.os.Parcel;
import android.os.Parcelable;

@Table(name = "am_record")
@NativeNamedQueries({
		@NativeNamedQuery(name = "listRecords", query = "select * from am_record order by clicks"),
		@NativeNamedQuery(name = "deleteAll", query = "delete from am_record") })
public class Record implements Comparable<Record>, Parcelable {
	@Id
	@AutoIncrement
	@Column(name = "_id")
	private Long id;

	@Column
	private String name;

	@Column
	private Float time;

	@Column
	private Integer clicks;

	@Column
	private Integer level;

	public Record() {
		super();
		time = 0f;
		name = "";
		clicks = 0;
		level = 0;
	}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public int compareTo(Record other) {
		if (other != null) {
			if (this.time < other.time) {
				return -1;
			} else if (this.time > other.time) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	@Override
	public String toString() {
		return "Record [id=" + this.id + ", name=" + this.name + ", time="
				+ this.time + ", clicks=" + this.clicks + ", level="
				+ this.level + "]";
	}

}
