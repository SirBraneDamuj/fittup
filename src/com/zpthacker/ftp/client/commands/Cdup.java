/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Cdup.java
 * Represents the cdup command
 * Changes to parent directory
 * Extends Command
 */

package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Cdup extends Command {
		
	public Cdup(String[] tokens) {
		super(tokens);
	}

	public Cdup() {
		super();
	}

	@Override
	public boolean execute(Client c) {
		String response = c.cdup();
		//if the response code isn't expected
		if(response == null || (response.indexOf("200") == -1 && response.indexOf("250") == -1)) {
			this.handleError(response);
			return false;
		} else {
			return true;
		}
	}
	
	private void handleError(String response) {
		this.failureMessage = "Server rejected CDUP command";
	}

	@Override
	public void printSuccessMessage() {
		//don't show a success message for this
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
	public void usage() {
		this.oneLineSummary();
		println("USAGE: cdup (no arguments");
	}

	@Override
	public void oneLineSummary() {
		println("cdup - change to parent directory");		
	}

}
