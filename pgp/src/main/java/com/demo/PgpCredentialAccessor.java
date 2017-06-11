package com.demo;

import org.mule.api.MuleEvent;

public class PgpCredentialAccessor implements CredentialsAccessor{
	private String credentials;
	public PgpCredentialAccessor(String credentials){
		this.credentials = credentials;
	}
	
	public Object getCredentials(MuleEvent event){
		return this.credentials;
	}
	
	public void setCredentials(MuleEvent event, Object credentials){
		
		this.credentials = (String) credentials;
	}
}
