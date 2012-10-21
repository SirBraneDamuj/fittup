package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Unknown extends Command {
	
	public Unknown(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		return true;
	}

	@Override
	public void printSuccessMessage() {
		println("Unknown command: " + this.command);
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
