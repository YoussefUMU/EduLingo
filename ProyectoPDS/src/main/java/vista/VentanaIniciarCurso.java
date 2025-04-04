package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ButtonGroup;

public class VentanaIniciarCurso {
    private JFrame panelIncial;
    
    public VentanaIniciarCurso() {
        initialize();
    }
    
    public void initialize() {
        panelIncial = new JFrame();
        panelIncial.setBackground(new Color(240, 240, 240));
        panelIncial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panelIncial.setLocationByPlatform(true);
        panelIncial.setSize(new Dimension(600, 600));
        panelIncial.getContentPane().setBackground(new Color(240, 240, 240));
        panelIncial.getContentPane().setLayout(new BorderLayout(0, 0));
        
        JPanel panelNorte = new JPanel();
        panelIncial.getContentPane().add(panelNorte, BorderLayout.NORTH);
        
        JLabel lblNewLabel = new JLabel("Último paso antes de empezar");
        lblNewLabel.setForeground(Color.BLUE);
        lblNewLabel.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 20));
        panelNorte.add(lblNewLabel);
        
        JPanel panel = new JPanel();
        panelIncial.getContentPane().add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{243, 97, 0};
        gbl_panel.rowHeights = new int[]{23, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
        JLabel lblNewLabel_1 = new JLabel("Seleccione su estrategia:");
        lblNewLabel_1.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 15));
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 2;
        panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
        
        JCheckBox chckbxNewCheckBox = new JCheckBox("Secuencial");
        chckbxNewCheckBox.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
        GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
        gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxNewCheckBox.anchor = GridBagConstraints.NORTHWEST;
        gbc_chckbxNewCheckBox.gridx = 1;
        gbc_chckbxNewCheckBox.gridy = 3;
        panel.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
        
        JTextPane txtpnConLaEstrategia = new JTextPane();
        txtpnConLaEstrategia.setText("Con la estrategia secuencial, estudiarías en el orden en que las lecciones están dispuestas.");
        txtpnConLaEstrategia.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 12));
        txtpnConLaEstrategia.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc_txtpnConLaEstrategia = new GridBagConstraints();
        gbc_txtpnConLaEstrategia.insets = new Insets(0, 0, 5, 0);
        gbc_txtpnConLaEstrategia.fill = GridBagConstraints.BOTH;
        gbc_txtpnConLaEstrategia.gridx = 1;
        gbc_txtpnConLaEstrategia.gridy = 4;
        panel.add(txtpnConLaEstrategia, gbc_txtpnConLaEstrategia);
        
        JCheckBox chckbxNewCheckBox_1 = new JCheckBox("Repetición espaciada");
        chckbxNewCheckBox_1.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
        GridBagConstraints gbc_chckbxNewCheckBox_1 = new GridBagConstraints();
        gbc_chckbxNewCheckBox_1.anchor = GridBagConstraints.WEST;
        gbc_chckbxNewCheckBox_1.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxNewCheckBox_1.gridx = 1;
        gbc_chckbxNewCheckBox_1.gridy = 5;
        panel.add(chckbxNewCheckBox_1, gbc_chckbxNewCheckBox_1);
        
        JTextPane txtpnConEstaTcnica = new JTextPane();
        txtpnConEstaTcnica.setPreferredSize(new Dimension(10, 75));
        txtpnConEstaTcnica.setBackground(new Color(240, 240, 240));
        txtpnConEstaTcnica.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 12));
        txtpnConEstaTcnica.setText("Con esta técnica, en lugar de estudiar todo de una vez, repasarías la misma información en intervalos de tiempo, aumentando el espacio entre cada repaso. Esto ayuda a consolidar la memoria a largo plazo.");
        GridBagConstraints gbc_txtpnConEstaTcnica = new GridBagConstraints();
        gbc_txtpnConEstaTcnica.insets = new Insets(0, 0, 5, 0);
        gbc_txtpnConEstaTcnica.fill = GridBagConstraints.BOTH;
        gbc_txtpnConEstaTcnica.gridx = 1;
        gbc_txtpnConEstaTcnica.gridy = 6;
        panel.add(txtpnConEstaTcnica, gbc_txtpnConEstaTcnica);
        
        JCheckBox chckbxNewCheckBox_2 = new JCheckBox("Aleatoria");
        chckbxNewCheckBox_2.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 15));
        GridBagConstraints gbc_chckbxNewCheckBox_2 = new GridBagConstraints();
        gbc_chckbxNewCheckBox_2.insets = new Insets(0, 0, 5, 0);
        gbc_chckbxNewCheckBox_2.anchor = GridBagConstraints.WEST;
        gbc_chckbxNewCheckBox_2.gridx = 1;
        gbc_chckbxNewCheckBox_2.gridy = 7;
        panel.add(chckbxNewCheckBox_2, gbc_chckbxNewCheckBox_2);
        
        JTextPane txtpnConEstaEstrategia = new JTextPane();
        txtpnConEstaEstrategia.setText("Con esta estrategia, el orden en que estudias los elementos no sigue una secuencia fija. En lugar de estudiar un tema de principio a fin, podrías mezclar las lecciones o incluso cambiar entre diferentes temas en el mismo día. Esto puede ayudarte a desarrollar flexibilidad mental y a evitar memorizar solo en un patrón.");
        txtpnConEstaEstrategia.setFont(new Font("Verdana Pro Cond", Font.PLAIN, 12));
        txtpnConEstaEstrategia.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc_txtpnConEstaEstrategia = new GridBagConstraints();
        gbc_txtpnConEstaEstrategia.fill = GridBagConstraints.BOTH;
        gbc_txtpnConEstaEstrategia.gridx = 1;
        gbc_txtpnConEstaEstrategia.gridy = 8;
        panel.add(txtpnConEstaEstrategia, gbc_txtpnConEstaEstrategia);
        
        // Crear el ButtonGroup
        ButtonGroup group = new ButtonGroup();
        group.add(chckbxNewCheckBox);
        group.add(chckbxNewCheckBox_1);
        group.add(chckbxNewCheckBox_2);
        
        JPanel botonera = new JPanel();
        panelIncial.getContentPane().add(botonera, BorderLayout.SOUTH);
        
        JButton btnNewButton = new JButton("Iniciar");
        btnNewButton.setPreferredSize(new Dimension(100, 50));
        btnNewButton.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 15));
        botonera.add(btnNewButton);
        
        Component horizontalStrut = Box.createHorizontalStrut(20);
        botonera.add(horizontalStrut);
        
        JButton btnNewButton_1 = new JButton("Cancelar");
        btnNewButton_1.setPreferredSize(new Dimension(100, 50));
        btnNewButton_1.setFont(new Font("Verdana Pro Cond Black", Font.PLAIN, 15));
        botonera.add(btnNewButton_1);
    }
}
