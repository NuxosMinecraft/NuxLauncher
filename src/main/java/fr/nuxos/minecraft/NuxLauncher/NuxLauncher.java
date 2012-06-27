package fr.nuxos.minecraft.NuxLauncher;

import java.io.File;

import fr.nuxos.minecraft.NuxLauncher.console.ConsolePerformer;
import fr.nuxos.minecraft.NuxLauncher.exceptions.InvalidLauncherModeException;
import fr.nuxos.minecraft.NuxLauncher.utils.Utils;

public class NuxLauncher {

	static String nuxLauncherVersion = "indev";
	static Integer minecraftLauncherVersion = 13;
	static File config;

	private Performer performer;

	public NuxLauncher(String Mode) {
		try {
			config = new File(Utils.getWorkingDir(), "config.yml");
			if (!config.exists()) {
				config.createNewFile();
			}

			if (Mode.equals("console")) {
				performer = new ConsolePerformer(this);
			} else if (Mode.equals("gui")) {
				// not yet implemented
			} else {
				throw new InvalidLauncherModeException();
			}

			performer.doLogin();

			performer.doUpdate();

			performer.doLaunchMinecraft();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getMinecraftLauncherVersion() {
		return minecraftLauncherVersion;
	}

	public static File getConfig() {
		return config;
	}
}
