package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import modelado.Curso;
import modelado.CursoEnMarcha;

public class CursoEnMarchaCellRenderer extends JPanel implements ListCellRenderer<CursoEnMarcha>{
	private JLabel CursoLabel;
    private JLabel imageLabel;
    private JLabel textoLabel;
    private JPanel panelCentro;

    public CursoEnMarchaCellRenderer() {
        setForeground(Color.BLUE);
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(10, 10));

        // Inicialización de etiquetas
        CursoLabel = new JLabel();
        CursoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        CursoLabel.setForeground(Color.BLACK);
        textoLabel = new JLabel();
        textoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        textoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        textoLabel.setForeground(Color.BLACK);
        imageLabel = new JLabel();
        imageLabel.setForeground(Color.GREEN);
        imageLabel.setBackground(new Color(240, 240, 240));
        imageLabel.setFont(new Font("Lucida Console", Font.BOLD, 12));

        // Configuración de panelCentro para etiquetas de usuario y texto
        panelCentro = new JPanel();
        panelCentro.setPreferredSize(new Dimension(150, 10));
        panelCentro.setFont(new Font("Lucida Console", Font.BOLD, 12));
        panelCentro.setBorder(new LineBorder(Color.BLUE));
        panelCentro.setForeground(Color.BLACK);
        panelCentro.setBackground(new Color(240, 240, 240));
        panelCentro.setLayout(new BorderLayout());
        panelCentro.add(CursoLabel, BorderLayout.NORTH);
        panelCentro.add(textoLabel, BorderLayout.SOUTH);
        
        add(imageLabel, BorderLayout.WEST);
        add(panelCentro, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends CursoEnMarcha> list, CursoEnMarcha curso, int index,
            boolean isSelected, boolean cellHasFocus) {
        // Set the text
    	textoLabel.setText("Bloque:"+curso.getBloqueActual()+",Pregunta:"+curso.getPreguntaActual());
        
    	// Set user
    	//To do: este get lo deberia hacer a traves del controlador
    	CursoLabel.setText(curso.getNombre());
    	
        // Cargar la imagen del perfil
        try {
            URL imageUrl = null;
            //to do: este get tambien se deberia hacer desde el controlador
            imageUrl = curso.getImagenCurso();
            Image image = ImageIO.read(imageUrl);
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
            imageLabel.setIcon(null); // Default to no image if there was an issue
        }

        // Configuración de colores y borde en función de la selección
        Color borderColor = new Color(0, 255, 0); // Verde
        setBorder(new LineBorder(borderColor, 1)); // Borde verde de 2 píxeles
        
        textoLabel.setFont(new Font("Lucida Console", Font.BOLD, 11));
        CursoLabel.setFont(new Font("Lucida Console", Font.BOLD, 11));
        
        // Cambiar colores en funcion de si esta seleccionado o no
        if (isSelected) {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.BLACK);
            panelCentro.setBackground(Color.LIGHT_GRAY);
            panelCentro.setForeground(Color.BLACK);
            panelCentro.setBorder(new LineBorder(Color.BLUE));
            CursoLabel.setForeground(Color.BLACK);
            textoLabel.setForeground(Color.BLACK);
        } else {
            setBackground(new Color(240, 240, 240));
            setForeground(Color.BLACK);
            panelCentro.setBackground(new Color(240, 240, 240));
            panelCentro.setForeground(Color.black);
            panelCentro.setBorder(new LineBorder(Color.BLUE));
            CursoLabel.setForeground(Color.black);
            textoLabel.setForeground(Color.BLACK);
        }

        return this;
    }
}
