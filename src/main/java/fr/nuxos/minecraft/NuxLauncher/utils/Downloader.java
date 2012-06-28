package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import fr.nuxos.minecraft.NuxLauncher.exceptions.BadMd5Exception;

public class Downloader implements Runnable {
	private String inPath;
	private String outPath;
	private String exceptedMd5;
	private String downloadId;
	private String name;
	private DownloadsManager manager;
	private Downloader download = this;
	private File outFile;
	private int response;
	private long downloaded;
	private int size;

	public Downloader(String inPath, String outPath, String exceptedMd5, String downloadId, String name, DownloadsManager manager) {
		this.inPath = inPath;
		this.outPath = outPath;
		this.exceptedMd5 = exceptedMd5;
		this.downloadId = downloadId;
		this.name = name;
		this.manager = manager;
		outFile = null;
	}

	public Downloader(String inPath, String outPath) {
		this.inPath = inPath;
		this.outPath = outPath;
		this.exceptedMd5 = null;
		this.downloadId = null;
		this.manager = null;
		outFile = null;
	}

	public void run() {
		try {
			start();
			new Thread(new Runnable() {
				public void run() {
					manager.downloadFinished(download);
				}
			}).start();
			checkMD5();
			doAction();
			new Thread(new Runnable() {
				public void run() {
					manager.actionFinished(download);
				}
			}).start();
		} catch (MalformedURLException e) {
			manager.downloadFailed("erreur inconnue. regarder les logs et contacter un administrateur.", download);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			manager.downloadFailed("fichier non trouvé " + e.getMessage(), download);
			e.printStackTrace();
		} catch (IOException e) {
			manager.downloadFailed("erreur inconnue. regarder les logs et contacter un administrateur.", download);
			e.printStackTrace();
		} catch (BadMd5Exception e) {
			manager.downloadFailed("mauvais md5.", download);
			e.printStackTrace();
		}
	}

	public void start() throws MalformedURLException, FileNotFoundException, IOException {
		URL url = new URL(inPath);
		URLConnection conn = url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(false);
		System.setProperty("http.agent", "NuxLauncher");
		conn.setRequestProperty("User-Agent", "NuxLauncher");
		HttpURLConnection.setFollowRedirects(true);
		conn.setUseCaches(false);
		((HttpURLConnection) conn).setInstanceFollowRedirects(true);
		response = ((HttpURLConnection) conn).getResponseCode();
		InputStream in = conn.getInputStream();

		size = conn.getContentLength();
		outFile = new File(outPath);
		outFile.delete();

		ReadableByteChannel rbc = Channels.newChannel(in);
		FileOutputStream fos = new FileOutputStream(outFile);

		Thread t = new Thread() {
			public void run() {
				boolean running = true;
				while (running) {
					try {
						downloaded = outFile.length();
						Thread.sleep(100);
						manager.updateProgress(download);
					} catch (InterruptedException e) {
						running = false;
					}
				}
			}
		};
		t.start();

		fos.getChannel().transferFrom(rbc, 0, size > 0 ? size : Integer.MAX_VALUE);
		in.close();
		rbc.close();
		t.interrupt();
	}

	public void checkMD5() throws BadMd5Exception, IOException {
		if (exceptedMd5 == null) {
			return;
		}

		FileInputStream fis = new FileInputStream(outFile);
		String foundMd5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
		fis.close();

		if (!exceptedMd5.equalsIgnoreCase(foundMd5)) {
			throw new BadMd5Exception(outFile, exceptedMd5, foundMd5);
		}
	}

	public void doAction() throws FileNotFoundException, IOException {
		Updater.doAction(this);
	}

	public String getDownloadId() {
		return downloadId;
	}

	public String getName() {
		return name;
	}

	public File getOutFile() {
		return outFile;
	}

	public int getPercent() {
		return Math.round(100 * downloaded / size);
	}
}
