package fr.nuxos.minecraft.NuxLauncher.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.utils.Utils;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 8717494428368790716L;
	
	// declarations
	private GuiPerformer performer;
	private NuxLauncher launcher;

	// GUI elements
	private static JPanel contentPane;
	private static JLabel backgroundLabel;
	private static JLabel titleLabel;
	private static JLabel descriptionLabel;
	private static JLabel userLabel;
	private static JLabel passLabel;
	private static JTextField userField;
	private static JPasswordField passField;
	private static JButton playButton;
	private static JButton optionsButton;
	private static JCheckBox rememberCheckBox;
	private static JLabel statusLabel;
	private static JProgressBar statusPbar;
	private static JScrollPane scrollPane;
	private static JTextPane textPane;

	private static boolean isLogged = false;

	public MainFrame(GuiPerformer performer, NuxLauncher launcher) {
		this.performer = performer;
		this.launcher = launcher;

		setTitle("Nuxos Launcher v. " + launcher.getNuxLauncherVersion());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((dim.width - 854) / 2, (dim.height - 480) / 2, 854, 480);

		try {
			InputStream in = MainFrame.class.getResourceAsStream("/icon.png");
			if (in != null) {
				setIconImage(ImageIO.read(in));
			}
		} catch (IOException e) {
		}

		if (init()) {
			setVisible(true);
		} else {

		}
	}

	private boolean init() {

		try {

			contentPane = new JPanel();
			contentPane.setBorder(null);
			contentPane.setLayout(null);

			getContentPane().add(contentPane);
			
			textPane = new JTextPane();
		    textPane.setEditable(false);
		    textPane.setMargin(null);
		    textPane.setBackground(new Color(255, 255, 255, 20));
		    textPane.setContentType("text/html");
		    textPane.addHyperlinkListener(EXTERNAL_HYPERLINK_LISTENER);
			
			scrollPane = new JScrollPane(textPane);
			scrollPane.setBounds(0, 0, 350, 480);
			scrollPane.setBackground(new Color(255, 255, 255, 50));
			scrollPane.setBorder(null);
			contentPane.add(scrollPane);

			titleLabel = new JLabel("Nuxos Minecraft");
			titleLabel.setForeground(Color.WHITE);
			titleLabel.setBounds(600, 168, 316, 38);
			titleLabel.setFont(new java.awt.Font("Bitstream Charter", 0, 32));
			contentPane.add(titleLabel);

			descriptionLabel = new JLabel("La communauté avant tout.");
			descriptionLabel.setForeground(Color.WHITE);
			descriptionLabel.setBounds(600, 195, 316, 38);
			descriptionLabel.setFont(new java.awt.Font("Bitstream Charter", 0, 16));
			contentPane.add(descriptionLabel);

			userLabel = new JLabel("Username");
			userLabel.setForeground(Color.WHITE);
			userLabel.setToolTipText("Votre nom d'utilisateur minecraft.net");
			userLabel.setBounds(642, 342, 88, 15);
			contentPane.add(userLabel);

			userField = new JTextField();
			userField.setToolTipText("Votre nom d'utilisateur minecraft.net");
			userField.setBounds(730, 340, 110, 20);
			userField.setColumns(10);
			contentPane.add(userField);

			passLabel = new JLabel("Password");
			passLabel.setForeground(Color.WHITE);
			passLabel.setToolTipText("Votre mot de passe minecraft.net");
			passLabel.setBounds(645, 368, 85, 15);
			contentPane.add(passLabel);

			passField = new JPasswordField();
			passField.setToolTipText("Votre mot de passe minecraft.net");
			passField.setBounds(730, 366, 110, 20);
			contentPane.add(passField);

			playButton = new JButton("Login");
			playButton.setToolTipText("Connexion (SSL) et lancement du jeu");
			playButton.setBounds(630, 418, 211, 30);

			playButton.addActionListener(new ActionListener() {
				@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent arg0) {
					if (isLogged == false) {
						if (passField.getText().isEmpty() || userField.getText().isEmpty()) {
						} else {
							performer.doLogin();
						}
					} else {
						performer.doLaunchMinecraft();
					}

				}
			});

			contentPane.add(playButton);

			optionsButton = new JButton("");
			optionsButton.setIcon(new ImageIcon(MainFrame.class.getResource("/gui/cog.png")));
			optionsButton.setToolTipText("Gestion des dépôts");
			optionsButton.setBounds(596, 418, 30, 30);

			optionsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					ConfigurationFrame.main();
				}
			});

			contentPane.add(optionsButton);

			rememberCheckBox = new JCheckBox();
			rememberCheckBox.setText("Remember");
			rememberCheckBox.setToolTipText("Mémorise les identifiants");
			rememberCheckBox.setBounds(730, 390, 111, 19);
			rememberCheckBox.setOpaque(false);
			rememberCheckBox.setForeground(Color.WHITE);
			contentPane.add(rememberCheckBox);

			statusLabel = new JLabel();
			statusLabel = new JLabel("Connectez vous à votre compte minecraft.net");
			statusLabel.setForeground(Color.WHITE);
			statusLabel.setToolTipText("Indique l'état du launcher");
			statusLabel.setBounds(368, 12, 472, 15);
			contentPane.add(statusLabel);

			statusPbar = new JProgressBar();
			statusPbar.setVisible(false);
			statusPbar.setMaximum(100);
			statusPbar.setMinimum(0);
			statusPbar.setValue(50);
			statusPbar.setBounds(368, 30, 472, 20);
			contentPane.add(statusPbar);

			backgroundLabel = new JLabel("");
			backgroundLabel.setForeground(Color.WHITE);
			
			Random r = new Random();
			int intScreen = 1 + r.nextInt(4);
					
			backgroundLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/gui/bg" + intScreen + ".png")));
			backgroundLabel.setBounds(0, 0, 854, 480);
			contentPane.add(backgroundLabel);

			readRemember();
			updateNews();
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}
	
	private void updateNews() {
		new Thread()
	      {
	        public void run() {
	          try {
	            textPane.setPage(new URL("http://launcher.nuxos-minecraft.fr/news/index.php"));
	          } catch (Exception localException) {
	            localException.printStackTrace();
	            textPane.setText("<html><body><font color=\"white\"><br><br><br><br><br><br><br><center><h2>Erreur de connexion.</h2><br>" + localException.toString() + "</center></font></body></html>");
	          }
	        }
	      }
	      .start();
	}

	// thanks to mojang ...
	private static final HyperlinkListener EXTERNAL_HYPERLINK_LISTENER = new HyperlinkListener() {
		public void hyperlinkUpdate(HyperlinkEvent paramAnonymousHyperlinkEvent) {
			if (paramAnonymousHyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
				try {
					openLink(paramAnonymousHyperlinkEvent.getURL().toURI());
				} catch (Exception localException) {
					localException.printStackTrace();
				}
		}
	};

	// thanks to mojang 2 ...
	private static void openLink(URI paramURI) {
		try {
			Object localObject = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
			localObject.getClass().getMethod("browse", new Class[] { URI.class }).invoke(localObject, new Object[] { paramURI });
		} catch (Throwable localThrowable) {
			System.out.println("Failed to open link " + paramURI.toString());
		}
	}

	public void setStatus(String status) {
		statusLabel.setText(status);
	}

	public void setProgression(Integer progression) {
		if (progression >= statusPbar.getMinimum() && progression <= statusPbar.getMaximum()) {
			statusPbar.setValue(progression);
		}
	}

	public void setProgressBarView(Boolean view) {
		statusPbar.setVisible(view);
	}

	public String getUsername() {
		return userField.getText();
	}

	@SuppressWarnings("deprecation")
	public String getPassword() {
		return passField.getText();
	}

	public void setButtonText(String text) {
		playButton.setText(text);
	}

	public void setButtonEnabled(boolean enabled) {
		playButton.setEnabled(enabled);
	}

	public void setLogged(boolean logged) {
		isLogged = logged;
	}

	private void readRemember() {
		try {
			File lastLogin = new File(Utils.getWorkingDir(), "lastlogin");
			if (!lastLogin.exists()) {
				return;
			}

			Cipher cipher = getCipher(2, "passwordfile");

			DataInputStream dis;
			if (cipher != null) {
				dis = new DataInputStream(new CipherInputStream(new FileInputStream(lastLogin), cipher));
			} else {
				dis = new DataInputStream(new FileInputStream(lastLogin));
			}

			userField.setText(dis.readUTF());
			passField.setText(dis.readUTF());
			rememberCheckBox.setSelected(passField.getPassword().length > 0);
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeRemember() {
		try {
			File lastLogin = new File(Utils.getWorkingDir(), "lastlogin");

			Cipher cipher = getCipher(1, "passwordfile");

			DataOutputStream dos;
			if (cipher != null)
				dos = new DataOutputStream(new CipherOutputStream(new FileOutputStream(lastLogin), cipher));
			else {
				dos = new DataOutputStream(new FileOutputStream(lastLogin));
			}

			dos.writeUTF(userField.getText());
			dos.writeUTF(rememberCheckBox.isSelected() ? new String(passField.getPassword()) : "");
			dos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final static Cipher getCipher(int mode, String password) throws Exception {
		Random random = new Random(43287234L);
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);

		SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(password.toCharArray()));
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(mode, pbeKey, pbeParamSpec);
		return cipher;
	}
}
