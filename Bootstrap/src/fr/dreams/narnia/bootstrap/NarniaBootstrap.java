package fr.dreams.narnia.bootstrap;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ClasspathConstructor;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.openlauncherlib.util.SplashScreen;
import fr.theshark34.openlauncherlib.util.explorer.ExploredDirectory;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;

import java.io.File;

import static fr.theshark34.swinger.Swinger.*;

public class NarniaBootstrap {


    private static SplashScreen splash;
    private static SColoredBar bar;
    private static Thread barThread;

    private static final File NMC_B_DIR = new File(GameDirGenerator.createGameDir("NarniaV1"), "Launcher"); // Version

    private static CrashReporter NMC_B_REPORTER = new CrashReporter("Narnia 1 Bootstrap", NMC_B_DIR); // Version

    public static void main(String[] args) {

        setSystemLookNFeel();
        setResourcePath("/fr/dreams/narnia/bootstrap/ressources/");
        showSplash();
        try {
            Update();
        } catch (Exception e) {
            if (barThread != null)
                barThread.interrupt();

            NMC_B_REPORTER.catchError(e, "Impossible de mettre a jour le launcher !");
        }
        try {
            launch();
        } catch (LaunchException e) {
            NMC_B_REPORTER.catchError(e, "Impossible de lancer le launcher !");

        }

    }

    private static void showSplash() {
        splash = new SplashScreen("Narnia 1", getResource("splash.png")); // Version
        splash.setLayout(null);
        splash.setBackground(Swinger.TRANSPARENT);

        bar = new SColoredBar(getTransparentWhite(100), getTransparentWhite(175));
        bar.setBounds(0, 252, 256, 4);
        splash.add(bar);

        splash.setVisible(true);
    }

    private static void Update() throws Exception {
        SUpdate su = new SUpdate("https://www.eldaria.fr/launcher/bootstrap/", NMC_B_DIR);
        su.getServerRequester().setRewriteEnabled(true);

        barThread = new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    bar.setValue((int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000));
                    bar.setMaximum((int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000));
                }
            }
        };
        barThread.start();

        su.start();
        barThread.interrupt();

    }

    private static void launch() throws LaunchException {
        ClasspathConstructor constructor = new ClasspathConstructor();
        ExploredDirectory gameDir = Explorer.dir(NMC_B_DIR);
        constructor.add(gameDir.sub("Libs").allRecursive().files().match("^(.*\\.((jar)$))*$"));
        constructor.add(gameDir.get("Launcher.jar"));

        ExternalLaunchProfile profile = new ExternalLaunchProfile("fr.dreams.LauncherFrame", constructor.make());
        ExternalLauncher launcher = new ExternalLauncher(profile);

        Process b = launcher.launch();

        splash.setVisible(false);

        try {
            b.waitFor();
        } catch (InterruptedException ignored) {
        }
        System.exit(0);
    }
}
