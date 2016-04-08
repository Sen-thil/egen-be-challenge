package com.sensor;

import java.net.UnknownHostException;
import java.util.List;

import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class SensorMetricController {
	
	private final Datastore dataStore;
	
	@Autowired
	public SensorMetricController(Datastore dataStore) throws UnknownHostException {
		this.dataStore = dataStore;
	} 
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Metric create(
			@RequestBody CreateInput createInput) {
		//Check if baseweight is exiting
		List<BaseWeight> baseWeightList = dataStore.find(BaseWeight.class).asList();
		double baseWeight = 0;
		if(baseWeightList.size() == 0) {
			BaseWeight b = new BaseWeight();
			baseWeight = Double.parseDouble(createInput.getValue()); 
			b.setValue(baseWeight);
			dataStore.save(b);
		} else {
			baseWeight = baseWeightList.get(0).getValue();
		}
		Metric metric = new Metric();
		metric.setBaseWeight(baseWeight);
		metric.setRecordedWeight(Double.parseDouble(createInput.getValue()));
		metric.setTimeStamp(Long.parseLong(createInput.getTimeStamp()));
		if(metric.getRecordedWeight() < 0 || metric.getRecordedWeight() > 500) {
			return null;
		}
		dataStore.save(metric);
		checkWeight(metric);
		return metric;
	}
	
	private void checkWeight(Metric metric) {
		RulesEngine rulesEngine = RulesEngineBuilder.aNewRulesEngine().build();
        rulesEngine.registerRule(new LessRule(metric, dataStore));
        rulesEngine.registerRule(new GreaterRule(metric, dataStore));
        rulesEngine.fireRules();
	}
	
	@RequestMapping(value = "/metrics/read", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Metric> readMetrics() {
		Query<Metric> query = dataStore.find(Metric.class);
		return query.asList();
	}
	
	@RequestMapping(value = "/alerts/read", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Alert> readAlerts() {
		return dataStore.find(Alert.class).asList();
	}
	
	@RequestMapping(value = "/metrics/readByTimeRange", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Metric> readMetricsByTimeRange(
			@RequestParam(value="startTime", required=true) Long startTime, 
			@RequestParam(value="endTime", required=true) Long endTime) {
		if(startTime > endTime) {
			throw new InvalidDataException("invalid time");
		}
		//inclusive on both ends
		return dataStore.find(Metric.class)
				.field("timeStamp").greaterThanOrEq(startTime)
				.field("timeStamp").lessThanOrEq(endTime)
				.asList();
	}
	
	@RequestMapping(value = "/alerts/readByTimeRange", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Alert> readAlertsByTimeRange(
			@RequestParam(value="startTime", required=true) Long startTime, 
			@RequestParam(value="endTime", required=true) Long endTime) {
		if(startTime > endTime) {
			throw new InvalidDataException("invalid time");
		}
		//inclusive on both ends
		return dataStore.find(Alert.class)
				.field("timeStamp").greaterThanOrEq(startTime)
				.field("timeStamp").lessThanOrEq(endTime)
				.asList();
	}
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Invalid Data")  
    public static class InvalidDataException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public InvalidDataException(String message) {
			super(message);
		}
    }
	
}
