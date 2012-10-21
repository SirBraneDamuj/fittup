package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import java.io.IOException;

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
			CLI cli = new CLI(c);
			cli.start();
			Logger.close();
		} catch(Exception e) {
			Logger.close();
			throw e;
		}
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
