package fingerprint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	/*public static boolean pseudoprime(int p) {
		if (Math.pow(2, p-1) % p == 1) {
			return true;
		}
		return false;
	}*/
	
	public static boolean pseudoprime(int p) {
		if (puissance(2, p-1, p) == 1) {
			return true;
		}
		return false;
	}
	
	public static int nextprime() {
		Random r = new Random();
		int valeur = 2 + r.nextInt((int)Math.pow(2, 23) - 2);
		while (!pseudoprime(valeur)) {
			valeur = 2 + r.nextInt((int)Math.pow(2, 23) - 2);
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
	
	public static int fingerprint(int p, String fn, int taille, int debut) {
		int finger = 0, cmp = 0;
		try {
			FileInputStream file = new FileInputStream(fn);
			int n = taille;
			int tab[] = new int[n];
			tab[0] = 1 % p;
			for (int i=1; i<n; i++) {
				tab[i] = (tab[i-1] * 256) % p;
			}
		
			int octet;
			for(int i = 0; i < debut; i++){
				octet = file.read();
			}
			while ((octet = file.read()) != -1 && cmp < taille) {	
				finger += ((octet % p) * tab[n-1]) % p;
				n--;		
				cmp++;
			}
			file.close();
		
		} catch(IOException e) {
			e.printStackTrace();
	}
	
	return finger % p;
	}
	
	//Temps d'exec tr�s long attention !!! 
	public static void testQ5(){
		int cmp = 0, premier, finger1, finger2;
		for(int i = 0; i < 1000; i++){
			System.out.println(i);
			premier = nextprime();
			finger1 = fingerprint(premier,"test1");
			finger2 = fingerprint(premier,"test4");
			if(finger1 == finger2){
				cmp++;
			}
		}
		System.out.println("1000 tests sur 2 fichers test1 et test4 :");
		System.out.println("% fingerprint identique : " + (cmp / 1000 * 100));
	}
	
	public static void testQ6(){
		int premier, cmp = 0, finger1, finger2;
		for(int i = 0; i < 100; i++){
			System.out.println(i);
			premier = nextprime();
			finger1 = fingerprint(premier,"test3.xpm");
			finger2 = fingerprint(premier,"test4");
			System.out.println("premier : " + premier + ", fingers :" + finger1 + ", " + finger2);
			if(finger1 == finger2){
				cmp++;
			}
		}
		System.out.println("cmp : " + cmp);
	}
	
	public static void testQ7(){
		System.out.println(fingerprint(17,"test2"));
		System.out.println(fingerprint(59,"test2"));
		System.out.println(fingerprint(521,"test2"));
		System.out.println(fingerprint(997,"test2"));
		System.out.println(fingerprint(2399,"test2"));
	}
	
	public static boolean testContenu(String fn1, String fn2){
		boolean trouve = false;
		FileInputStream file1, file2;
		int n1, n2, premier = nextprime(), finger1, finger2, cmp;
		try {
			file1 = new FileInputStream(fn1);
			file2 = new FileInputStream(fn2);
			n1 = file1.available();
			n2 = file2.available();
			if(n1 > n2){
				cmp = 0;
				finger2 = fingerprint(premier,fn2);
				while(cmp + n2 < n1 + 1 && !trouve){
					finger1 = fingerprint(premier, fn1, n2, cmp);
					if(finger1 == finger2){
						trouve = true;
					}
					cmp = cmp + 1;
				}
			}else{
				cmp = 0;
				finger1 = fingerprint(premier,fn1);
				while(cmp + n1 < n2 + 1 && !trouve){
					finger2 = fingerprint(premier, fn2, n1, cmp);
					if(finger1 == finger2){
						trouve = true;
					}
					cmp = cmp + 1;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trouve;
	}
	
	public static void main(String[] args) {
		//testQ5();
		//testQ6();
		//testQ7();
		
		//Test 2 fichiers identiques :
		/*int premier = nextprime();
		int finger1 = fingerprint(premier,"test1");
		int finger2 = fingerprint(premier,"test2");
		if(finger1 == finger2){
			System.out.println("true");
		}else{
			System.out.println("false");
		}*/
		
		//Test si un des fichiers contient l'autre
		System.out.println(testContenu("test6.txt", "test7.txt"));
		System.out.println(testContenu("test6.txt", "test8.txt"));
		System.out.println(testContenu("test8.txt", "test7.txt"));
	}
}
