package com.sensor;



import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;
import org.mongodb.morphia.Datastore;

import com.sensor.Alert.WeightType;

@Rule(name = "GreaterRule")
public class GreaterRule {

	private Metric metric;
	private Datastore dataStore;
	
	public GreaterRule(Metric metric, Datastore dataStore) {
		this.metric = metric;
		this.dataStore = dataStore;
	}
	
	@Condition
	public boolean calculate() {
		double value = (metric.getRecordedWeight() - metric.getBaseWeight())/metric.getBaseWeight();
		if((value * 100) > 10) {
			return true;
		} else {
			return false;
		}
	}
	
	@Action
	public void action() {
		Alert alert = new Alert();
		alert.setBaseWeight(metric.getBaseWeight());
		alert.setRecordedWeight(metric.getRecordedWeight());
		alert.setTimeStamp(metric.getTimeStamp());
		alert.setWeightType(WeightType.OVERWEIGHT);
		dataStore.save(alert);
	}
}
