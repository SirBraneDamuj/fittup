package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.zpthacker.ftp.client.util.Logger;

public class Client {
	String hostname;
	int port;
	Socket clientSocket;
	PrintStream out;
	BufferedReader in;
	boolean passive;
	Socket pasvDataSocket;
	Socket portDataSocket;
	ServerSocket ssock;
	
	public Client(String hostname, int port) throws IOException {
		this.hostname = hostname;
		this.port = port;
		this.clientSocket = new Socket(hostname, port);
		this.out = new PrintStream(this.clientSocket.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
	}
	
	public void setPassiveOff(int port) {
		this.passive = false;
		this.ssock = this.getPortServerSocket(port);
	}
	
	public void setPassiveOn(String ip, int port) {
		this.passive = true;
		this.pasvDataSocket = this.getPassiveSocket(ip, port);
	}
	
	public boolean transferReady() {
		return this.pasvDataSocket != null || this.passive == false;
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
	
	public String list(String argument) {
		String command = "list";
		if(argument != null) {
			command += " " + argument;
		}
		if(this.passive) {
			return this.passiveList(command);
		} else {
			return this.portList(command);
		}
	}
	
	private String passiveList(String command) {
		String response = this.writeCommand(command);
		return response;
	}
	
	private String portList(String command) {
		String response = this.writeCommand(command);
		this.portDataSocket = this.getPortDataSocket();
		try {
			this.portDataSocket.close();
		} catch(IOException e) {
			/* swallow */
		}
		return response;
	}
	
	public String pasv() {
		return this.writeCommand("pasv");
	}
	
	public String port(int port) {
		String portString = "," + Integer.toString(port/256) + ",";
		portString += port % 256;
		String argString = this.clientSocket.getLocalAddress().toString().substring(1).replaceAll("\\.", ",") + portString;
		return this.writeCommand("port " + argString);
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
		if(this.ssock != null) {
			this.ssock.close();
		}
		if(this.portDataSocket != null) {
			this.portDataSocket.close();
		}
		if(this.pasvDataSocket != null) {
			this.pasvDataSocket.close();
		}
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
	
	public Socket getPassiveSocket(String ip, int port) {
		try {
			return new Socket(ip, port);
		} catch(IOException e) {
			println("Unable to establish data connection");
			return null;
		}
	}
	
	public ServerSocket getPortServerSocket(int port) {
		try {
			return new ServerSocket(port);
		} catch(IOException e) {
			println("Unable to establish data connection");
			return null;
		}
	}
	
	public Socket getPortDataSocket() {
		try {
			return this.ssock.accept();
		} catch(IOException e) {
			println("Unable to establish data connection");
			return null;
		}
	}
	
	public String getInfo() {
		return this.hostname + ":" + port;
	}
}
