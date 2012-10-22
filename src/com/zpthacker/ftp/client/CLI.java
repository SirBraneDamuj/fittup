package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.print;
import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import static com.zpthacker.ftp.client.util.ConsoleUtils.readLine;

import com.zpthacker.ftp.client.commands.Cd;
import com.zpthacker.ftp.client.commands.Cdup;
import com.zpthacker.ftp.client.commands.Get;
import com.zpthacker.ftp.client.commands.Login;
import com.zpthacker.ftp.client.commands.Ls;
import com.zpthacker.ftp.client.commands.Mkdir;
import com.zpthacker.ftp.client.commands.Passive;
import com.zpthacker.ftp.client.commands.Pwd;
import com.zpthacker.ftp.client.commands.Unknown;

public class CLI {
	
	private Client client;
	
	public CLI(Client c) {
		this.client = c;
	}
	
	public void start() {
		println("Successfully connected to " + this.client.getInfo());
		this.commandLoop();
	}
	
	private void commandLoop() {
		while(true) {
			print(this.getPrompt());
			String line = readLine();
			Command command = createCommand(line);
			if(command == null) {
				return;
			} else if(command.isValid() && command.execute(this.client)) {
				command.printSuccessMessage();
			}
		}
	}
	
	private Command createCommand(String command) {
		String[] tokens = command.split(" ");
		switch(tokens[0].toLowerCase()) {
			case "login":
				return new Login(tokens);
			case "pwd":
				return new Pwd(tokens);
			case "mkdir":
				return new Mkdir(tokens);
			case "cd":
				return new Cd(tokens);
			case "cdup":
				return new Cdup(tokens);
			case "passive":
				return new Passive(tokens);
			case "ls":
				return new Ls(tokens);
			case "get":
				return new Get(tokens);
			case "quit":
				return null;
			default:
				return new Unknown(tokens);
		}
	}
	
	private String getPrompt() {
		return "[" + this.client.getInfo() + "] ftp> ";
	}
}
