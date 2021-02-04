package Parte2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RLC {

	
	private int t;
	
	public RLC(int tolerancia){
		// por defecto 0 con lo cual se codifica sin perdida
		this.t = tolerancia;
	}
	
	public ArrayList<Byte> codificarRLC(BufferedImage img){
		ArrayList<Byte> codificacion = new ArrayList<Byte>();
		
		// obtengo el primer color de todos
		Color cAct = new Color(img.getRGB(0, 0));
		int tonoAct = cAct.getRed();
		
		// inicializo las cantidades
		int cantC = 0;
		// recoro toda la imagen
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int tonoSig = new Color(img.getRGB(x, y)).getRed();
				if ((cantC < 255) && (Math.abs(tonoSig - tonoAct) <= t)){
					cantC++;
				} else {
					codificacion.add((byte)(tonoAct & 0x000000FF));
					codificacion.add((byte)(cantC & 0x000000FF));
					cantC = 1;
					tonoAct = tonoSig;
				}
			}
		}
		codificacion.add((byte)(tonoAct & 0x000000FF));
		codificacion.add((byte)(cantC & 0x000000FF));
		return codificacion;
	}
	
	public BufferedImage decodificarRLC(ArrayList<Byte> codificacion, int alto, int ancho){
		BufferedImage reconstruccion = new BufferedImage(alto, ancho, BufferedImage.TYPE_INT_RGB);
		int n = 0, cant = 0, tono = -1;
		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				if( cant == 0){
					tono = Byte.toUnsignedInt(codificacion.get(n++));
					cant = Byte.toUnsignedInt(codificacion.get(n++));
				}
				reconstruccion.setRGB(x, y, new Color(tono, tono, tono).getRGB());
				cant--;
			}
		}
		return reconstruccion;
	}
}
