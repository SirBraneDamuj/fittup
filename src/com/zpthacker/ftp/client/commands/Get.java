package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;
import com.zpthacker.ftp.client.util.FileUtils;

public class Get extends Command {
	
	private String path;
	
	public Get(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		String response = c.retr(this.path);
		if(response == null) {
			this.failureMessage = "Error retrieving file";
			return false;			
		} else {
			this.successMessage = "Successfully retrieved file.";
			FileUtils.writeFile(this.path, response);
			return true;
		}
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length != 2) {
			this.failureMessage = "Invalid syntax";
			return;
		}
		this.path = tokens[1];
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("get - download file");
		println("USAGE: get PATH");
		println("File downloads directly to a local file of the same name.");

	}

}
