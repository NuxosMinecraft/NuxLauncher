package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.File;
import java.util.LinkedList;

public class Utils {
	private static OS operatingSystem;
	private static String operatingSystemName;
	private static File workingDirectory;

	private Utils() {
	}

	public static void init(String applicationName) {
		String userHome = System.getProperty("user.home", ".");
		operatingSystem = OS.UNKNOWN;
		operatingSystemName = "unknown";
		workingDirectory = new File(userHome, applicationName + '/');

		// Detect Operating System and working directory
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			operatingSystem = OS.WINDOWS;
			operatingSystemName = "windows";
			String applicationData = System.getenv("APPDATA");
			if (applicationData != null) {
				workingDirectory = new File(applicationData, '.' + applicationName);
			} else {
				workingDirectory = new File(userHome, '.' + applicationName);
			}
		}
		if (osName.contains("mac")) {
			operatingSystem = OS.MAC_OS;
			operatingSystemName = "macosx";
			workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
		}
		if (osName.contains("solaris")) {
			operatingSystem = OS.SOLARIS;
			operatingSystemName = "solaris";
			workingDirectory = new File(userHome, '.' + applicationName);
		}
		if (osName.contains("sunos")) {
			operatingSystem = OS.SOLARIS;
			operatingSystemName = "solaris";
			workingDirectory = new File(userHome, '.' + applicationName);
		}
		if (osName.contains("linux")) {
			operatingSystem = OS.LINUX;
			operatingSystemName = "linux";
			workingDirectory = new File(userHome, '.' + applicationName);
		}
		if (osName.contains("unix")) {
			operatingSystem = OS.LINUX;
			operatingSystemName = "linux";
			workingDirectory = new File(userHome, '.' + applicationName);
		}

		// Create basic dirs
		if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) {
			throw new RuntimeException("The working directory could not be created: " + workingDirectory);
		}
		
		LinkedList<File> dirList = new LinkedList<File>();
		dirList.add(new File(workingDirectory, "bin/"));
		dirList.add(new File(workingDirectory, "bin/natives/"));
		dirList.add(new File(workingDirectory, "mods/"));
		dirList.add(new File(workingDirectory, "tmp/"));
		dirList.add(new File(workingDirectory, "tmp/bin/"));
		dirList.add(new File(workingDirectory, "tmp/bin/natives/"));
		dirList.add(new File(workingDirectory, "tmp/mods/"));
		
		for(File dir : dirList) {
			if (!dir.exists()) {
				dir.mkdirs();
			}	
		}
	}

	public static OS getOS() {
		return operatingSystem;
	}

	public static String getOSName() {
		return operatingSystemName;
	}

	public static File getWorkingDir() {
		return workingDirectory;
	}

	public enum OS {
		LINUX, SOLARIS, WINDOWS, MAC_OS, UNKNOWN;
	}
}
