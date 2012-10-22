package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import static com.zpthacker.ftp.client.util.ConsoleUtils.prompt;

import java.io.IOException;

import com.zpthacker.ftp.client.commands.Login;
import com.zpthacker.ftp.client.util.Logger;

public class Main {
	public static void main(String[] args) {
		try {
			if(args.length < 2 || args.length > 3) {
				usageAndExit();
			}
			if(!Logger.initWithFilename(args[1])) {
				usageAndExit("Error creating logger, make sure your file is correct");
			}
			String hostname = args[0];
			int port = args.length == 3 ? Integer.parseInt(args[2]) : 21;
			Client c = null;
			try {
				c = new Client(hostname, port);
				String line = c.readLine();
				while(line.indexOf("220 ") == -1) {
					line = c.readLine();
				}
			} catch(IOException e) {
				usageAndExit("Error creating connection. Verify hostname and port and try again.");
			}
			if(login(c)) {
				CLI cli = new CLI(c);
				cli.start();
			} else {
				println("Login failed. Aborting...");
			}
			Logger.close();
		} catch(Exception e) {
			throw e;
		} finally {
			Logger.close();
		}
	}
	
	private static boolean login(Client c) {
		String username = prompt("Username: ");
		String password = prompt("Password: ");
		String command = "login " + username + " " + password;
		Login l = new Login(command.split(" "));
		return l.execute(c);
	}
	
	private static void usageAndExit(String errorMessage) {
		println(errorMessage);
		usageAndExit();
	}
	
	private static void usageAndExit() {
		println("Zach's super rad FTP client");
		println("java com.zpthacker.ftp.client.Main HOSTNAME LOG_FILENAME [PORT_NUMBER]");
		println("HOSTNAME is the name or IP of the server's machine");
		println("LOG_FILENAME is the name of the file where all requests/responses are logged");
		println("PORT_NUMBER is the server's listening port. Defaults to 21");
		System.exit(1);
	}
}
