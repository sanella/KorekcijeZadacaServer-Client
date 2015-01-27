package ba.bitcamp.rqg.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;


public class User {
	//Atribut:
	
	// Upisite adresu file-a na kojoj zelite da se kreira i ispisuju citati
		public static String rQuotesFile = "C:\\Users\\Sanela\\Desktop\\receivedQuotes.txt";
	
	//port preko kojes se spjaju User i server
	public static final int port = 1717;
	
	
	/**
	 * metoda za konektovanje Usera na server
	 * @throws IOException
	 */
	public static void connect() throws IOException {
		Socket user = null;
		//Kad se pokrene User, mora prvo da unese IP
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter ip:");
		String ipAddress = scan.nextLine();
		
		// file u koji cemo upisati citate koje klijent dobije
		File rQuotes = new File(rQuotesFile);
		
		try {
			// sad imam Ip i port, pa provjeravam Pass
			user = new Socket(ipAddress, port);
			
			OutputStream os = user.getOutputStream();
			
			System.out.println("Please enter the password.");
			String password = scan.nextLine();
			os.write(password.getBytes());	
			
			user.shutdownOutput();
			
			// ukoliko je uni tacan pasvord onda se ispisuje citat
			InputStream in = user.getInputStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			
			// poruka koju mi je server poslao
			String message = br.readLine();
			System.out.println(message);
			
			// Poruka, tj citat koji se ispise na consoli
			String quote = br.readLine();
			System.out.println(quote);
			
			//Dio u kojem upisujem u file citat koji sam dobila
			FileWriter fw = new FileWriter(rQuotes, true);
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(new Date()).append("  ").append("]").append(quote);
			fw.write(sb.toString() + "\n"+ "\n");
			fw.flush();
			fw.close();
			
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			scan.close();
			user.close();
		}
	}
	
	/**
	 * Main metoda u kojoj moram pozvati konekciju da bi radio program
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			connect();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

}