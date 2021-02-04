package trabajoEspecial2019;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;

import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;

public class InterfazTeoInfoTPE extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private BufferedImage img;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazTeoInfoTPE frame = new InterfazTeoInfoTPE();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	/**
	 * Create the frame.
	 */
	public InterfazTeoInfoTPE() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(InterfazTeoInfoTPE.class.getResource("/Parte1/appIcon.png")));
		setBackground(new Color(70, 130, 180));
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setTitle("Trabajo Especial - Teoria de la Información");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 300);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(1,1,1,1));
		setContentPane(contentPane);
		Font f = new Font("Arial", Font.BOLD, 15);
		
		InterfazTeoInfoTPE frame = this;
		
		JButton btnCalcular = new JButton("CALCULAR");
		btnCalcular.setMinimumSize(new Dimension(0, 25));
		btnCalcular.setFont(f);
		btnCalcular.setBorder(new EmptyBorder(20, 40, 20, 40));
		btnCalcular.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCalcular.setEnabled(false);
		btnCalcular.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCalcular.setForeground(new Color(255, 255, 255));
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setFont(f);
		lblNewLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFocusable(false);
		lblNewLabel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel.setPreferredSize(new Dimension(200,200));
		lblNewLabel.setMaximumSize(new Dimension(1000,1000));
		lblNewLabel.setMinimumSize(new Dimension(0,0));

		JButton btnAbrirImagen = new JButton("ABRIR IMAGEN");
		btnAbrirImagen.setMinimumSize(new Dimension(0, 25));
		btnAbrirImagen.setFont(f);
		btnAbrirImagen.setForeground(new Color(255, 255, 255));
		btnAbrirImagen.setBackground(new Color(70, 130, 180));
		btnAbrirImagen.setBorder(new EmptyBorder(0, 5, 0, 0));
		btnAbrirImagen.setBorderPainted(false);
		btnAbrirImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e2) {
			System.out.println(e2.getMessage());
		}
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter("Imagen BMP", "bmp"));
		fc.setPreferredSize(new Dimension(1000, 710));
		fc.setDialogTitle("Abrir Imagen");
		// Mostrar el panel para seleccionar archivo y mostrar la imagen seleccionada
		btnAbrirImagen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fc.showOpenDialog(contentPane);
				try {
					if(fc.getSelectedFile() != null){
						if(fc.getSelectedFile().exists() && fc.getSelectedFile().getName().endsWith(".bmp")){
							img = ImageIO.read(fc.getSelectedFile());
							lblNewLabel.setText(null);
							ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth()/5, img.getHeight()/5, ICONIFIED));
							lblNewLabel.setPreferredSize(new Dimension(icon.getIconWidth()+4, icon.getIconHeight()+4));
							lblNewLabel.setIcon(icon);
							frame.setSize(new Dimension(icon.getIconWidth()+20, icon.getIconHeight()+100));
							btnCalcular.setPreferredSize(new Dimension(2*frame.getWidth()/3, 25));
							btnCalcular.setBackground(new Color(70, 130, 180));
							btnCalcular.setEnabled(true);
							frame.setLocationRelativeTo(null);
						}else
							lblNewLabel.setText("El archivo seleccionado no es una Imagen BMP");
					}else{
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
		// Generar el archivo y los histogramas de cada bloque.
		btnCalcular.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
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
				
				Control c = new Control(img);
				c.calcularVectoresProbabilidades();
				cierre.setVisible(true);
				
				c.calcularEntropiasSinMemoria();
				c.calcularEntropiasConMemoria();
				int menor = c.bloqueMenorEntropia(),
						mayor = c.bloqueMayorEntropia(),
						prom = c.bloqueEntropiaPromedio();
				FileWriter fr;
				try {
					fr = new FileWriter(c.crearArchivo("a Entropias.txt"));
					fr.write("Entropía sin memoria de cada bloque" + c.ls + c.ls);
					fr.write(c.mostrarArreglo(c.h_SIN));
					fr.write(c.ls + "Entropía con memoria de cada bloque" + c.ls);
					fr.write(c.mostrarArreglo(c.h_Mem));
					fr.write(c.ls + "El bloque de mayor entropia es el " + mayor + c.ls);
					fr.write("El bloque de menor entropia es el " + menor + c.ls);
					fr.write("El bloque de entropia mas cercana al promedio es el " + prom + c.ls);
					fr.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				double[][] matMayor = c.calcularMatrizPasaje(mayor);
				double[][] matMenor = c.calcularMatrizPasaje(menor);
				try {
					fr = new FileWriter(c.crearArchivo("c Matriz Condicional (Mayor Entropía).txt"));
					fr.write(c.mostrarMatriz(matMayor));
					fr.close();
					
					fr = new FileWriter(c.crearArchivo("c Matriz Condicional (Menor Entropía).txt"));
					fr.write(c.mostrarMatriz(matMenor));
					fr.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				try{
					int tir = 100; double error = 0.000001;
					fr = new FileWriter(c.crearArchivo("d Muestro Computacional.txt"));
					for(int i = 0; i<10; i++){
						fr.write("______________________________Intento "+(i+1)+"___________________________________"+c.ls);
						fr.write("Media obtenida por muestreo computacional, para el bloque de menor entropía:  ");
						fr.write(MotorCalculo.getMedia(tir, error, c.probs.get(menor), matMenor) + c.ls);
						fr.write("Desvío estándar obtenido para el bloque de menor entropía:  ");
						fr.write(MotorCalculo.getDesvio(tir, error, c.probs.get(menor), matMenor) + c.ls);
						fr.write("Media obtenida por muestreo computacional, para el bloque de mayor entropía:  ");
						fr.write(MotorCalculo.getMedia(tir, error, c.probs.get(mayor), matMayor) + c.ls);
						fr.write("Desvío estándar obtenido para el bloque de mayor entropía:  ");
						fr.write(MotorCalculo.getDesvio(tir, error, c.probs.get(mayor), matMayor) + c.ls);
					}
					fr.write(c.ls + "Se hacen varios intentos para intentar hacer una mejor estimación");
					fr.close();
				}catch(IOException e1){
					e1.getMessage();
				}
				
				c.guardarImagen(mostrarHistograma(c.probs.get(menor),
						c.probs.get(prom), c.probs.get(mayor)),
						"b histogramas");
				
				cierre.setTitle("Listo");
				cierre.setSize(600,120);
				lbl.setText("<html><body><center>Los archivos fueron generados en<br>"+c.ruta+"</center></body></html>");
			}
		});
		contentPane.setLayout(new BorderLayout(5,5));

		contentPane.add(lblNewLabel, BorderLayout.NORTH);
		contentPane.add(btnCalcular, BorderLayout.WEST);
		contentPane.add(btnAbrirImagen, BorderLayout.CENTER);
	}
	public BufferedImage mostrarHistograma(double[] probMenor, double[] probProm, double[] probMayor){
	/*	JDialog ventana = new JDialog();
		ventana.setSize(1000, 500);
		ventana.setTitle("HISTOGRAMA");
		ventana.setLocationRelativeTo(null);
		
		XYSeries mayor = new XYSeries("Mayor Entropía");
		XYSeries prom = new XYSeries("Entropía cercana al promedio");
		XYSeries menor = new XYSeries("Menor Entropía");
		for(int i = 0; i < probMenor.length; i++){
			mayor.add(i, probMayor[i]);
			prom.add(i, probProm[i]);
			menor.add(i, probMenor[i]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(mayor);
		dataset.addSeries(prom);
		dataset.addSeries(menor);
		
		JFreeChart chart = ChartFactory.createXYAreaChart("Comparación de los histogramas calculados",
				"Tono de gris", "Frecuencia relativa", dataset, PlotOrientation.VERTICAL,
				true, true, false);
		
		ChartPanel panel = new ChartPanel(chart);
		ventana.getContentPane().add(panel);
		ventana.setVisible(true);
		
		BufferedImage image = new BufferedImage(1000, 500, BufferedImage.TYPE_INT_ARGB);
		panel.paint(image.getGraphics());
//		ventana.setVisible(false);
		
		return image;
	 */ return null;
	}
}
