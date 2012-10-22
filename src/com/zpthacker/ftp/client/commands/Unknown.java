package com.zpthacker.ftp.client.commands;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Unknown extends Command {
	
	public Unknown(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		this.successMessage = "Unknown command: " + this.command;
		return true;
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		this.valid = true;
	}

	@Override
	protected void usage() {
		/* no op */
	}
}
