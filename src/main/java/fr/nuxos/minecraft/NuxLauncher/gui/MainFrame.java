package fr.nuxos.minecraft.NuxLauncher.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.swing.JTextField;

import fr.nuxos.minecraft.NuxLauncher.utils.Utils;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 8717494428368790716L;
	// declarations
	private GuiPerformer performer;

	// GUI elements
	private static JPanel contentPane;
	private static JPanel newsPane;
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
	private static JLabel titleNewsLabel;

	private static boolean isLogged = false;

	public MainFrame(GuiPerformer performer) {
		this.performer = performer;

		setTitle("Nuxos Launcher v.indev");
		setResizable(false);
		setBounds(100, 100, 854, 480);
		setDefaultCloseOperation(3);

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

			newsPane = new JPanel();
			newsPane.setBounds(0, 0, 350, 480);
			newsPane.setBackground(new Color(255, 255, 255, 50));

			contentPane.add(newsPane);
			getContentPane().add(contentPane);

			// contentPane elements
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
			playButton.setToolTipText("Connexion et lancement du jeu. La connexion est faite directement auprès des serveurs de minecraft.net, et est sécurisé ( SSL ). En aucun cas le launcher ne récupère ces informations pour les transmettre à un tiers.");
			playButton.setBounds(630, 418, 211, 30);

			playButton.addActionListener(new ActionListener() {
				@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent arg0) {
					if (isLogged == false) {
						if (passField.getText().isEmpty() || userField.getText().isEmpty()) {
							// set status "retry", removed ( useless )
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
			optionsButton.setToolTipText("Connexion et lancement du jeu. La connexion est faite directement auprès des serveurs de minecraft.net, et est sécurisé ( SSL ). En aucun cas le launcher ne récupère ces informations pour les transmettre à un tiers.");
			optionsButton.setBounds(596, 418, 30, 30);
			contentPane.add(optionsButton);

			rememberCheckBox = new JCheckBox();
			rememberCheckBox.setText("Remember");
			rememberCheckBox.setToolTipText("Mémoriser votre nom d'utilisateur et votre mot de passe pour la prochaine connexion.");
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
			backgroundLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/gui/bg.png")));
			backgroundLabel.setBounds(0, 0, 854, 480);
			contentPane.add(backgroundLabel);

			// newsPane elements
			titleNewsLabel = new JLabel();
			titleNewsLabel.setText("News");
			// titleNews.setBounds(20, 220, 50, 20);
			titleNewsLabel.setForeground(Color.WHITE);
			titleNewsLabel.setFont(new java.awt.Font("Bitstream Charter", 0, 22));

			newsPane.add(titleNewsLabel);

			readRemember();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

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
