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
		if(c.transferReady()) {
			String response = c.list(this.listArgument);
			println(response);
			return true;
		} else {
			println("Data transfer not ready. Call passive on/off first.");
			return false;
		}
	}

	@Override
	public void printSuccessMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length < 1 || tokens.length > 2) {
			usage();
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
		// TODO Auto-generated method stub

	}

}
