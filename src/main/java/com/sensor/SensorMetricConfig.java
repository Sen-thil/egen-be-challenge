package com.sensor;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

@Configuration
public class SensorMetricConfig {
	
	@Bean
	public Datastore dataStore() throws UnknownHostException {
		Morphia morphia = new Morphia();
		morphia.mapPackage("com.sensor");
		Datastore dataStore = morphia.createDatastore(new MongoClient(), "test_example");
		dataStore.ensureIndexes();
		return dataStore;
	}
	
}
