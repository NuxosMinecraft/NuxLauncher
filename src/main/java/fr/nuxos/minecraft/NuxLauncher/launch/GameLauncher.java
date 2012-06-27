package fr.nuxos.minecraft.NuxLauncher.launch;

import java.applet.Applet;
import java.awt.Dimension;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fr.nuxos.minecraft.NuxLauncher.exceptions.LaunchException;
import fr.nuxos.minecraft.NuxLauncher.utils.MinecraftLogin;
import fr.nuxos.minecraft.NuxLauncher.utils.Utils;

public class GameLauncher {
	private File actualDir;
	private ClassLoader classLoader;
	private Map<String, String> parameters = new HashMap<String, String>();
	private Dimension windowDim;

	private GameLauncher() {
		this.actualDir = Utils.getWorkingDir();
	}

	public void setParameter(String key, String val) {
		parameters.put(key, val);
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	private void setupEnvironment() throws LaunchException {
		System.setProperty("org.lwjgl.librarypath", new File(actualDir, "bin/natives").getAbsolutePath());
		System.setProperty("net.java.games.input.librarypath", new File(actualDir, "bin/natives").getAbsolutePath());
	}

	private void setupClassLoader() {
		List<File> files = new ArrayList<File>();

		files.add(new File(actualDir, "bin/lwjgl.jar"));
		files.add(new File(actualDir, "bin/jinput.jar"));
		files.add(new File(actualDir, "bin/lwjgl_util.jar"));
		files.add(new File(actualDir, "bin/minecraft.jar"));

		URL[] urls = new URL[files.size()];
		int i = 0;
		for (File file : files) {
			try {
				urls[i] = file.toURI().toURL();
			} catch (MalformedURLException e) {
			}
			i++;
		}

		classLoader = new URLClassLoader(urls);
	}

	public Dimension getWindowDim() {
		return windowDim;
	}

	public void setWindowDim(Dimension windowDim) {
		this.windowDim = windowDim;
	}

	private void launch() throws LaunchException {

		setupEnvironment();
		setupClassLoader();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Class<?> cls = classLoader.loadClass("net.minecraft.client.MinecraftApplet");
					Applet game = (Applet) cls.newInstance();

					GameFrame frame = new GameFrame(windowDim);
					frame.setVisible(true);
					GameApplet applet = new GameApplet(parameters, game);
					frame.start(applet);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void setLookAndFeel() throws InterruptedException, InvocationTargetException {
		// Set look and fill
		// removed temporarly
		//SwingUtilities.invokeAndWait(new Runnable() {
			//public void run() {
				//try {
					//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				//} catch (Exception e) {
				//}
			//}
		//});
	}

	public static void main(MinecraftLogin logger) {

		try {
			setLookAndFeel();

			GameLauncher launcher = new GameLauncher();
			launcher.setWindowDim(new Dimension(854, 480));
			launcher.setParameter("stand-alone", "true");
			launcher.setParameter("username", logger.getPseudo());
			launcher.setParameter("sessionid", logger.getSessionId());

			launcher.launch();
		} catch (final LaunchException t) {
		} catch (final Throwable t) {
		}
	}
}
