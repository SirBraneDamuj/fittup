package com.zpthacker.ftp.client;

public abstract class Command {
	
	protected String command;
	protected boolean valid;
	protected String successMessage;
	
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
	public abstract void printSuccessMessage();
	protected abstract void interpretTokens(String[] tokens);
	protected abstract void usage();
}
