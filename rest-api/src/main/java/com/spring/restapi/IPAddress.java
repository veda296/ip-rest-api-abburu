package com.spring.restapi;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="IPAddresses")
public class IPAddress {
	@Id
	private String ipAddress;
	private boolean available;
	
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public IPAddress(String ipAddress, boolean available) {
		super();
		this.ipAddress = ipAddress;
		this.available = available;
	}

	public IPAddress(String ipAddress) {
		super();
		this.ipAddress = ipAddress;
		this.available = true;
	}
	
	public IPAddress() {
		
	}
	
	public String toString() {
		return "address: " + this.ipAddress + ", available: " + this.available + "\n";
	}
	
    @Override
    public boolean equals(Object o) { 
  
        if (!(o instanceof IPAddress)) { 
            return false; 
        } 
        IPAddress i = (IPAddress) o;           
        return ((i.ipAddress).equals( this.ipAddress) && (i.available == this.available)); 
    } 


	
}
