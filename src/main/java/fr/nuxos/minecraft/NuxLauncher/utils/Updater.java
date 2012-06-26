package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.jar.JarOutputStream;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import fr.nuxos.minecraft.NuxLauncher.yml.YAMLNode;

public class Updater {
	public static void processFiles(Map<String, YAMLNode> files) {
		try {
			for (String index : files.keySet()) {
				YAMLNode file = files.get(index);
				if (file.getString("mode").equalsIgnoreCase("copy")) {
					Downloader.download(file.getString("source"), Utils.getWorkingDir().toString() + "/" + file.getString("destination"));

				} else if (file.getString("mode").equalsIgnoreCase("jarupdate")) {
					Downloader.download(file.getString("source"), Utils.getWorkingDir().toString() + "/tmp/" + index);

					// What will contain the new jar
					File newJar = new File(Utils.getWorkingDir().toString() + "/" + file.getString("destination") + ".tmp");
					JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(newJar));

					// The mod to add
					File tmpJar = new File(Utils.getWorkingDir().toString() + "/tmp/" + index);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void copyStream(InputStream input, OutputStream output)
			throws IOException {
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
