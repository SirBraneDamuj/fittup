package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Pwd extends Command {
	
	public Pwd(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		String response = c.pwd();
		if(response.indexOf("257") != -1) {
			this.successMessage = response.substring(4); //remove status code from response to get pwd message
			return true;
		} else {
			this.failureMessage = "PWD failed...something's really messed up here.";
			return false;
		}
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 1) {
			this.failureMessage = "Invalid syntax";
			return;
		}
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("pwd - print working directory");
		println("USAGE: pwd (no arguments)");
	}

}
