package fr.nuxos.minecraft.NuxLauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import fr.nuxos.minecraft.NuxLauncher.console.ConsolePerformer;
import fr.nuxos.minecraft.NuxLauncher.exceptions.InvalidLauncherModeException;
import fr.nuxos.minecraft.NuxLauncher.gui.GuiPerformer;
import fr.nuxos.minecraft.NuxLauncher.utils.Downloader;
import fr.nuxos.minecraft.NuxLauncher.utils.Utils;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLFormat;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLProcessor;

import fr.nuxos.minecraft.NuxLauncher.launch.GameApplet;

public class NuxLauncher {

	static Integer nuxLauncherVersion = 1;
	static Integer minecraftLauncherVersion = 13;
	static YAMLProcessor config;
	static YAMLProcessor repo;

	public NuxLauncher(String Mode) {
		
		// necessary for minecraft forge
        System.setProperty("minecraft.applet.WrapperClass", GameApplet.class.getCanonicalName());

		try {
			File configFile = new File(Utils.getWorkingDir(), "config.yml");
			if (!configFile.exists()) {
				configFile.createNewFile();
			}
			config = new YAMLProcessor(configFile, false, YAMLFormat.EXTENDED);
			config.load();

			downloadRepo();

			if (Mode.equals("console")) {
				new ConsolePerformer(this);
			} else if (Mode.equals("gui")) {
				new GuiPerformer(this);
			} else {
				throw new InvalidLauncherModeException();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getMinecraftLauncherVersion() {
		return minecraftLauncherVersion;
	}
	
	public Integer getNuxLauncherVersion() {
		return nuxLauncherVersion;
	}

	public static void downloadRepo() {
		try {
			File repoFile = new File(Utils.getWorkingDir(), "repo.yml");
			Downloader repoDL;
			if (config.getBoolean("testrepo", false)) {
				repoDL = new Downloader("http://launcher.nuxos-minecraft.fr/repo-test.yml", repoFile.getAbsolutePath());
			} else {
				repoDL = new Downloader("http://launcher.nuxos-minecraft.fr/repo.yml", repoFile.getAbsolutePath());
			}
			repoDL.start();

			if (!repoFile.exists()) {
				repoFile.createNewFile();
			}

			repo = new YAMLProcessor(repoFile, false, YAMLFormat.EXTENDED);
			repo.load();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static YAMLProcessor getConfig() {
		return config;
	}

	public static YAMLProcessor getRepo() {
		return repo;
	}
}
