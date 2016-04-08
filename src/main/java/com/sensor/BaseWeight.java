package com.sensor;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("baseweights")
public class BaseWeight {

	@Id
	private ObjectId id;
	private double value;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof BaseWeight)) {
			return false;
		}
		BaseWeight baseWeight = (BaseWeight)object;
		boolean result = false;
		if(this.value==baseWeight.value) {
			result = true;
		}
		return result;
	}
	@Override
	public int hashCode() {
		return (int)this.value%(100000000);
	}
	
}
