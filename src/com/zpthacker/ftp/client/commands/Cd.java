package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Cd extends Command {
	
	private String targetDirectory;
	
	public Cd(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		String response = c.cwd(this.targetDirectory);
		if(response == null || (response.indexOf("250") == -1 && response.indexOf("200") == -1)) {
			this.handleError(response);
			return false;
		} else {
			this.successMessage = "Changed directory to \"" + this.targetDirectory + "\"";
			return true;
		}
	}
	
	private void handleError(String response) {
		if(response.indexOf("550") != -1) {
			this.failureMessage = "Error: Directory does not exist.";
		} else {
			this.failureMessage = "Unknown error";
		}
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 2) {
			this.failureMessage = "Invalid syntax";
			return;
		}
		this.targetDirectory = tokens[1];
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("cd - change working directory");
		println("USAGE: cd DIRECTORY_NAME");
		println("DIRECTORY_NAME can be either a relative or absolute path");
	}

}
