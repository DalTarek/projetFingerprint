package fingerprint;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Fingerprint {
	
	public static int multiply(int x, int y, int p) {
		int result = 0;
		while (y != 0) {
			if((y & 1) != 0)
				result = (result + x) % p;
			y >>= 1;
			x = (2*x) % p;
		}
		return result;
	}
	
	public static int puissance(int x, int k, int p) {
		int r = x;
		for (int i=0; i<k-1; i++) {
			r = multiply(r, x, p);
		}
		return r;
	}

	public static boolean pseudoprime(int p) {
		if (Math.pow(2, p-1) % p == 1) {
			return true;
		}
		return false;
	}
	
	public static int nextprime() {
		Random r = new Random();
		int valeur = 2 + r.nextInt((int)Math.pow(2, 23) - 3);
		while (!pseudoprime(valeur)) {
			valeur = 2 + r.nextInt((int)Math.pow(2, 23) - 3);
		}
		return valeur;
	}
	
	public static int fingerprint(int p, String fn) {
		int finger = 0;
		try {
			FileInputStream file = new FileInputStream(fn);
			int n = file.available();
			int octet;
			int t;
			int x;
			while ((octet = file.read()) != -1) {			
				//System.out.println(octet);
				//t = (int)(octet * (Math.pow(256, n)));
				//finger = (finger + (t % p)) % p;
				//x = puissance(256, n-1, p);
				x = (int)Math.pow(256, n-1);
				t = multiply(octet, x, p);
				finger = (finger + t)%p;			
				//System.out.println("m = " + finger);
				n--;				
			}
			file.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return finger;
	}
	
	public static void main(String[] args) {
		int finger = fingerprint(5407,"test1");
		System.out.println(finger);
	}
}
