package com.sensor;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("alerts")
public class Alert {
	
	public enum WeightType {
		UNDERWEIGHT, 
		OVERWEIGHT
	};
	
	@Id
	private ObjectId id;
	private double recordedWeight;
	private double baseWeight;
	private long timeStamp;
	private WeightType weightType;
	
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
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public WeightType getWeightType() {
		return weightType;
	}
	public void setWeightType(WeightType weightType) {
		this.weightType = weightType;
	}
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Alert)) {
			return false;
		}
		Alert alert = (Alert)object;
		boolean result = false;
		if(this.timeStamp == alert.timeStamp
				&& this.baseWeight == alert.baseWeight
				&& this.recordedWeight == alert.recordedWeight
				&& this.weightType.equals(alert.weightType)) {
			result = true;
		}
		return result;	
	}
	@Override
	public int hashCode() {
		return (int)this.timeStamp%(100000000);
	}
}
