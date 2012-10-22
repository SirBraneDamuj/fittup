/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Mkdir.java
 * Represents the mkdir command
 * Creates a new directory
 */

package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Mkdir extends Command {

	private String directoryArg;
	
	public Mkdir(String[] tokens) {
		super(tokens);
	}
	
	@Override
	public boolean execute(Client c) {
		String response = c.mkd(this.directoryArg);
		if(response == null || response.indexOf("257") == -1) {
			this.handleError(response);
			return false;
		} else {
			this.successMessage = "Created directory " + directoryArg;
			return true;
		}
	}
	
	private void handleError(String response) {
		this.failureMessage = "Unable to create directory, it may already exist.";
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 2) {
			this.failureMessage = "Invalid syntax";
			return;
		}
		this.directoryArg = tokens[1];
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("mkdir - create a new directory");
		println("USAGE: mkdir DIRECTORY_NAME");
		println("If you provide a relative path, it is created inside the current working directory.");
		println("If you provide an absolute path, it is created at that absolute path.");
	}

}
