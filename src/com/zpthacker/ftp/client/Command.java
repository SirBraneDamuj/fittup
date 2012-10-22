package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

public abstract class Command {
	
	protected String command;
	protected boolean valid;
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
	
	public abstract boolean execute(Client c);
	public void printSuccessMessage() {
		println(this.successMessage);
	}
	public void printFailureMessage() {
		println(this.failureMessage);
		usage();
	}
	protected abstract void interpretTokens(String[] tokens);
	protected abstract void usage();
}
