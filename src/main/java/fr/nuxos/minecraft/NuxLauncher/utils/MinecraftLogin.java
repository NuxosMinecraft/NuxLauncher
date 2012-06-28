package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PublicKey;

import javax.net.ssl.HttpsURLConnection;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.Performer;
import fr.nuxos.minecraft.NuxLauncher.exceptions.*;

public class MinecraftLogin implements Runnable {

	// Logging infos
	private static String userName;
	private static String password;
	
	// Logged infos
	/*
	private static String latestVersion;    -> logged_infos[0]
	private static String downloadTicket;   -> logged_infos[1]
	private static String sessionId;        -> logged_infos[3]
	private static String pseudo;           -> logged_infos[2]
	*/
	public String[] logged_infos = new String[4];
	
	// State
	private static Boolean isLogged;

	// Statement
	private NuxLauncher launcher;
	private Performer performer;

	public MinecraftLogin(NuxLauncher NL, Performer performer, String username, String password) {
		this.launcher = NL;
		this.performer = performer;
		this.userName = username;
		this.password = password;
	}
	

	public void run() {
		if(userName.isEmpty() || password.isEmpty()) {
			performer.authFinishedFail("Champs incomplets");
		}
		else {
			performer.changeProgress("Logging in ...", 0);
			login();
		}
		
	}

	
	private void login() {
		try {

			String parameters = "user=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + launcher.getMinecraftLauncherVersion();
			performer.changeProgress("Connexion à minecraft.net ...", 10);
			String result = executePostSSL("https://login.minecraft.net/", parameters, "minecraft");

			if (result == null) {
				performer.authFinishedFail("Can't connect");
				throw new MCNetworkException();		
			}

			if (!result.contains(":")) {
				if (result.trim().equals("Bad login")) {
					performer.authFinishedFail("Bad login");
					throw new BadLoginException();
				} else if (result.trim().equals("Old version")) {
					performer.authFinishedFail("Outdated version.");
					throw new OutdatedMCLauncherException();
				} else if (result.trim().contains("User not premium")) {
					performer.authFinishedFail("User not premium");
					throw new MinecraftUserNotPremiumException();
				} else if (result.trim().contains("Account migrated, use e-mail as username.")) {
					performer.authFinishedFail("Use you email instead");
					throw new AccountMigratedException();
				} else {
					performer.authFinishedFail("Unknown error");
					System.err.print("Unknown login result : \"" + result + "\"");
				}

			}
			
			performer.changeProgress("Lecture des données ...", 50);
			
			String[] values = result.split(":");

			logged_infos[2] = values[2].trim();
			logged_infos[0] = values[0].trim();
			logged_infos[1] = values[1].trim();
			logged_infos[3] = values[3].trim();

			isLogged = true;
			
			performer.authFinishedSuccess(logged_infos);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String executePostSSL(String targetURL, String urlParameters, String CertifName) {

		HttpsURLConnection connection = null;

		try {
			URL url = new URL(targetURL);

			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			// connection.setRequestProperty("Content-Language", "en-US");
			// useless
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();

			java.security.cert.Certificate[] certs = connection.getServerCertificates();

			byte[] bytes = new byte[294];
			DataInputStream minecraftCert = new DataInputStream(MinecraftLogin.class.getResourceAsStream("/" + CertifName + ".key"));
			minecraftCert.readFully(bytes);
			minecraftCert.close();

			java.security.cert.Certificate remoteCert = certs[0];
			PublicKey remoteCertPublicKey = remoteCert.getPublicKey();
			byte[] data = remoteCertPublicKey.getEncoded();

			for (int i = 0; i < data.length; i++) {
				if (data[i] == bytes[i])
					continue;
				throw new RuntimeException("Error : key mismatch !");
			}

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			StringBuffer response = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

			String str1 = response.toString();
			return str1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
