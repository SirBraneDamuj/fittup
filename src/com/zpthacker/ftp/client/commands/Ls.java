package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Ls extends Command {
	
	private String listArgument;
	
	public Ls(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		boolean response = c.list(this.listArgument);
		if(response) {
			return true;
		} else {
			this.failureMessage = "Error retrieving contents";
			return false;
		}
	}

	@Override
	public void printSuccessMessage() {
		/* no op - the ls command already output the info */
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length < 1 || tokens.length > 2) {
			this.failureMessage = "Invalid syntax";
			return;
		}
		if(tokens.length == 2) {
			this.listArgument = tokens[1];
		} else {
			this.listArgument = null;
		}
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("ls - list contents");
		println("USAGE: ls [FILENAME]");
		println("Lists contents of current working directory");
		println("Lists information about FILENAME if provided");

	}

}
