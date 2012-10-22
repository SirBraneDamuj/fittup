package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Cdup extends Command {
		
	public Cdup(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		String response = c.cdup();
		if(response == null || (response.indexOf("200") == -1 && response.indexOf("250") == -1)) {
			this.handleError(response);
			return false;
		} else {
			return true;
		}
	}
	
	private void handleError(String response) {
		println("Error with cdup - most likely already in the root directory");
	}

	@Override
	public void printSuccessMessage() {
		//don't show a success message for this
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 1) {
			usage();
			return;
		}
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("cdup - change to parent directory");
		println("USAGE: cdup (no arguments");
	}

}
