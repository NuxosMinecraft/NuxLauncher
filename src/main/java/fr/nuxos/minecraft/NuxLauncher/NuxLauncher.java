package fr.nuxos.minecraft.NuxLauncher;

import java.io.File;

import fr.nuxos.minecraft.NuxLauncher.console.ConsolePerformer;
import fr.nuxos.minecraft.NuxLauncher.exceptions.InvalidLauncherModeException;

public class NuxLauncher {

	static String nuxLauncherVersion = "indev";
	static Integer minecraftLauncherVersion = 13;

	private Performer performer;
	public final File workingDirectory;

	public NuxLauncher(String Mode) {
		String userHome = System.getProperty("user.home", ".");
		workingDirectory = new File(userHome + "/.nuxos/"); //TODO : windows and mac

		if (!workingDirectory.exists()) {
			workingDirectory.mkdirs();
		}

		try {
			if (Mode.equals("console")) {
				performer = new ConsolePerformer(this);
			} else if (Mode.equals("gui")) {
				// not yet implemented
			} else {
				throw new InvalidLauncherModeException();
			}

			performer.doLogin();

			performer.doUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getMinecraftLauncherVersion() {
		return minecraftLauncherVersion;
	}

}
