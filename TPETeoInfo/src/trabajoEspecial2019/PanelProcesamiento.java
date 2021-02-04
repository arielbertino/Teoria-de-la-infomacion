package trabajoEspecial2019;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JDialog;

public class PanelProcesamiento extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JButton btnCalcular, btnComprimir;
	private InterfazParte2 padre;

	/**
	 * Create the panel.
	 */
	public PanelProcesamiento(InterfazParte2 owner) {
		this.padre = owner;
		Font f = new Font("Arial", Font.BOLD, 15);
		setBorder(new EmptyBorder(1,1,1,1));
		label = new JLabel();
		label.setFont(f);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFocusable(false);
		label.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setPreferredSize(new Dimension(200,200));
		label.setMaximumSize(new Dimension(1000,1000));
		label.setMinimumSize(new Dimension(0,0));

		btnCalcular = new JButton("CALCULAR DESVIO ESTÁNDAR");
		btnCalcular.setFont(f);
		btnCalcular.setMinimumSize(new Dimension(0, 25));
		btnCalcular.setForeground(new Color(255, 255, 255));
		btnCalcular.setBackground(new Color(70, 130, 180));
		btnCalcular.setBorder(new EmptyBorder(20, 40, 20, 40));
		btnCalcular.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCalcular.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JDialog cierre = new JDialog(padre);
				cierre.setTitle("CARGANDO...");
				cierre.setLocationRelativeTo(null);
				JLabel lbl = new JLabel();
				lbl.setFont(new Font("Arial", Font.BOLD, 15));
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
				int menor = padre.c.bloqueMenorEntropia(),
						mayor = padre.c.bloqueMayorEntropia();
				double[][] matMayor = padre.c.calcularMatrizPasaje(mayor);
				double[][] matMenor = padre.c.calcularMatrizPasaje(menor);
				try{
					int tir = 100; double error = 0.000001;
					fr = new FileWriter(padre.c.crearArchivo("d Desvio Estandar.txt"));
					fr.write("Desvío estándar obtenido para el bloque de menor entropía:  ");
					fr.write(MotorCalculo.getDesvio(tir, error, padre.c.probs.get(menor), matMenor) + padre.c.ls);
					fr.write("Desvío estándar obtenido para el bloque de mayor entropía:  ");
					fr.write(MotorCalculo.getDesvio(tir, error, padre.c.probs.get(mayor), matMayor) + padre.c.ls);
					fr.close();
				}catch(IOException e1){
					e1.getMessage();
				}
				
				long finaliza= System.currentTimeMillis();
				System.out.println(Calendar.getInstance().getTime().toString());
				System.out.println("Tiempo del cálculo de desvio estandar: " + (finaliza - inicio)/1000 +" segundos");
				
				cierre.setTitle("LISTO");
				cierre.setSize(600,120);
				lbl.setText("<html><body><center>Los archivos fueron generados en<br>"+padre.c.ruta+"</center></body></html>");
				
			}
		});
		
		btnComprimir = new JButton("COMPRIMIR");
		btnComprimir.setFont(f);
		btnComprimir.setMinimumSize(new Dimension(0, 25));
		btnComprimir.setForeground(new Color(255, 255, 255));
		btnComprimir.setBackground(new Color(70, 130, 180));
		btnComprimir.setBorder(new EmptyBorder(0, 5, 0, 0));
		btnComprimir.setBorderPainted(false);
		btnComprimir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		
		
		btnComprimir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JDialog cierre = new JDialog(padre);
				cierre.setTitle("CARGANDO...");
				cierre.setLocationRelativeTo(null);
				JLabel lbl = new JLabel();
				lbl.setFont(new Font("Arial", Font.BOLD, 15));
				lbl.setHorizontalTextPosition(SwingConstants.CENTER);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
				lbl.setPreferredSize(new Dimension(400, 60));
				cierre.getContentPane().add(lbl);
				cierre.setResizable(false);
				cierre.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				cierre.setVisible(true);
				
				System.out.println(Calendar.getInstance().getTime().toString());
				long inicio = System.currentTimeMillis();
				padre.c.comprimir(3.8);
				long finaliza= System.currentTimeMillis();
				System.out.println(Calendar.getInstance().getTime().toString());
				System.out.println("Tiempo de compresion: " + (finaliza - inicio)/1000 +" segundos");
				
				cierre.setTitle("LISTO");
				cierre.setSize(600,120);
				lbl.setText("<html><body><center>Los archivos fueron generados en<br>"+padre.c.ruta+"</center></body></html>");
				
			}
		});
		
		setLayout(new BorderLayout(5, 5));
		add(label, BorderLayout.NORTH);
		add(btnCalcular, BorderLayout.WEST);
		add(btnComprimir, BorderLayout.CENTER);
	}
	public void setLabel(JLabel lblNew){
		label.setText(lblNew.getText());
		label.setIcon(lblNew.getIcon());
		label.setPreferredSize(lblNew.getPreferredSize());
	}
	public void setFrame(InterfazParte2 frame){
		super.setFont(frame.getFont());
		label.setFont(frame.getFont());
		btnComprimir.setFont(frame.getFont());
		btnCalcular.setFont(frame.getFont());
		btnCalcular.setSize(frame.getWidth()/2, 25);
		btnComprimir.setSize(frame.getWidth()/2, 25);
	}
}
