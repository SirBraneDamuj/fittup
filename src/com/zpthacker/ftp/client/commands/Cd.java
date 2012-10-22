/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Cd.java
 * Represents the cd command
 * Extends Command
 */

package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Cd extends Command {
	
	//the directory that the user is switching to
	private String targetDirectory;
	
	public Cd(String[] tokens) {
		super(tokens);
	}

	public Cd() {
		super();
	}

	@Override
	public boolean execute(Client c) {
		String response = c.cwd(this.targetDirectory);
		//if we don't receive an expected response code
		if(response == null || (response.indexOf("250") == -1 && response.indexOf("200") == -1)) {
			this.handleError(response);
			return false;
		} else {
			this.successMessage = "Changed directory to \"" + this.targetDirectory + "\"";
			return true;
		}
	}
	
	private void handleError(String response) {
		if(response.indexOf("550") != -1) {
			this.failureMessage = "Error: Directory does not exist.";
		} else {
			this.failureMessage = "Unknown error";
		}
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 2) {
			this.failureMessage = "Invalid syntax";
			return;
		}
		this.targetDirectory = tokens[1];
		this.valid = true;
	}

	@Override
	public void usage() {
		this.oneLineSummary();
		println("USAGE: cd DIRECTORY_NAME");
		println("DIRECTORY_NAME can be either a relative or absolute path");
	}

	@Override
	public void oneLineSummary() {
		println("cd - change working directory");
	}

}
