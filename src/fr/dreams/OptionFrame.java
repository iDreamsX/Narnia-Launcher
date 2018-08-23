package fr.dreams;


import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import static fr.dreams.Launcher.NMC_SAVER;

public class OptionFrame extends JDialog implements MouseListener {

    private static OptionFrame instance;

    private JLabel memoryLabel = new JLabel("<html><font color=#ffffff>RAM allouée</font></html>");
    private JComboBox<AllowedMemory> memoryComboBox = new JComboBox(
            new AllowedMemory[]{AllowedMemory.XMX512M, AllowedMemory.XMX1G, AllowedMemory.XMX2G, AllowedMemory.XMX4G, AllowedMemory.XMX6G}
    );
    private JButton saveButton = new JButton("Valider");

    public static OptionFrame getInstance() {
        if (instance == null)
            instance = new OptionFrame();
        return instance;
    }

    private OptionFrame() {
        super(LauncherFrame.getInstance(), "Options", true);

        setSize(200, 150);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(LauncherFrame.getInstance());
        setLayout(null);


        memoryLabel.setBounds(20, 0, 150, 70);
        add(memoryLabel);

        memoryComboBox.setBounds(110, 23, 70, 25);
        add(memoryComboBox);

        saveButton.setBounds(40, 70, 120, 30);
        saveButton.addMouseListener(this);
        add(saveButton);


        getContentPane().setBackground(new java.awt.Color(50, 50, 50));

    }
    @Override
    public void setVisible(boolean b) {
        if (b) {
            try {
                AllowedMemory am = AllowedMemory.valueOf(NMC_SAVER.get("allowed-memory", "XMX1G"));
                memoryComboBox.setSelectedItem(am);
            } catch (IllegalArgumentException ex) {
                memoryComboBox.setSelectedIndex(1);
            }
        }
        super.setVisible(b);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == saveButton)
        {
            NMC_SAVER.set("allowed-memory", ((AllowedMemory) memoryComboBox.getSelectedItem()).name());
            setVisible(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}