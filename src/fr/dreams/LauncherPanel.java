package fr.dreams;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import static fr.dreams.Launcher.NMC_SAVER;
import static fr.dreams.Launcher.NMC_URL;
import static fr.theshark34.swinger.Swinger.drawFullsizedImage;
import static fr.theshark34.swinger.Swinger.getResource;

public class LauncherPanel extends JPanel implements SwingerEventListener{

    private Image background = getResource("background.png");
    private BufferedImage news1 = null;
    private BufferedImage news2 = null;
    private BufferedImage news3 = null;

    private JTextField usernameField = new JTextField(NMC_SAVER.get("username"));
    private JPasswordField passwordField = new JPasswordField(NMC_SAVER.get("password"));

    private STexturedButton playButton = new STexturedButton(getResource("play.png"), getResource("play_hover.png"));
    private STexturedButton quitButton = new STexturedButton(getResource("quit.png"), getResource("quit_hover.png"));
    private STexturedButton hideButton = new STexturedButton(getResource("hide.png"), getResource("hide_hover.png"));
    private STexturedButton settingsButton = new STexturedButton(getResource("settings.png"), getResource("settings_hover.png"));

    private STexturedButton copyrightButton = new STexturedButton(getResource("copyright.png"), getResource("copyright_hover.png"));


    private SColoredBar progressBar = new SColoredBar(new Color(22, 22, 22, 100), new Color(80, 156, 61, 255));
    private JLabel infoLabel = new JLabel("Clique sur jouer !");


    public LauncherPanel() {
        this.setLayout(null);
        this.setBackground(Swinger.TRANSPARENT);

        usernameField.setForeground(Color.WHITE);
        usernameField.setFont(usernameField.getFont().deriveFont(20F));
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setOpaque(false);
        usernameField.setBorder(null);
        usernameField.setBounds(389, 122, 345, 40);
        this.add(usernameField);

        playButton.setBounds(385, 258);
        playButton.addEventListener(this);
        this.add(playButton);

        quitButton.setBounds(1046, 9);
        quitButton.addEventListener(this);
        this.add(quitButton);

        hideButton.setBounds(1019, 9);
        hideButton.addEventListener(this);
        this.add(hideButton);

        settingsButton.setBounds(984, 9);
        settingsButton.addEventListener(this);
        this.add(settingsButton);

        copyrightButton.addEventListener(this);
        copyrightButton.setBounds(1060, 585);
        this.add(copyrightButton);

        progressBar.setBounds(4, 602, 1074, 4);
        this.add(progressBar);

        infoLabel.setBounds(6, 585, 364, 18);
        infoLabel.setForeground(Color.WHITE);
        this.add(infoLabel);

        try {
            URLConnection connection = new URL(NMC_URL.concat("/news_1.png")).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
            news1 = ImageIO.read(connection.getInputStream());
            repaint();
        } catch (IOException ex) {
            System.err.println("Impossible de charger l'image de news (" + ex + ")");
        }

        try {
            URLConnection connection = new URL(NMC_URL.concat("/news_2.png")).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
            news2 = ImageIO.read(connection.getInputStream());
            repaint();
        } catch (IOException ex) {
            System.err.println("Impossible de charger l'image de news (" + ex + ")");
        }

        try {
            URLConnection connection = new URL(NMC_URL.concat("/news_3.png")).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
            news3 = ImageIO.read(connection.getInputStream());
            repaint();
        } catch (IOException ex) {
            System.err.println("Impossible de charger l'image de news (" + ex + ")");
        }
    }


    @Override
    public void onEvent(SwingerEvent e) {
        if (e.getSource() == playButton) {
            setFieldsEnabled(false);

                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Launcher.auth(usernameField.getText());
                        } catch (AuthenticationException e) {
                            JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de se connecter : " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                            setFieldsEnabled(true);
                            return;
                        }

                        NMC_SAVER.set("username", usernameField.getText());

                        try {
                            Launcher.update();
                        } catch (Exception e) {
                            Launcher.interruptThread();
                            LauncherFrame.getCrashReporter().catchError(e, "Impossible de mettre a jour Narnia !");
                        }

                        try {
                            Launcher.launch();
                        } catch (LaunchException e) {
                            LauncherFrame.getCrashReporter().catchError(e, "Impossible de lancer Narnia !");
                        }
                    }
                };
                t.start();

        } else if (e.getSource() == quitButton)
            Animator.fadeOutFrame(LauncherFrame.getInstance(), 2, new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            });
        else if (e.getSource() == hideButton)
            LauncherFrame.getInstance().setState(1);
        else if (e.getSource() == this.settingsButton)
            OptionFrame.getInstance().setVisible(true);
        else if (e.getSource() == this.copyrightButton)
            InfosFrame.getInstance().setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFullsizedImage(g, this, background);
        if (news1 != null)
            g.drawImage(news1, 35, 393, 313, 180, this);
        if (news2 != null)
            g.drawImage(news2, 385, 393, 313, 180, this);
        if (news3 != null)
            g.drawImage(news3, 735, 393, 313, 180, this);
    }

    public void setFieldsEnabled(boolean enabled){
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        playButton.setEnabled(enabled);
    }

    public SColoredBar getProgressBar() {
        return progressBar;
    }

    public void setInfoText(String text) {
        this.infoLabel.setText(text);
    }
}
