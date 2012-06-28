package fr.nuxos.minecraft.NuxLauncher;

import fr.nuxos.minecraft.NuxLauncher.utils.Utils;

public class Main {

	static NuxLauncher launcher;

	public static void main(String[] args) {
		// TODO : args

		Utils.init("nuxos");
		launcher = new NuxLauncher("console");
	}
}
