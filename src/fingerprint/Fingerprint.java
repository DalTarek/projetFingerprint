package fingerprint;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		int tab[] = new int[n];
		tab[0] = 1 % p;
		for (int i=1; i<n; i++) {
			tab[i] = (tab[i-1] * 256) % p;
		}
		
		int octet;
		while ((octet = file.read()) != -1) {			
			finger += ((octet % p) * tab[n-1]) % p;
			n--;				
		}
		file.close();
		
	} catch(IOException e) {
		e.printStackTrace();
	}
	
	return finger % p;
}
	
	public static void main(String[] args) {
		int premier = nextprime();
		int finger = fingerprint(premier,"test2");
		System.out.println(finger);
	}
}
