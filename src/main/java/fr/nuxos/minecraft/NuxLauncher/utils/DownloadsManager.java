package fr.nuxos.minecraft.NuxLauncher.utils;

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
		while (downloadsList != null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		downloadsList = null;
		performer.downloadsFinished();
	}
}
