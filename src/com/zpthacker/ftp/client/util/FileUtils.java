package com.zpthacker.ftp.client.util;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	public static void writeFile(String path, String content) {
		String filename = (new File(path)).getName();
		BufferedWriter out = null;
		try {
		out = new BufferedWriter(new FileWriter(filename));
		out.write(content, 0, content.length());
		out.close();
		} catch(IOException e) {
			println("Error writing file: " + filename);
		}
	}
	
	public static FileOutputStream getFileOutputStream(String path) throws IOException {
		String filename = (new File(path)).getName();
		return new FileOutputStream(filename);
	}
}
