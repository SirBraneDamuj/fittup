package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Mkdir extends Command {

	private String directoryArg;
	
	public Mkdir(String[] tokens) {
		super(tokens);
	}
	
	@Override
	public boolean execute(Client c) {
		String response = c.mkd(this.directoryArg);
		if(response == null || response.indexOf("257") == -1) {
			this.handleMkdError(response);
			return false;
		} else {
			this.successMessage = "Directory " + response.substring(4);
			return true;
		}
	}
	
	private void handleMkdError(String response) {
		println("Unable to create directory, it may already exist.");
		usage();
	}

	@Override
	public void printSuccessMessage() {
		println(this.successMessage);
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 2) {
			usage();
			return;
		}
		this.directoryArg = tokens[1];
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("mkdir - create a new directory");
		println("USAGE: mkdir DIRECTORY_NAME");
		println("If you provide a relative path, it is created inside the current working directory.");
		println("If you provide an absolute path, it is created at that absolute path.");
	}

}
