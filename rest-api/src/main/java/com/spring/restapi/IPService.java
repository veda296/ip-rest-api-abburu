package com.spring.restapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IPService {
	
	@Autowired
	IPRepository repo;
	
	public String acquireIPAddress(String id) throws Exception {
		// Check if address exists
		Optional<IPAddress> res = repo.getAddress(id);
		if (!res.isPresent()) {
			throw new Exception("IP doesn't exist");
		}
		
		// Check that address is not already acquired
		if (res.get().isAvailable() == false) {
			throw new Exception("IP already acquired");
		}
		
		// Acquire IP
		repo.acquireIP(id);
		return "Acquired IP successfully";
	}
	
	public String releaseIPAddress (String id) throws Exception {
		
		// Check if address exists
		Optional<IPAddress> res = repo.getAddress(id);
		if (!res.isPresent()) {
			throw new Exception( "IP doesn't exist");
		}
		
		// Check that address is not already released
		if (res.get().isAvailable() == true) {
			throw new Exception("IP already released");
		}
		
		// Release IP
		repo.releaseIP(id);
		return "Released IP successfully";

	}
	
	public String createIPAddresses ( Map<String, String> m) {		
		// Get cidr value
		String c = m.get("cidr");		
		int cidr_bits = Integer.parseInt(c.split("/")[1]);
		
		// Get binary representation of given IP address
		String [] binaryIP = new String[4];
		int blocknum=0;
		for (String block : c.split("/")[0].split("\\.")) {
			int i = Integer.parseInt(block);
			binaryIP[blocknum] = Integer.toBinaryString(i);
			blocknum++;
		}

		// Get binary representation of IP address excluding reserved cidr bits (subnet)
		String [] subnet = new String[4];
		int i=0;
		while ( i<4 && (i+1)*8 <= cidr_bits) {
			subnet[i] = binaryIP[i];
			i++;
		}
		StringBuilder temp = new StringBuilder("");
		for (int j=i*8; j<32; j++) {
			if (j%8==0) {
				temp = new StringBuilder("");
			}
			if (j<cidr_bits) 
				temp.append(binaryIP[j/8].charAt(j%8)); 
			else 
				temp.append('0');
			if (j%8 == 7) {
				subnet[j/8] = temp.toString();
			}
		}
		// Generate list of IP addresses available in given CIDR block
		List<IPAddress> res = new ArrayList<>();
		genIPAddresses(subnet, cidr_bits, res);
		
		// Add list of new addresses to DB
		repo.saveAll(res);
		return "IP addresses created successfully";
	}

	// Recursively generate list of available IP addresses
	private void genIPAddresses(String[] subnet, int bits, List<IPAddress> res) {
		
		if (bits>32) return;
		
		if (bits==32) {
			res.add(new IPAddress(String.join(".", binaryToStringIP(subnet))));
			return;
		}
		
		String [] ip = subnet;
		
		ip[bits/8] = subnet[bits/8].substring(0, bits%8) + '0';
		genIPAddresses(ip, bits+1, res);
		
		ip[bits/8] = subnet[bits/8].substring(0, bits%8) + '1';
		genIPAddresses(ip, bits+1, res);
		
	}
	
	// Given IP address as string array in binary, convert to string array in decimal
	private String[] binaryToStringIP (String[] ip) {
		return new String [] {String.valueOf(Integer.parseInt(ip[0], 2)),
				String.valueOf(Integer.parseInt(ip[1], 2)),
				String.valueOf(Integer.parseInt(ip[2], 2)),
				String.valueOf(Integer.parseInt(ip[3], 2))};
	}

	public List<IPAddress> getAllAddresses() {
		
		return repo.getAll();
	}

}
