/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Passive.java
 * Represents the passive command.
 * Allows the user to switch between passive and port modes
 */

package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Passive extends Command {
	
	private boolean isPassive;
	private int port;
	
	public Passive(String[] tokens) {
		super(tokens);
	}

	public Passive() {
		super();
	}

	@Override
	public boolean execute(Client c) {
		if(this.isPassive) {
			c.setPassiveOn();
			this.successMessage = "Passive mode enabled";
		} else {
			c.setPassiveOff(this.port);
			this.successMessage = "Passive mode disabled. Port set to " + this.port;
		}
		return true;
	}

	//this one's a bit bumpy compared to the other commands
	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length < 2) { //if the command has fewer than two tokens
			this.failureMessage = "Invalid syntax";
			return;
		} else if(tokens[1].toLowerCase().equals("on")) { //if the second token is "on"
			if(tokens.length != 2) { //and the number of tokens is not 2
				this.failureMessage = "Invalid syntax";
				return;
			}
			this.isPassive = true;
		} else if(tokens[1].toLowerCase().equals("off")) { //if the second token is "off"
			if(tokens.length > 3) { //and the number of tokens is greater than 3
				this.failureMessage = "Invalid syntax";
				return;
			} else if(tokens.length == 3) { //or the number of tokens is equal to 3
				this.port = Integer.parseInt(tokens[2]);
			} else { //number of tokens is 2
				this.port = 9000;
			}
			this.isPassive = false;
		} else { //second token is neither "on" nor "off"
			this.failureMessage = "Invalid syntax";
			return;
		}
		this.valid = true;
	}

	@Override
	public void usage() {
		this.oneLineSummary();
		println("USAGE: passive (on)|(off [PORT=9000])");
		println("passive on needs no more arguments");
		println("passive off takes an optional port, otherwise it uses 9000");
	}

	@Override
	public void oneLineSummary() {
		println("passive - toggle passive on or off");
	}

}
