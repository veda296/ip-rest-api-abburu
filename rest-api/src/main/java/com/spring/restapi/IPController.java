package com.spring.restapi;

import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IPController {
	
	@Autowired
	IPService service;
	
	@GetMapping("/list")
	public List<IPAddress> list() {
		return service.getAllAddresses();	
	}
	
	@GetMapping("/acquire/{id}")
	public ResponseEntity<String> acquire(@PathVariable String id) {
		try {
			service.acquireIPAddress(id);
			return new ResponseEntity<>("IP address acquired: " + id, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Acquire failed for IP address: " + id + 
					" Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/release/{id}")
	public ResponseEntity<String> release(@PathVariable String id) {
		try {
			service.releaseIPAddress(id);
			return new ResponseEntity<>("IP address released: " + id, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Release failed for IP address: " + id + 
					" Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	

	@PostMapping(path = "/create")
	public ResponseEntity<String> create(@RequestBody Map<String, String> m) {
		try {
			service.createIPAddresses(m);
			return new ResponseEntity<>("IP addresses created for: " + m.get("cidr"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("IP address creation failed. Error: " + e.getMessage(), 
					HttpStatus.BAD_REQUEST);
		}

	}


}
