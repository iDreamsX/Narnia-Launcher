package fr.dreams;


import javax.swing.*;
import java.awt.*;


public class InfosFrame extends JDialog{

    private static InfosFrame instance;

    public static InfosFrame getInstance() {
        if (instance == null)
            instance = new InfosFrame();
        return instance;
    }

    public InfosFrame() {
        super(LauncherFrame.getInstance(), "Infos", true);
        setSize(280, 190);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(LauncherFrame.getInstance());
        setContentPane(buildContentPane());
        setVisible(true);

    }

    public JPanel buildContentPane(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(new java.awt.Color(50, 50, 50));

        JLabel info = new JLabel("<html><center><font size=6><b>© Narnia - 2018</b></font></center><br>"
                + "Launcher créer par Dreams.<br>"
                + "Toute copie intégrale ou partielle est interdite sous <br>peine de poursuites.<br>"
                + "<br>Version Launcher : <font color=#31C43F>000001a</font>"
                + "<br>Version Client : <font color=#31C43F>000001a</font>"
                + "<br>Version Serveur : <font color=#31C43F>000001a</font></html>" );
        info.setForeground(Color.white);
        panel.add(info);

        return panel;
    }


}