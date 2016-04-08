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
public class LessRuleTest {
	
	private Datastore dataStore;
	
	@Before
	public void setup() {
		dataStore = Mockito.mock(Datastore.class);
	}
	
	@Test
	public void testLessThan() {
		Metric metric = new Metric();
		metric.setBaseWeight(100);
		metric.setRecordedWeight(80);
		metric.setTimeStamp(1000);
		Alert alert = new Alert();
		alert.setBaseWeight(100);
		alert.setRecordedWeight(80);
		alert.setTimeStamp(1000);
		alert.setWeightType(WeightType.UNDERWEIGHT);
		LessRule lessRule = new LessRule(metric, dataStore);
		Assert.assertTrue(lessRule.calculate());
		lessRule.action();
		Mockito.verify(dataStore).save(alert);
	}
	
	@Test
	public void testLessThanWithoutAlert() {
		Metric metric = new Metric();
		metric.setBaseWeight(100);
		metric.setRecordedWeight(110);
		metric.setTimeStamp(1000);
		LessRule lessRule = new LessRule(metric, dataStore);
		Assert.assertFalse(lessRule.calculate());
	}
	
	@Test
	public void testLessThanWithoutAlert10Percent() {
		Metric metric = new Metric();
		metric.setBaseWeight(100);
		metric.setRecordedWeight(91);
		metric.setTimeStamp(1000);
		LessRule lessRule = new LessRule(metric, dataStore);
		Assert.assertFalse(lessRule.calculate());
	}
	
}
