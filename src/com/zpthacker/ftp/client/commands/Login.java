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
		if(this.sendUserRequest(c) && this.sendPassRequest(c)) {
			this.successMessage = "Login successful";
			return true;
		} else {
			return false;
		}
	}
	
	private boolean sendUserRequest(Client c) {
		String response = c.user(this.username);
		if(response == null || response.indexOf("331") == -1) {
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
	protected void usage() {
		println("login - login to the server using a username and password");
		println("USAGE: login USERNAME PASSWORD");
	}

}
