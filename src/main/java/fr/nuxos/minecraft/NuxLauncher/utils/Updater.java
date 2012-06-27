package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.jar.JarOutputStream;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLFormat;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLNode;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLProcessor;

public class Updater {
	private YAMLProcessor config;

	public Updater() throws IOException {
		Downloader repoDL = new Downloader("http://launcher.nuxos-minecraft.fr/repo.yml", Utils.getWorkingDir().toString() + "/repo.yml");
		repoDL.start();

		YAMLProcessor repo = new YAMLProcessor(repoDL.getOutFile(), false, YAMLFormat.EXTENDED);
		repo.load();

		config = new YAMLProcessor(NuxLauncher.getConfig(), false, YAMLFormat.EXTENDED);
		config.load();

		if (repo.getInt("repository.version") > config.getInt("repository.version", 0)) {
			updateAll(repo.getNodes("repository.highest"));
			updateAll(repo.getNodes("repository.high"));
			updateAll(repo.getNodes("repository.normal"));
			updateOptional(repo.getNodes("repository.optional"));

			config.setProperty("repository.version", repo.getInt("repository.version"));
			config.save();
		}
	}

	private void updateAll(Map<String, YAMLNode> files) throws FileNotFoundException, IOException {
		for (String index : files.keySet()) {
			YAMLNode file = files.get(index);
			updateFile(file);
		}
	}

	private void updateOptional(Map<String, YAMLNode> files) throws IOException {
		for (String index : files.keySet()) {
			YAMLNode file = files.get(index);
			if (config.getBoolean("optional." + index + ".enabled", false)) {
				updateFile(file);
			}
		}
	}

	private static void updateFile(YAMLNode file) throws FileNotFoundException, IOException {
		if (file.getString("mode").equalsIgnoreCase("copy")) {
			Downloader fileDL = new Downloader(file.getString("source").replace("$os$", Utils.getOSName()), Utils.getWorkingDir().toString() + "/" + file.getString("destination"));
			fileDL.start();

			// Extract natives
			if (file.getString("destination").contains("natives")) {
				JarInputStream inputStream = new JarInputStream(new FileInputStream(fileDL.getOutFile()));

				ZipEntry entry = inputStream.getNextEntry();
				while (entry != null) {
					if (entry.getName().endsWith(".so") || entry.getName().endsWith(".dll") || entry.getName().endsWith("lib")) {
						FileOutputStream outputStream = new FileOutputStream(Utils.getWorkingDir().toString() + "/bin/natives/" + entry.getName());
						copyStream(inputStream, outputStream);
						outputStream.close();
					}
					entry = inputStream.getNextEntry();
				}
				inputStream.close();
				fileDL.getOutFile().delete();
			}
		} else if (file.getString("mode").equalsIgnoreCase("jarupdate")) {
			String[] out = file.getString("source").split("/");
			Downloader fileDL = new Downloader(file.getString("source"), Utils.getWorkingDir().toString() + "/tmp/" + out[out.length - 1]);
			fileDL.start();

			// What will contain the new jar
			File newJar = new File(Utils.getWorkingDir().toString() + "/" + file.getString("destination") + ".tmp");
			JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(newJar));

			// The mod to add
			File tmpJar = fileDL.getOutFile();
			JarInputStream inputStream = new JarInputStream(new FileInputStream(tmpJar));

			ArrayList<String> list = new ArrayList<String>();

			ZipEntry entry = inputStream.getNextEntry();
			while (entry != null) {
				outputStream.putNextEntry(new ZipEntry(entry.getName()));
				copyStream(inputStream, outputStream);
				list.add(entry.getName());
				entry = inputStream.getNextEntry();
			}
			inputStream.close();
			tmpJar.delete();

			// The old jar, e.g. the original minecraft.jar
			File oldJar = new File(Utils.getWorkingDir().toString() + "/" + file.getString("destination"));
			JarInputStream oldStream = new JarInputStream(new FileInputStream(oldJar));

			entry = oldStream.getNextEntry();
			while (entry != null) {
				if (!list.contains(entry.getName())) {
					outputStream.putNextEntry(new ZipEntry(entry.getName()));
					copyStream(oldStream, outputStream);
				}
				entry = oldStream.getNextEntry();
			}
			oldStream.close();

			outputStream.flush();
			outputStream.close();

			oldJar.delete();
			newJar.renameTo(oldJar);
		}
	}

	static void copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int count = input.read(buffer);
			if (count < 0) {
				break;
			}
			output.write(buffer, 0, count);
		}
	}
}
