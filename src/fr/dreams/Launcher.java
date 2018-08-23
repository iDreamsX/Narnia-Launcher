package fr.dreams;

import fr.litarvan.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.BeforeLaunchingEvent;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;

import java.io.File;

public class Launcher {

    public static final GameVersion NMC_VERSION = new GameVersion("1.7.10", GameType.V1_7_10);
    public static final GameInfos NMC_INFOS = new GameInfos("NarniaV1", NMC_VERSION, new GameTweak[] {GameTweak.FORGE}); // Version
    public static final File NMC_DIR = NMC_INFOS.getGameDir();
    public static final File NMC_CRASHES_DIR = new File(NMC_DIR, "crashes");
    public static final Saver NMC_SAVER = new Saver(new File(NMC_DIR, "narnia.properties"));
    public static final String NMC_URL = "https://www.eldaria.fr/img/launcher/";

    private static AuthInfos authInfos;

    public static void auth(String username) throws AuthenticationException {
        authInfos = new AuthInfos(username, "sry", "nope");
    }

    private static final Thread updateThread = new Thread() {
        private int val;
        private int max;

        @Override
        public void run() {

            while (!this.isInterrupted()) {
                if (BarAPI.getNumberOfFileToDownload() == 0) {
                    LauncherFrame.getInstance().getLauncherPanel().setInfoText("<html><b><font color =#ffffff>Verifications des fichiers</font></b></html>");
                    continue;
                }
                val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
                max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);

                LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
                LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);

                LauncherFrame.getInstance().getLauncherPanel().setInfoText("<html><b><font color =#ffffff>Telechargement des fichiers " + " | </font><font color = #509c3d>" +
                        Swinger.percentage(val, max) + "%</font></b></html>");
            }
        }
    };

    public static void update() throws Exception {
        SUpdate su = new SUpdate("https://www.eldaria.fr/launcher/s-update", NMC_DIR);
        su.addApplication(new FileDeleter());

        updateThread.start();
        su.start();
        updateThread.interrupt();
    }

    public static void launch() throws LaunchException {
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(NMC_INFOS, GameFolder.BASIC, authInfos);

        AllowedMemory am = AllowedMemory.XMX1G;
        try {
            am = AllowedMemory.valueOf(NMC_SAVER.get("allowed-memory", "XMX1G"));
        } catch (IllegalArgumentException ex) {}
        profile.getVmArgs().addAll(0, am.getVmArgs());

        ExternalLauncher launcher = new ExternalLauncher(profile, new BeforeLaunchingEvent() {
            @Override
            public void onLaunching(ProcessBuilder processBuilder) {
                String javaPath = NMC_SAVER.get("java-path", "");
                if (javaPath != null && !javaPath.equals(""))
                    processBuilder.command().set(0, javaPath);
            }
        });

        Process p = launcher.launch();

        try
        {
            Thread.sleep(2000L);
            LauncherFrame.getInstance().setVisible(false);
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public static void interruptThread() {
        updateThread.interrupt();
    }

}