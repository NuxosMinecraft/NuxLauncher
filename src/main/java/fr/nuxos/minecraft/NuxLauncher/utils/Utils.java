package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.File;

public class Utils {
	private static File workingDirectory;
	private static OS operatingSystem;

	private Utils() {
	}
	
	public static void init(String applicationName) {
		String userHome = System.getProperty("user.home", ".");
		operatingSystem = OS.UNKNOWN;
		workingDirectory = new File(userHome, applicationName + '/');
		
		// Detect Operating System and working directory
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			operatingSystem = OS.WINDOWS;
			String applicationData = System.getenv("APPDATA");
			if (applicationData != null) {
				workingDirectory = new File(applicationData, "." + applicationName + '/');
			} else {
				workingDirectory = new File(userHome, '.' + applicationName + '/');
			}
		}
		if (osName.contains("mac")) {
			operatingSystem = OS.MAC_OS;
			workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
		}
		if (osName.contains("solaris")) {
			operatingSystem = OS.SOLARIS;
			workingDirectory = new File(userHome, '.' + applicationName + '/');
		}
		if (osName.contains("sunos")) {
			operatingSystem = OS.SOLARIS;
			workingDirectory = new File(userHome, '.' + applicationName + '/');
		}
		if (osName.contains("linux")) {
			operatingSystem = OS.LINUX;
			workingDirectory = new File(userHome, '.' + applicationName + '/');
		}
		if (osName.contains("unix")) {
			operatingSystem = OS.LINUX;
			workingDirectory = new File(userHome, '.' + applicationName + '/');
		}
		
		// Create basic dirs 
		if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) {
			throw new RuntimeException("The working directory could not be created: " + workingDirectory);
		}
		File binDirectory = new File(workingDirectory, "bin/");
		if (!binDirectory.exists()) {
			binDirectory.mkdirs();
		}
		File modsDirectory = new File(workingDirectory, "mods/");
		if (!modsDirectory.exists()) {
			modsDirectory.mkdirs();
		}
		File tmpDirectory = new File(workingDirectory, "tmp/");
		if (!tmpDirectory.exists()) {
			tmpDirectory.mkdirs();
		}
	}
	public static OS getOS() {
		return operatingSystem;
	}

	public static File getWorkingDir() {
		return workingDirectory;
	}
	
	public enum OS {
		LINUX,
		SOLARIS,
		WINDOWS,
		MAC_OS,
		UNKNOWN;
	}
}
