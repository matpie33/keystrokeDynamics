package pl.master.thesis.others;

import java.util.Random;

public class PasswordMasker {

	public static String maskPassword() {
		Random r = new Random();
		int size = r.nextInt(20);
		String result = "";
		for (int i = 0; i < size; i++) {
			result += "*";
		}
		return result;
	}

}
