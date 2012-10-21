package com.zpthacker.ftp.client.commands;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zpthacker.ftp.client.Client;
import com.zpthacker.ftp.client.Command;

public class Passive extends Command {
	
	private boolean isPassive;
	private int port;
	
	public Passive(String[] tokens) {
		super(tokens);
	}

	@Override
	public boolean execute(Client c) {
		String response = null;
		boolean success = false;
		if(this.isPassive) {
			response = c.pasv();
			String ipInfo = this.extractIPAndPortInfoFromPasvResponse(response);
			String pasvIp = this.extractIPFromPasvString(ipInfo);
			int pasvPort = this.extractPortFromPasvString(ipInfo);
			success = response.indexOf("227") != -1 && pasvIp != null && pasvPort > 0 && pasvPort < 65535;
			if(success) {
				this.successMessage = "Passive mode enabled. IP: " + pasvIp + " Port: " + pasvPort;
				c.setPassiveOn(pasvIp, pasvPort);
			}
		} else {
			response = c.port(this.port);
			success = response.indexOf("200") != -1;
			if(success) {
				this.successMessage = "Passive mode disabled.";
				c.setPassiveOff(this.port);
			}
		}
		return success;
	}
	
	private String extractIPAndPortInfoFromPasvResponse(String response) {
		Pattern p = Pattern.compile("(\\d{1,3},){5}\\d{1,3}");
		Matcher m = p.matcher(response);
		if(m.find()) {
			return m.group();
		} else {
			return null;
		}
	}
	
	private String extractIPFromPasvString(String pasvString) {
		Pattern p = Pattern.compile("\\d{1,3}");
		Matcher m = p.matcher(pasvString);
		String ip = "";
		int count = 0;
		while(m.find()) {
			count++;
			if(count == 5) {
				break;
			}
			ip += m.group() + ".";
		}
		ip = ip.substring(0, ip.length()-1);
		return ip;
	}
	
	private int extractPortFromPasvString(String pasvString) {
		Pattern p = Pattern.compile("\\d{1,3}");
		Matcher m = p.matcher(pasvString);
		int count = 0;
		int port = 0;
		while(m.find()) {
			count++;
			if(count == 5) {
				port += Integer.parseInt(m.group()) * 256;
			} else if(count == 6) {
				port += Integer.parseInt(m.group());
			}
		}
		return port;
	}

	@Override
	public void printSuccessMessage() {
		println(this.successMessage);
	}

	@Override
	protected void interpretTokens(String[] tokens) {
		if(tokens.length < 2) { //if the command has fewer than two tokens
			usage();
			return;
		} else if(tokens[1].toLowerCase().equals("on")) { //if the second token is "on"
			if(tokens.length != 2) { //and the number of tokens is not 2
				usage();
				return;
			}
			this.isPassive = true;
		} else if(tokens[1].toLowerCase().equals("off")) { //if the second token is "off"
			if(tokens.length > 3) { //and the number of tokens is greater than 3
				usage();
				return;
			} else if(tokens.length == 3) { //and the number of tokens is equal to 3
				this.port = Integer.parseInt(tokens[2]);
			} else { //number of tokens is 2
				this.port = 20;
			}
			this.isPassive = false;
		} else { //invalid command
			usage();
			return;
		}
		this.valid = true;
	}

	@Override
	protected void usage() {
		println("passive - toggle passive on or off");
		println("USAGE: passive (on)|(off HOSTNAME PORT)");
		println("passive on needs no more arguments");
		println("passive off requires HOSTNAME and PORT arguments to follow");
	}

}
