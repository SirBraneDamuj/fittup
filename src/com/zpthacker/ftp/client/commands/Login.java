/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Login.java
 * Represents the login command
 * No longer available in the CLI, but is still used to login initially.
 */

package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.*;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Login extends Command {
	
	private String username;
	private String password;
	
	public Login(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		//send a USER request, then follow up with a PASS request
		if(this.sendUserRequest(c) && this.sendPassRequest(c)) {
			this.successMessage = "Login successful";
			return true;
		} else {
			return false;
		}
	}
	
	private boolean sendUserRequest(Client c) {
		String response = c.user(this.username);
		//unexpected response code
		if(response == null || (response.indexOf("331") == -1 && response.indexOf("230") == -1)) {
			this.handleUserError(response);
			return false;
		}
		return true;
	}
	
	private void handleUserError(String response) {
		this.failureMessage = "Username was not accepted.";
	}
	
	private boolean sendPassRequest(Client c) {
		String response = c.pass(this.password);
		if(response == null || (response.indexOf("230") == -1 && response.indexOf("202") == -1)) {
			this.handlePassError(response);
			return false;
		}
		return true;
	}
	
	private void handlePassError(String response) {
		this.failureMessage = "Invalid login information";
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 3) {
			this.failureMessage = "Invalid syntax";
			return;
		}
		this.username = tokens[1];
		this.password = tokens[2];
		this.valid = true;
	}
	
	@Override
	public void usage() {
		this.oneLineSummary();
		println("USAGE: login USERNAME PASSWORD");
	}

	@Override
	public void oneLineSummary() {
		println("login - login to the server using a username and password");		
	}

}
