/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Command.java
 * Abstract command object. All commands extend this class.
 */

package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

public abstract class Command {
	
	protected String command; //the verb
	protected boolean valid; //is this command valid?
	protected String successMessage;
	protected String failureMessage;
	
	public Command() {
		this.valid = false;
	}
	
	public Command(String[] tokens) {
		this();
		this.command = tokens[0];
		this.interpretTokens(tokens);
	}
	
	public boolean isValid() {
		return this.valid;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	//performs some action with the client and returns true or false
	public abstract boolean execute(Client c);
	public void printSuccessMessage() {
		println(this.successMessage);
	}
	public void printFailureMessage() {
		println(this.failureMessage);
		usage();
	}
	//parses the tokens and fills out the fields for this command
	protected abstract void interpretTokens(String[] tokens);
	//prints some handy information about this command
	public abstract void usage();
	//used by help to give a summary of all commands
	public abstract void oneLineSummary();
}
