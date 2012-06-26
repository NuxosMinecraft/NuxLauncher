package fr.nuxos.minecraft.NuxLauncher;

import fr.nuxos.minecraft.NuxLauncher.console.ConsolePerformer;
import fr.nuxos.minecraft.NuxLauncher.exceptions.InvalidLauncherModeException;

public class NuxLauncher {

	static String nuxLauncherVersion = "indev";
	static Integer minecraftLauncherVersion = 13;

	private Performer performer;

	public NuxLauncher(String Mode) {
		try {
			if (Mode.equals("console")) {
				performer = new ConsolePerformer(this);
			} else if (Mode.equals("gui")) {
				// not yet implemented
			} else {
				throw new InvalidLauncherModeException();
			}

			//performer.doLogin();

			performer.doUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getMinecraftLauncherVersion() {
		return minecraftLauncherVersion;
	}
}
