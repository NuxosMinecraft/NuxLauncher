package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import fr.nuxos.minecraft.NuxLauncher.Performer;

public class DownloadsManager {
	private LinkedList<Downloader> downloadsList;
	private ListIterator<Downloader> iterator;
	private Performer performer;

	DownloadsManager(Performer performer) {
		downloadsList = new LinkedList<Downloader>();
		this.performer = performer;
	}

	public void addDownload(String source, String dest, String md5, String name, String downloadId) {
		Downloader download = new Downloader(source.replace("$os$", Utils.getOSName()), dest, md5, downloadId, name, this);

		downloadsList.add(download);
	}

	public void startDownloads() {
		iterator = downloadsList.listIterator();
		startNextDownload();
	}

	private void startNextDownload() {
		if (iterator.hasNext()) {
			Downloader download = iterator.next();
			Thread t = new Thread(download);
			t.start();
			performer.changeProgress("Téléchargement de " + download.getName(), 0);
		} else {
			downloadsFinished();
		}
	}

	public void updateProgress(Downloader download) {
		performer.changeProgress("Téléchargement de " + download.getName(), download.getPercent());
	}

	public void downloadFinished(Downloader download) {
		performer.changeProgress(download.getName() + " téléchargé", 100);
	}

	public void actionFinished(Downloader downloader) {
		startNextDownload();
	}

	private void downloadsFinished() {
		performer.changeProgress("Installation des fichiers", 0);
		moveTmpFiles();
		Updater.downloadsFinished();
		performer.downloadsFinished();
	}

	public void downloadFailed(String reason, Downloader download) {
		String[] options = { "Abandonner les téléchargements", "Recommencer le téléchargement" };
		int answer = JOptionPane.showOptionDialog(null, "Erreur : " + reason, "Erreur de téléchargement pour " + download.getName(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (answer == 0) {
			FileUtils.deleteQuietly(new File(Utils.getWorkingDir().toString() + "/tmp/"));
			performer.downloadsFinished();
		} else {
			Thread t = new Thread(download);
			t.start();
			performer.changeProgress("Téléchargement de " + download.getName(), 0);
		}
	}

	private void moveTmpFiles() {
		File tmpDir = new File(Utils.getWorkingDir().toString() + "/tmp/");
		for (File file : tmpDir.listFiles()) {
			File dest = new File(Utils.getWorkingDir().toString() + "/" + file.getName());
			FileUtils.deleteQuietly(dest);
			boolean success = file.renameTo(dest); // TODO: error if != true
		}
	}
}
