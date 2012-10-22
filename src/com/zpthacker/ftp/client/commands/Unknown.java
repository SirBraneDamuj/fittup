/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Unknown.java
 * Represents an unknown command
 * It simply exists to spit out an error message
 */

package com.zpthacker.ftp.client.commands;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Unknown extends Command {
	
	private String attemptedCommand;
	
	public Unknown(String[] tokens) {
		super(tokens);
		this.command = "unknown";
	}
	
	public String getAttemptedCommand() {
		return this.attemptedCommand;
	}

	@Override
	public boolean execute(Client c) {
		this.successMessage = "Unknown command: " + this.attemptedCommand;
		return true;
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		this.attemptedCommand = tokens[0];
		this.valid = true;
	}

	@Override
	public void usage() {
		/* no op */
	}

	@Override
	public void oneLineSummary() {
		/* no op */
	}
}
