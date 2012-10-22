/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Pwd.java
 * represents pwd
 * print working directory
 * straightforward enough, right?
 */

package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Pwd extends Command {
	
	public Pwd(String[] tokens) {
		super(tokens);
	}

	public Pwd() {
		super();
	}

	@Override
	public boolean execute(Client c) {
		String response = c.pwd();
		if(response == null) {
			this.failureMessage = "PWD failed...something's really messed up here.";
			return false;
		}
		this.successMessage = response;
		return true;
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
		println("USAGE: pwd (no arguments)");
	}

	@Override
	public void oneLineSummary() {
		println("pwd - print working directory");
	}

}
