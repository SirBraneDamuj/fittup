/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * ConsoleUtils.java
 * Set of static functions to aid in console operations
 */

package com.zpthacker.ftp.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUtils {
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	//Lets me avoid writing System.out.print
	public static void print(String str) {
		System.out.print(str);
	}
	
	//Lets me avoid writing System.out.println
	public static void println(String str) {
		System.out.println(str);
	}
	
	//prints a prompt, then takes a line of input
	public static String prompt(String prompt) {
		print(prompt);
		return readLine();
	}
	
	public static String readLine() {
		String retval = null;
		try {
			retval = in.readLine();
		} catch(IOException e) {
			println("Error reading console input");
			System.exit(1);
		}
		return retval;
	}
}
