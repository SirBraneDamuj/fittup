/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Help.java
 * Represents the help command
 * Displays usage info for commands
 */

package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.CLI;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Help extends Command {
	
	private String helpArg;
	
	public Help(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		if(this.helpArg != null) {
			//create a command using the supplied verb
			Command command = CLI.createCommand(this.helpArg);
			//if it's unknown, the command is invalid
			if(command.getCommand().equals("unknown")) {
				this.failureMessage = "Unknown command: " + this.helpArg;
				return false;
			} else {
				command.usage();
				return true;
			}
		} else {
			this.dumpHelpInfo();
			return true;
		}
	}
	
	//dumps one line summaries of each available command
	private void dumpHelpInfo() {
		println("Welcome to Zach's lousy FTP client");
		println("Available commands:");
		new Pwd().oneLineSummary();
		new Cd().oneLineSummary();
		new Cdup().oneLineSummary();
		new Ls().oneLineSummary();
		new Mkdir().oneLineSummary();
		new Passive().oneLineSummary();
		new Get().oneLineSummary();
		this.oneLineSummary();
		println("Use \"help COMMAND\" to get more info about that command.");
	}
	
	@Override
	public void printSuccessMessage() {
		/* no op */
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length > 2 || tokens.length < 1) {
			this.failureMessage = "Invalid syntax";
		}
		if(tokens.length == 2) {
			this.helpArg = tokens[1];
		} else {
			this.helpArg = null;
		}
		this.valid = true;
	}

	@Override
	public void usage() {
		this.oneLineSummary();
		println("USAGE: help [COMMAND]");
		println("With no arguments, help displays a list of all available commands");
		println("With an argument, help displays the usage info for that command.");
	}

	@Override
	public void oneLineSummary() {
		println("help - get some helpful info");
	}

}
