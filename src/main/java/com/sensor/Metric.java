package com.sensor;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("metrics")
public class Metric {
	@Id
	private ObjectId id;
	private double recordedWeight;
	private double baseWeight;
	private long timeStamp;
	
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public double getRecordedWeight() {
		return recordedWeight;
	}
	public void setRecordedWeight(double recordedWeight) {
		this.recordedWeight = recordedWeight;
	}
	public double getBaseWeight() {
		return baseWeight;
	}
	public void setBaseWeight(double baseWeight) {
		this.baseWeight = baseWeight;
	}
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Metric)) {
			return false;
		}
		Metric metric = (Metric)object;
		boolean result = false;
		if(this.timeStamp==metric.timeStamp
				&& this.baseWeight == metric.baseWeight
				&& this.recordedWeight == metric.recordedWeight) {
			result = true;
		}
		return result;
	}
	@Override
	public int hashCode() {
		return (int)this.timeStamp%(100000000);
	}
}
