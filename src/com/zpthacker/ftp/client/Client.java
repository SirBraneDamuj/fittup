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
	boolean passive = true;
	DataConnection conn;
	int dataPort;
	
	public Client(String hostname, int port) throws IOException {
		this.hostname = hostname;
		this.port = port;
		this.clientSocket = new Socket(hostname, port);
		this.out = new PrintStream(this.clientSocket.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
	}
	
	public void setPassiveOff(int port) {
		this.passive = false;
		this.dataPort = port;
	}
	
	public void setPassiveOn() {
		this.passive = true;
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
	
	private void createConnection() {
		if(this.conn != null) {
			this.conn.close();
		}
		if(this.passive) {
			this.pasv();
		} else {
			this.port();
		}
	}
	
	public String list(String argument) {
		this.createConnection();
		String command = "list";
		if(argument != null) {
			command += " " + argument;
		}
		return this.getData(command);
	}
	
	public String retr(String path) {
		if(path == null) {
			return null;
		}
		this.createConnection();
		String command = "retr " + path;
		return this.getData(command);
	}
	
	private String getData(String command) {
		Socket socket = null;
		if(this.passive) {
			socket = this.conn.getSocket();
		}
		String response = this.writeCommand(command);
		if(!this.passive) {
			socket = this.conn.getSocket();
		}
		if(response.indexOf("150") != -1) {
			String data = this.readDataSocket(socket);
			try {
				response = this.readLine();
				if(response.indexOf("226") == -1) {
					return null;
				} else {
					return data;
				}
			} catch(IOException e) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public void pasv() {
		String response = this.writeCommand("pasv");
		this.conn = new PassiveConnection(response);
	}
	
	public void port() {
		String portString = "," + Integer.toString(this.dataPort/256) + ",";
		portString += this.dataPort % 256;
		String argString = this.clientSocket.getLocalAddress().toString().substring(1).replaceAll("\\.", ",") + portString;
		this.writeCommand("port " + argString);
		this.conn = new PortConnection(this.dataPort);
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
		this.conn.close();
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
	
	public String readDataSocket(Socket socket) {
		try {
			BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String data = "";
			while(true) {
				String buf = dataIn.readLine();
				if(buf == null) {
					break;
				}
				data += buf + "\n";
			}
			dataIn.close();
			return data;
		} catch(IOException e) {
			return null;
		}
	}
	
	public String getInfo() {
		return this.hostname + ":" + port;
	}
}
