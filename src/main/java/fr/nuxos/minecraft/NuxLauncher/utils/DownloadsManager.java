package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.ListIterator;

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
		performer.downloadsFinished();
	}

	private void moveTmpFiles() {
		File tmpDir = new File(Utils.getWorkingDir().toString() + "/tmp/");
		for (File file : tmpDir.listFiles()) {
			File dest = new File(Utils.getWorkingDir().toString() + "/" + file.getName());
			removeDirectory(dest);
			boolean success = file.renameTo(dest); //TODO: error if != true
		}
	}

	private void removeDirectory(File directory) {
		String[] list = directory.list();
		for (int i = 0; i < list.length; i++) {
			File entry = new File(directory, list[i]);
			if (entry.isDirectory()) {
				removeDirectory(entry);
			} else {
				entry.delete();
			}
		}
		directory.delete();
	}
}
