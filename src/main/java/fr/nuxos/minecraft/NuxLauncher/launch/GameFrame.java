package fr.nuxos.minecraft.NuxLauncher.launch;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 5499648340202625650L;
	private JPanel wrapper;
	private Applet applet;

	GameFrame(Dimension dim) {
		setTitle("Minecraft");
		setBackground(Color.BLACK);

		/*
		 * try { InputStream in =
		 * Launcher.class.getResourceAsStream("/resources/icon.png"); if (in !=
		 * null) { setIconImage(ImageIO.read(in)); } } catch (IOException e) { }
		 */

		wrapper = new JPanel();
		wrapper.setOpaque(false);
		wrapper.setPreferredSize(dim != null ? dim : new Dimension(854, 480));
		wrapper.setLayout(new BorderLayout());
		add(wrapper, BorderLayout.CENTER);

		pack();
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				if (applet != null) {
					applet.stop();
					applet.destroy();
				}

				System.exit(0);
			}
		});
	}

	public void start(Applet applet) {
		applet.init();
		wrapper.add(applet, BorderLayout.CENTER);
		validate();
		applet.start();
	}
}