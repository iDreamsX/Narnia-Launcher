package fr.dreams;

import com.sun.awt.AWTUtilities;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.animation.Animator;
import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;


public class LauncherFrame extends JFrame {

    private static LauncherFrame instance;
    private LauncherPanel launcherPanel;
    private static CrashReporter crashReporter;

    public LauncherFrame(){
        this.setTitle("Narnia");
        this.setSize(1082, 610);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setIconImage(Swinger.getResource("icon.png"));
        this.setBackground(Swinger.TRANSPARENT);
        this.setContentPane(launcherPanel = new LauncherPanel());
        AWTUtilities.setWindowOpacity(this, 0.0F);

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);

        Animator.fadeInFrame(this, 3);


    }

    public static void main(String[] args) {
        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath("/fr/dreams/ressources/");
        Launcher.NMC_CRASHES_DIR.mkdirs();
        crashReporter = new CrashReporter("Narnia V2 Launcher", Launcher.NMC_CRASHES_DIR); // Version

        instance = new LauncherFrame();
    }

    public static LauncherFrame getInstance() {
        return instance;
    }

    public LauncherPanel getLauncherPanel() {
        return this.launcherPanel;
    }

    public static CrashReporter getCrashReporter(){
        return crashReporter;
    }

}
