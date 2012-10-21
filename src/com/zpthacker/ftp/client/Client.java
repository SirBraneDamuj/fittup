package com.zpthacker.ftp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.zpthacker.ftp.client.util.Logger;

public class Client {
	String hostname;
	int port;
	Socket clientSocket;
	PrintStream out;
	BufferedReader in;
	
	public Client(String hostname, int port) throws IOException {
		this.hostname = hostname;
		this.port = port;
		this.clientSocket = new Socket(hostname, port);
		this.out = new PrintStream(this.clientSocket.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
	}
	
	public String user(String user) {
		String command = "user " + user;
		return this.writeCommand(command);
	}
	
	public String pass(String pass) {
		String command = "pass " + pass;
		return this.writeCommand(command);
	}
	
	public String pwd() {
		return this.writeCommand("pwd");
	}
	
	public String mkd(String directoryArg) {
		String command = "mkd " + directoryArg;
		return this.writeCommand(command);
	}
	
	public String cwd(String directoryArg) {
		String command = "cwd " + directoryArg;
		return this.writeCommand(command);
	}
	
	public String cdup() {
		return this.writeCommand("cdup");
	}
	
	public String help() {
		return this.writeCommand("help");
	}
	
	//Writes a command to the server and returns the response
	//Hides IOException so we don't have try/catches errywhere
	public String writeCommand(String command) {
		String response = null;
		try {
			this.printLine(command);
			response = this.readLine();
		} catch(IOException e) {
			System.out.println("Error while executing command: " + command);
		}
		return response;
	}
	
	public void close() throws IOException {
		this.out.close();
		this.in.close();
		this.clientSocket.close();
	}
	
	public void printLine(String line) {
		this.out.println(line);
		Logger.request(line);
	}
	
	public String readLine() throws IOException {
		String line = in.readLine();
		Logger.response(line);
		return line;
	}
	
	public boolean standby() throws IOException {
		return in.ready();
	}
	
	public String getInfo() {
		return this.hostname + ":" + port;
	}
}
