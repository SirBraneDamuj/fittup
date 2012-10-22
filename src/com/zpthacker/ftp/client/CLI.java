/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * CLI.java
 * The CLI for the client. Acts as a factory for Command objects
 */

package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.print;
import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import static com.zpthacker.ftp.client.util.ConsoleUtils.readLine;

import com.zpthacker.ftp.client.commands.Cd;
import com.zpthacker.ftp.client.commands.Cdup;
import com.zpthacker.ftp.client.commands.Get;
import com.zpthacker.ftp.client.commands.Help;
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
	
	/*
	 * simple input loop, waits for user input and then parses and handles it
	 */
	private void commandLoop() {
		while(true) {
			print(this.getPrompt());
			String line = readLine();
			if(line.equals("")) { //user just hit enter
				continue;
			}
			Command command = createCommand(line);
			if(command == null) { //user entered "quit"
				return;
			} else if(command.isValid() && command.execute(this.client)) {
				//the command had valid syntax and executed successfully
				command.printSuccessMessage();
			} else {
				//the command was invalid or failed during execution
				command.printFailureMessage();
			}
		}
	}
	
	/*
	 * creates a command object based on the first token of the user's command
	 */
	public static Command createCommand(String command) {
		String[] tokens = command.split(" ");
		switch(tokens[0].toLowerCase()) {
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
			case "help":
				return new Help(tokens);
			default:
				return new Unknown(tokens);
		}
	}
	
	private String getPrompt() {
		return "[" + this.client.getInfo() + "] ftp (" + this.client.pwd() + ")> ";
	}
}
