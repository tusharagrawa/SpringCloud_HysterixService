package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.HystrixProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class SimpleHysterixController {
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/")
	@HystrixCommand(commandProperties = {
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "1000"),
			@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "100")
	}, fallbackMethod = "defaultInfo")
	public String info() {
		System.out.println("Inside Hysterix");
		String msg = restTemplate.getForObject("http://DiscoveryService/", String.class);
		return "Hysterix Controller is calling : " + msg;
	}
	
	public String defaultInfo() {
		return "Fallback in Action" ;
	}
}
