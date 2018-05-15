package strategy;

import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Utils {
	protected static Scanner cmdScanner = new Scanner(System.in);
	public static String loadResource (String path) throws IOException {
		// loads given file as string
		String file = "";
		FileReader inputStream = new FileReader(path);
		int c = inputStream.read();
		//System.out.println("Starting to read file");
		while (c != -1) {
			file += (char) c;
			c = inputStream.read();
			//System.out.println(c);
		}
		//System.out.println("Finished reading file");
		if (inputStream != null) {
			inputStream.close();
		}
		return file;
	}
	public static int inputInt () throws InputMismatchException{
		int n = 0;
		n = cmdScanner.nextInt();
		return n;
	}
}
