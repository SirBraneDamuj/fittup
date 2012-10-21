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
		
	@Override
	public void printSuccessMessage() {
		println(this.successMessage);
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length < 2) { //if the command has fewer than two tokens
			usage();
			return;
		} else if(tokens[1].toLowerCase().equals("on")) { //if the second token is "on"
			if(tokens.length != 2) { //and the number of tokens is not 2
				usage();
				return;
			}
			this.isPassive = true;
		} else if(tokens[1].toLowerCase().equals("off")) { //if the second token is "off"
			if(tokens.length > 3) { //and the number of tokens is greater than 3
				usage();
				return;
			} else if(tokens.length == 3) { //and the number of tokens is equal to 3
				this.port = Integer.parseInt(tokens[2]);
			} else { //number of tokens is 2
				this.port = 9000;
			}
			this.isPassive = false;
		} else { //invalid command
			usage();
			return;
		}
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("passive - toggle passive on or off");
		println("USAGE: passive (on)|(off [PORT=9000])");
		println("passive on needs no more arguments");
		println("passive off takes an optional port, otherwise it uses 9000");
	}

}
