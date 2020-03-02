package com.spring.restapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestApiApplicationTests {

	@Mock
	IPRepository repo;
	
	@InjectMocks
	IPService service;
	
	@Captor
    ArgumentCaptor<List<IPAddress>> captor;
	
	@Test
	void acquireIP_notExist() {
		// IP doesn't exist -> throw exception
		when(repo.getAddress("abc")).thenReturn(Optional.empty());		
		Exception e = Assertions.assertThrows(Exception.class, 
				()->{service.acquireIPAddress("abc");}
				);
		assertThat (e).hasMessageContaining("IP doesn't exist");
	}
	
	@Test
	void acquireIP_areadyAcquired() {
		// IP already acquired -> throw exception
		when(repo.getAddress("abc")).thenReturn(Optional.of(new IPAddress("abc", false)));
		Exception e = Assertions.assertThrows(Exception.class, 
				()->{service.acquireIPAddress("abc");}
				);
		assertThat (e).hasMessageContaining("IP already acquired");
	}
	
	@Test
	void acquireIP_success() throws Exception {
		// IP acquired successfully 
		when(repo.getAddress("abc")).thenReturn(Optional.of(new IPAddress("abc", true)));
		assertThat(service.acquireIPAddress("abc")).contains("Acquired IP successfully");
	}
	
	@Test
	void releaseIP_notExist() {
		// IP doesn't exist -> throw exception
		when(repo.getAddress("abc")).thenReturn(Optional.empty());
		Exception e = Assertions.assertThrows(Exception.class, 
				()->{service.releaseIPAddress("abc");}
				);
		assertThat (e).hasMessageContaining("IP doesn't exist");
	}
	
	@Test
	void releaseIP_areadyReleased() {
		// IP already released -> throw exception
		when(repo.getAddress("abc")).thenReturn(Optional.of(new IPAddress("abc", true)));
		Exception e = Assertions.assertThrows(Exception.class, 
				()->{service.releaseIPAddress("abc");}
				);
		assertThat (e).hasMessageContaining("IP already released");
	}
	
	@Test
	void releaseIP_success() throws Exception {
		// IP released successfully 
		when(repo.getAddress("abc")).thenReturn(Optional.of(new IPAddress("abc", false)));
		assertThat(service.releaseIPAddress("abc")).contains("Released IP successfully");
	}
	
	@Test
	void genIP_successCidr32() {
		Map<String, String> m = new HashMap<>();
		m.put("cidr", "255.125.125.255/32");
		service.createIPAddresses(m);
		
		List<IPAddress> res = new ArrayList<>();
		res.add(new IPAddress("255.125.125.255", true));
		Mockito.verify(repo).saveAll(res);
	}
	
	@Test
	void genIP_successCidr29() {
		Map<String, String> m = new HashMap<>();
		m.put("cidr", "255.125.125.255/29");
		service.createIPAddresses(m);
		
		List<IPAddress> res = new ArrayList<>();
		res.add(new IPAddress("255.125.125.248", true));
		res.add(new IPAddress("255.125.125.249", true));
		res.add(new IPAddress("255.125.125.250", true));
		res.add(new IPAddress("255.125.125.251", true));
		res.add(new IPAddress("255.125.125.252", true));
		res.add(new IPAddress("255.125.125.253", true));
		res.add(new IPAddress("255.125.125.254", true));
		res.add(new IPAddress("255.125.125.255", true));
		Mockito.verify(repo).saveAll(res);
	}

}
