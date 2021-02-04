package trabajoEspecial2019;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InterfazParte2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private BufferedImage img;
	protected Font f;
	public Control c;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazParte2 frame = new InterfazParte2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InterfazParte2() {
		setBackground(new Color(70, 130, 180));
		f = new Font("Arial", Font.BOLD, 15);
		setFont(f);
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setTitle("Trabajo Especial - Teoria de la Información");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 327);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		setContentPane(contentPane);

		InterfazParte2 frame = this;
		contentPane.setLayout(new GridLayout(1, 1, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		contentPane.add(tabbedPane);

		JPanel principal = new JPanel();
		tabbedPane.addTab("Inicio", null, principal, null);
		principal.setLayout(new BorderLayout(5, 5));

		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setFont(f);
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFocusable(false);
		lblNewLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setPreferredSize(new Dimension(200, 200));
		lblNewLabel.setMaximumSize(new Dimension(1000, 1000));
		lblNewLabel.setMinimumSize(new Dimension(0, 0));

		JButton btnDescomprimir = new JButton("DESCOMPRIMIR IMAGEN");
		btnDescomprimir.setMinimumSize(new Dimension(0, 25));
		btnDescomprimir.setFont(f);
		btnDescomprimir.setBorder(new EmptyBorder(20, 40, 20, 40));
		btnDescomprimir.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnDescomprimir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDescomprimir.setForeground(new Color(255, 255, 255));

		JButton btnAbrirImagen = new JButton("ABRIR IMAGEN");
		btnAbrirImagen.setFont(f);
		btnAbrirImagen.setMinimumSize(new Dimension(0, 25));
		btnAbrirImagen.setForeground(new Color(255, 255, 255));
		btnAbrirImagen.setBackground(new Color(70, 130, 180));
		btnAbrirImagen.setBorder(new EmptyBorder(0, 5, 0, 0));
		btnAbrirImagen.setBorderPainted(false);
		btnAbrirImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDescomprimir.setBackground(new Color(70, 130, 180));
		
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
				fc.showOpenDialog(contentPane);
				try {
					if (fc.getSelectedFile() != null) {
						if (fc.getSelectedFile().exists() && fc.getSelectedFile().getName().endsWith(".bmp")) {
							img = ImageIO.read(fc.getSelectedFile());
							lblNewLabel.setText(null);
							ImageIcon icon = new ImageIcon(
									img.getScaledInstance(img.getWidth() / 5, img.getHeight() / 5, ICONIFIED));
							lblNewLabel.setPreferredSize(
									new Dimension(icon.getIconWidth() + 4, icon.getIconHeight() + 4));
							lblNewLabel.setIcon(icon);
							frame.setSize(new Dimension(700, icon.getIconHeight() + 150));
							btnAbrirImagen.setText("CAMBIAR IMAGEN");
							c = new Control(img);
							c.calcularVectoresProbabilidades();
							c.calcularEntropiasConMemoria();
							btnDescomprimir.setPreferredSize(new Dimension((frame.getWidth() / 2), 25));
							
							PanelProcesamiento procesamiento = new PanelProcesamiento(frame);
							procesamiento.setFrame(frame);
							procesamiento.setSize(new Dimension(principal.getWidth(), principal.getHeight()));
							procesamiento.setLabel(lblNewLabel);
							
							PanelCanales canales = new PanelCanales(frame);
							procesamiento.setSize(new Dimension(principal.getWidth(), principal.getHeight()));
							
							frame.setLocationRelativeTo(null);
							tabbedPane.addTab("Procesar Imagen", null, procesamiento, null);
							tabbedPane.addTab("Canales", null, canales, null);
							
						} else
							lblNewLabel.setText("El archivo seleccionado no es una Imagen BMP");
					} else {
						lblNewLabel.setIcon(null);
						lblNewLabel.setText("No has seleccinado ningún archivo");
					}
				} catch (IOException e1) {
					String error = e1.getMessage();
					lblNewLabel.setIcon(null);
					lblNewLabel.setText(error);
				}
			}
		});
		JFileChooser folder = new JFileChooser();
		folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		folder.setDialogTitle("Buscar carpeta");
		folder.setPreferredSize(new Dimension(1000, 710));
		btnDescomprimir.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				folder.showOpenDialog(frame);
				File carpeta = folder.getSelectedFile();
				if(carpeta != null){
					if(carpeta.isDirectory() && carpeta.exists()){
						c = new Control();
						String path = carpeta.getAbsolutePath();
						System.out.println(path);
						JDialog cierre = new JDialog(frame,"CARGANDO...");
						cierre.setLocationRelativeTo(null);
						JLabel lbl = new JLabel();
						lbl.setFont(f);
						lbl.setHorizontalTextPosition(SwingConstants.CENTER);
						lbl.setHorizontalAlignment(SwingConstants.CENTER);
						lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
						lbl.setPreferredSize(new Dimension(400, 60));
						cierre.getContentPane().add(lbl);
						cierre.setResizable(false);
						cierre.setDefaultCloseOperation(HIDE_ON_CLOSE);
						cierre.setSize(150,70);
						cierre.setVisible(true);
						long inicio = System.currentTimeMillis();
						System.out.println(Calendar.getInstance().getTime().toString());

						BufferedImage imagen = c.descomprimir(path, 2000, 2500);
						
						lblNewLabel.setText(null);
						ImageIcon icon = new ImageIcon(
								imagen.getScaledInstance(imagen.getWidth() / 5, imagen.getHeight() / 5, ICONIFIED));
						lblNewLabel.setPreferredSize(
								new Dimension(icon.getIconWidth() + 4, icon.getIconHeight() + 4));
						lblNewLabel.setIcon(icon);
						frame.setSize(new Dimension(700, icon.getIconHeight() + 150));
						btnDescomprimir.setPreferredSize(new Dimension((frame.getWidth() / 2), 25));
						frame.setLocationRelativeTo(null);

						cierre.setTitle("Listo");
						cierre.setSize(600,120);
						lbl.setText("<html><body><center>Los archivos fueron generados en<br>"+c.ruta+"</center></body></html>");
		
						System.out.println(Calendar.getInstance().getTime().toString());
						long finaliza= System.currentTimeMillis();
						System.out.println("Tiempo de descompresión: " + (finaliza - inicio)+" milisegundos");
					}
				}
			}
		});
		
		principal.add(lblNewLabel, BorderLayout.NORTH);
		principal.add(btnDescomprimir, BorderLayout.WEST);
		principal.add(btnAbrirImagen, BorderLayout.CENTER);
	}
}