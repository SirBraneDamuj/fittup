/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Get.java
 * Represents the Get command
 * Retrieves a file specified by the argument
 * Extends Command
 */

package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Get extends Command {
	
	private String path;
	
	public Get(String[] tokens) {
		super(tokens);
	}

	public Get() {
		super();
	}

	@Override
	public boolean execute(Client c) {
		boolean response = c.retr(this.path);
		if(!response) {
			this.failureMessage = "Error retrieving file";
			return false;			
		} else {
			this.successMessage = "Successfully retrieved file.";
			return true;
		}
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 2) {
			this.failureMessage = "Invalid syntax";
			return;
		}
		this.path = tokens[1];
		this.valid = true;
	}

	@Override
	public void usage() {
		this.oneLineSummary();
		println("USAGE: get PATH");
		println("File downloads directly to a local file of the same name.");

	}

	@Override
	public void oneLineSummary() {
		println("get - download file");
	}

}
