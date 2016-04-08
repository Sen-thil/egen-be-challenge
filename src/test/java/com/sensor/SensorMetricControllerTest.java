package com.sensor;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.FieldEnd;
import org.mongodb.morphia.query.Query;

import com.sensor.Alert.WeightType;


@RunWith(JUnit4.class)
public class SensorMetricControllerTest {
	
private Datastore dataStore;
	
	@Before
	public void setup() {
		dataStore = Mockito.mock(Datastore.class);
	}
	
	@Test
	public void testCreate() throws UnknownHostException {
		Query<BaseWeight> baseWeight = Mockito.mock(Query.class);
		Mockito.when(dataStore.find(BaseWeight.class)).thenReturn(baseWeight);
		CreateInput createInput = new CreateInput();
		createInput.setValue("150");
		createInput.setTimeStamp("1000");
		SensorMetricController sensorMetricController = new SensorMetricController(dataStore);
		Metric m = sensorMetricController.create(createInput);
		BaseWeight b = new BaseWeight();
		b.setValue(150);
		Mockito.verify(dataStore).save(b);
		Metric metric = new Metric();
		metric.setBaseWeight(150);
		metric.setTimeStamp(1000);
		metric.setRecordedWeight(150);
		Mockito.verify(dataStore).save(metric);
		Assert.assertEquals(metric, m);
	}
	
	@Test
	public void testCreateAlert() throws UnknownHostException {
		Query<BaseWeight> baseWeightQuery = Mockito.mock(Query.class);
		List<BaseWeight> list = new ArrayList<>();
		BaseWeight b = new BaseWeight();
		b.setValue(150);
		list.add(b);
		Mockito.when(baseWeightQuery.asList()).thenReturn(list);
		Mockito.when(dataStore.find(BaseWeight.class)).thenReturn(baseWeightQuery);
		CreateInput createInput = new CreateInput();
		createInput.setValue("180");
		createInput.setTimeStamp("1000");
		SensorMetricController sensorMetricController = new SensorMetricController(dataStore);
		Metric m = sensorMetricController.create(createInput);
		
		Metric metric = new Metric();
		metric.setBaseWeight(150);
		metric.setTimeStamp(1000);
		metric.setRecordedWeight(180);
		Mockito.verify(dataStore).save(metric);
		Assert.assertEquals(metric, m);
		
		Alert alert = new Alert();
		alert.setBaseWeight(150);
		alert.setTimeStamp(1000);
		alert.setRecordedWeight(180);
		alert.setWeightType(WeightType.OVERWEIGHT);
		Mockito.verify(dataStore).save(alert);
	}
	
	@Test
	public void testReadMetrics() throws UnknownHostException {
		Query<Metric> query = Mockito.mock(Query.class);
		List<Metric> list = new ArrayList<>();
		Metric metric = new Metric();
		metric.setBaseWeight(150);
		metric.setTimeStamp(1000);
		metric.setRecordedWeight(180);
		list.add(metric);
		metric.setBaseWeight(150);
		metric.setTimeStamp(1001);
		metric.setRecordedWeight(181);
		list.add(metric);
		Mockito.when(query.asList()).thenReturn(list);
		Mockito.when(dataStore.find(Metric.class)).thenReturn(query);
		SensorMetricController sensorMetricController = new SensorMetricController(dataStore);
		List<Metric> m = sensorMetricController.readMetrics();
		Assert.assertEquals(list, m);
	}
	
	@Test
	public void readMetricsByTimeRangeInvalidRange() throws UnknownHostException {
		SensorMetricController sensorMetricController = new SensorMetricController(dataStore);
		try {
			sensorMetricController.readMetricsByTimeRange(1500l, 500l);
		} catch(Exception e) {
			//Exception has occurred which is expected
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void testReadAlerts() throws UnknownHostException {
		Query<Alert> query = Mockito.mock(Query.class);
		List<Alert> list = new ArrayList<>();
		Alert alert = new Alert();
		alert.setBaseWeight(150);
		alert.setTimeStamp(1000);
		alert.setRecordedWeight(180);
		alert.setWeightType(WeightType.OVERWEIGHT);
		list.add(alert);
		alert.setBaseWeight(150);
		alert.setTimeStamp(1001);
		alert.setRecordedWeight(181);
		alert.setWeightType(WeightType.OVERWEIGHT);
		list.add(alert);
		Mockito.when(query.asList()).thenReturn(list);
		Mockito.when(dataStore.find(Alert.class)).thenReturn(query);
		SensorMetricController sensorMetricController = new SensorMetricController(dataStore);
		List<Alert> m = sensorMetricController.readAlerts();
		Assert.assertEquals(list, m);
	}
	
	@Test
	public void readAlertsByTimeRangeInvalidRange() throws UnknownHostException {
		SensorMetricController sensorMetricController = new SensorMetricController(dataStore);
		try {
			sensorMetricController.readAlertsByTimeRange(1500l, 500l);
		} catch(Exception e) {
			//Exception has occurred which is expected
			return;
		}
		Assert.fail();
	}

}
