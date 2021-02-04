package trabajoEspecial2019;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class PanelCanales extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JButton btnCalcular, btnAbrirImagen;
	private InterfazParte2 padre;
	BufferedImage img;
	/**
	 * Create the panel.
	 */
	public PanelCanales(InterfazParte2 owner) {
		this.padre = owner;
		
		Font font = new Font("Arial", Font.BOLD, 15);
		setLayout(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(1,1,1,1));

		btnCalcular = new JButton("CALCULAR RUIDO DEL CANAL");
		btnCalcular.setMaximumSize(new Dimension(padre.getWidth()/2, 25));
		btnCalcular.setMinimumSize(new Dimension(padre.getWidth()/2, 25));
		btnCalcular.setPreferredSize(new Dimension(padre.getWidth()/2, 25));
		btnCalcular.setBorder(new EmptyBorder(20, 40, 20, 40));
		btnCalcular.setFont(font);
		btnCalcular.setEnabled(false);
		btnCalcular.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCalcular.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JDialog cierre = new JDialog(padre);
				cierre.setTitle("CARGANDO...");
				cierre.setLocationRelativeTo(null);
				JLabel lbl = new JLabel();
				cierre.setFont(font);
				lbl.setHorizontalTextPosition(SwingConstants.CENTER);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
				lbl.setPreferredSize(new Dimension(400, 60));
				cierre.getContentPane().add(lbl);
				cierre.setResizable(false);
				cierre.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				cierre.setVisible(true);
				
				System.out.println(Calendar.getInstance().getTime().toString());
				
				FileWriter fr;
				long inicio = System.currentTimeMillis();
				
				try{
					fr = new FileWriter(padre.c.crearArchivo("ruido del canal.txt"));
					double ruido = padre.c.getRuido(img);
					fr.write(Double.toString(ruido));
					label.setIcon(null);
					label.setText("El ruido del canal es: "+ruido);
					fr.close();
				}catch(IOException e1){
					e1.getMessage();
				}
				
				long finaliza= System.currentTimeMillis();
				System.out.println(Calendar.getInstance().getTime().toString());
				System.out.println("Tiempo del cálculo de desvio estandar: " + (finaliza - inicio)/1000 +" segundos");
				
				cierre.setTitle("LISTO");
				cierre.setSize(600,120);
				lbl.setFont(font);
				lbl.setText("<html><body><center>Los archivos fueron generados en<br>"+padre.c.ruta+"</center></body></html>");
				
			}
		});

		btnAbrirImagen = new JButton("ABRIR IMAGEN DE LLEGADA");
		btnAbrirImagen.setFont(font);
		btnAbrirImagen.setMaximumSize(new Dimension(getWidth()/2, 25));
		btnAbrirImagen.setMinimumSize(new Dimension(0, 25));
		btnAbrirImagen.setPreferredSize(new Dimension(getWidth()/2, 25));
		btnAbrirImagen.setForeground(new Color(255, 255, 255));
		btnAbrirImagen.setBackground(new Color(70, 130, 180));
		btnAbrirImagen.setBorder(new EmptyBorder(0, 5, 0, 0));
		btnAbrirImagen.setBorderPainted(false);
		btnAbrirImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		// Mostrar el panel para seleccionar archivo y mostrar la imagen
		// seleccionada
//		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//				| UnsupportedLookAndFeelException e2) {
//			System.out.println(e2.getMessage());
//		}
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Imagen BMP", "bmp"));
		fc.setPreferredSize(new Dimension(1000, 710));
		fc.setDialogTitle("Abrir Imagen");
		btnAbrirImagen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fc.showOpenDialog(padre);
				try {
					if (fc.getSelectedFile() != null) {
						if (fc.getSelectedFile().exists() && fc.getSelectedFile().getName().endsWith(".bmp")) {
							img = ImageIO.read(fc.getSelectedFile());
							label.setText(null);
							ImageIcon icon = new ImageIcon(
									img.getScaledInstance(img.getWidth() / 5, img.getHeight() / 5, JFrame.ICONIFIED));
							label.setPreferredSize(
									new Dimension(icon.getIconWidth() + 4, icon.getIconHeight() + 4));
							label.setIcon(icon);
							btnAbrirImagen.setText("CAMBIAR IMAGEN");
							btnCalcular.setPreferredSize(new Dimension((padre.getWidth() / 2), 25));
							btnCalcular.setForeground(new Color(255, 255, 255));
							btnCalcular.setBackground(new Color(70, 130, 180));
							btnCalcular.setEnabled(true);
						} else
							label.setText("El archivo seleccionado no es una Imagen BMP");
					} else {
						label.setIcon(null);
						label.setText("No has seleccinado ningún archivo");
					}
				} catch (IOException e1) {
					String error = e1.getMessage();
					label.setIcon(null);
					label.setText(error);
				}
			}
		});

		label = new JLabel("DEBE ELEGIR LA IMAGEN DE LLEGADA");
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFocusable(false);
		label.setFont(font);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setPreferredSize(new Dimension(200, padre.getHeight()-150));
		label.setMaximumSize(new Dimension(1000,1000));
		label.setMinimumSize(new Dimension(0,0));
		
		add(label, BorderLayout.NORTH);
		add(btnCalcular, BorderLayout.WEST);
		add(btnAbrirImagen, BorderLayout.CENTER);
	}

	public void setFrame(InterfazParte2 frame){
		super.setFont(frame.getFont());
		label.setFont(frame.getFont());
		btnAbrirImagen.setFont(frame.getFont());
		btnCalcular.setFont(frame.getFont());
		btnCalcular.setSize(frame.getWidth()/2, 25);
		btnAbrirImagen.setSize(frame.getWidth()/2, 25);
	}
}

