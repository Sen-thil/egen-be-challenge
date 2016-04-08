package com.sensor;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mongodb.morphia.Datastore;

import com.sensor.Alert.WeightType;

@RunWith(JUnit4.class)
public class GreaterRuleTest {
	
	private Datastore dataStore;
	
	@Before
	public void setup() {
		dataStore = Mockito.mock(Datastore.class);
	}
	
	@Test
	public void testGreaterThan() {
		Metric metric = new Metric();
		metric.setBaseWeight(100);
		metric.setRecordedWeight(115);
		metric.setTimeStamp(1000);
		Alert alert = new Alert();
		alert.setBaseWeight(100);
		alert.setRecordedWeight(115);
		alert.setTimeStamp(1000);
		alert.setWeightType(WeightType.OVERWEIGHT);
		GreaterRule greaterRule = new GreaterRule(metric, dataStore);
		Assert.assertTrue(greaterRule.calculate());
		greaterRule.action();
		Mockito.verify(dataStore).save(alert);
	}
	
	@Test
	public void testGreaterThanWithoutAlert() {
		Metric metric = new Metric();
		metric.setBaseWeight(100);
		metric.setRecordedWeight(90);
		metric.setTimeStamp(1000);
		GreaterRule greaterRule = new GreaterRule(metric, dataStore);
		Assert.assertFalse(greaterRule.calculate());
	}
	
	@Test
	public void testGreaterThanWithoutAlert10Percent() {
		Metric metric = new Metric();
		metric.setBaseWeight(100);
		metric.setRecordedWeight(109);
		metric.setTimeStamp(1000);
		GreaterRule greaterRule = new GreaterRule(metric, dataStore);
		Assert.assertFalse(greaterRule.calculate());
	}
	
}
