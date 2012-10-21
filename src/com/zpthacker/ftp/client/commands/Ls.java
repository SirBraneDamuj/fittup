package com.zpthacker.ftp.client.commands;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Ls extends Command {
	
	private String listArgument;
	
	public Ls(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		// TODO Auto-generated method stub
		return false;
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
		}
		this.valid = true;
	}

	@Override
	protected void usage() {
		// TODO Auto-generated method stub

	}

}
