package ba.bitcamp.rqg.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Generator {

	// Atributi:
	
	// Upisite svoju adresu na kojoj se nalazi file sa stihovima
	public static String quotesFile = "C:\\Users\\Sanela\\Desktop\\quotes.txt"; 
	//upisite adresu na kojoj zelite da se ispisuje file sa podacaima o login-u
	public static String authFile = "C:\\Users\\Sanela\\Desktop\\auth_log.txt";
	
	// pasvord koji se ocekuje da User treba da unese
	private static final String pass = "kamikaza";
	// port preko kojeg ces se posjiti klijent i server
	public static final int port = 1717;
	

	

	/**
	 * Metoda za pokretanje servera
	 * 
	 * @throws IOException
	 */
	public static void serverStart() throws IOException {

		ServerSocket server = null;
		// File quotes je file iz kojeg ce server uzimati citate i ispisivati ih
		File quotes = new File(quotesFile);
		// File auth je file u koj ce zapisivati kad i ko se konektovao na
		// server
		File auth = new File(authFile);

		try {

			server = new ServerSocket(port);
			while (true) {
				System.out.println("Waiting for connection");
				Socket user = server.accept();
				System.out.println("Connected");

				// Uzimanje pasvorda od Usera
				InputStream is = user.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				OutputStream os = user.getOutputStream();

				while (true) {
					// Slucaj ako je pass tacan
					if (br.readLine().equals(pass)) {
						System.out.println("You are logged in ");

						// poruka koju saljem Useru da je tacno unio pass:
						String message = "Welcome!\n";

						// pozivam f-ju koja daje jedan red/citat iz fila i
						// salje korisniku
						String quote = getQuote(quotes);
						// dodajem citat na String koji saljem korisniku
						message += quote + "\n";
						os.write(message.getBytes());
						os.flush();
						break;

					} else {
						// slucaj kada korisnik nije unio pass tacan

						System.out.println("Denied");
						// posaljem poruku korisniku
						String message = "Incorrect password!\n";
						os.write(message.getBytes());

						// Zapisem userov IP, datum pristupanja u file ako je
						// pogresno unio pass u auth_log.txt
						FileWriter fw = new FileWriter(auth, true);
						StringBuilder sbOut = new StringBuilder();
						sbOut.append("[").append(new Date()).append("  ")
								.append(user.getRemoteSocketAddress())
								.append("]");
						fw.write(sbOut.toString() + "\r" + "\n");
						fw.flush();
						fw.close();
						break;
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			server.close();
		}

	}

	/**
	 * Metoda koja bira jedan random broj/red iz kojeg ispisuje citat
	 * 
	 * @param quotes
	 *            - je file u kojem su upisani citati
	 * @return vraca String gdje smo zalisali taj citat
	 */
	private static String getQuote(File quotes) {
		int rand = (int) (1 + Math.random() * 15);
		String str = "";
		try {
			FileInputStream fs = new FileInputStream(quotes);
			InputStreamReader isr = new InputStreamReader(fs);
			BufferedReader br = new BufferedReader(isr);
			for (int i = 0; i < rand; i++) {
				br.readLine();
			}
			str = br.readLine();
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return str;
	}

	// uvijek moras pokrenuti server u main metodi
	public static void main(String[] args) {
		try {
			serverStart();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}