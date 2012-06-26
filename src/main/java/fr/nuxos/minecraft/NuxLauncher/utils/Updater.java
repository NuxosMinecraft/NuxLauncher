package fr.nuxos.minecraft.NuxLauncher.utils;

import java.util.Map;

import fr.nuxos.minecraft.NuxLauncher.yml.YAMLNode;

public class Updater {
	public static void processFiles(Map<String, YAMLNode> files) {
		for (String index : files.keySet()) {
			YAMLNode file = files.get(index);
			System.out.println(file.getString("name"));
		}
	}
}
