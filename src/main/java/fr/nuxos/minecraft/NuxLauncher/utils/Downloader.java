package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader {
	public static void download(String inPath, String outPath) {
		try {
			System.out.println("DL : " + outPath);
			URL url = new URL(inPath);
			URLConnection conn = url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			System.setProperty("http.agent", "NuxLauncher");
			conn.setRequestProperty("User-Agent", "NuxLauncher");
			HttpURLConnection.setFollowRedirects(true);
			conn.setUseCaches(false);
			((HttpURLConnection) conn).setInstanceFollowRedirects(true);
			int response = ((HttpURLConnection) conn).getResponseCode();
			InputStream in = conn.getInputStream();

			int size = conn.getContentLength();
			File outFile = new File(outPath);
			outFile.delete();

			ReadableByteChannel rbc = Channels.newChannel(in);
			FileOutputStream fos = new FileOutputStream(outFile);

			fos.getChannel().transferFrom(rbc, 0,
					size > 0 ? size : Integer.MAX_VALUE);
			in.close();
			rbc.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
