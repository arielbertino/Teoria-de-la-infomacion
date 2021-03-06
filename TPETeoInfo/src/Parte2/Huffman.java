package Parte2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Huffman {
	public static final int tonoInvalido=-1;
	public static final int byteLong= 8;
	
	private ArrayList<Par> arreglo;
	private ArbolHuffman   arbol;
	HashMap<Integer, StringBuffer> mapeado = new HashMap<Integer, StringBuffer>();
	
	public Huffman(ArrayList<Par> frecuencias){
		arreglo = frecuencias;
		arbol = generarArbol();
	}
	public void ordenar() {Collections.sort(arreglo);}
	
	public void eliminarCeros() {
		while (arreglo.get(0).getProbabilidad() == 0.0) {
			arreglo.remove(0);
		}
	}

	private void insertarOrdenado(ArbolHuffman tree, ArrayList<ArbolHuffman> list) {
		int i =0;
		while (  (i < list.size()) && (list.get(i).getProb() < tree.getProb())  )
			i++;
		list.add(i, tree);
	}

	private ArbolHuffman generarArbol() {
		ArrayList<ArbolHuffman> lista = new ArrayList<ArbolHuffman>();
		ordenar();
		eliminarCeros();

		for  (Par p : arreglo) {
			lista.add(new ArbolHuffman(null, null, p));
		}

		while ( lista.size()>1 ){
			// borra los menos probables
			ArbolHuffman par0= lista.remove(0);
			ArbolHuffman par1= lista.remove(0);
			// al primer punto le suma el segundo
			ArbolHuffman suma = new ArbolHuffman(par0, par1, new Par(par0.getProb()+par1.getProb(), tonoInvalido));
			// insertar nuevo punto en la lista anterior
			insertarOrdenado(suma, lista);
		}
		return lista.remove(0);
	}

	private StringBuffer getCodigo(int tono){//:)
		return mapeado.get(tono);
	}


	// metodo codificador
	public ArrayList<Byte> codificar(BufferedImage img) {
		generarHash(arbol, new StringBuffer());
//		StringBuffer codif = new StringBuffer();
//		for (int y = 0; y < img.getHeight(); y++) {
//			for (int x = 0; x < img.getWidth(); x++) {
//				Color c = new Color(img.getRGB(x, y));
//				codif.append(getCodigo(c.getRed()));
//			}
//		}
		// inicio de seccion de codificacion a nivel bit
		ArrayList<Byte> resultado = new ArrayList<Byte>();
		byte buffer = 0;
		int bufferPos = 0;
		StringBuffer c = new StringBuffer();
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				Color color = new Color(img.getRGB(x, y));
				c = getCodigo(color.getBlue());
				// La operación de corrimiento pone un '0'
				int i = 0;
				while (i < c.length()) {
					buffer = (byte) (buffer << 1);
					bufferPos++;
					if (c.charAt(i) == '1') {
						buffer = (byte) (buffer | 1);
					}
					if (bufferPos == byteLong) {
						resultado.add(buffer);
						buffer = 0;
						bufferPos = 0;
					}
					i++;
				}
			}
		}
		if ((bufferPos < byteLong) && (bufferPos != 0)) {
			buffer = (byte) (buffer << (byteLong - bufferPos));
			resultado.add(buffer);
		}
		return resultado;
	}

	// metodo decodificador
	public BufferedImage decodificar(ArrayList<Byte> codificacion, int alto, int ancho, int longitudSecuencia) {
		BufferedImage reconstruccion = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		int i = 0, tono = -1;
		ArrayList<Integer> tonos = getTonos(codificacion, longitudSecuencia);
		System.out.println(tonos.size());
		for (int y = 0; y < alto; y++) {
			for (int x = 0; x < ancho; x++) {
				if(i < codificacion.size()){
					tono = tonos.get(i);
					i++;
				}
				reconstruccion.setRGB(x, y, new Color(tono, tono, tono).getRGB());
			}
		}
		return reconstruccion;
	}

	private ArrayList<Integer> getTonos(ArrayList<Byte> codificacion, int longitudSecuencia) {
		ArrayList<Integer> tonos = new ArrayList<Integer>();
		ArbolHuffman aux = arbol;
		byte mask = (byte)0b10000000;
		int bytePos = 0, cantSimbolosDecod = 0;
		int i = 0;
//		for (int i = 0; i < codificacion.size(); i++) {
		while(cantSimbolosDecod < longitudSecuencia && i < codificacion.size()){
			byte b = codificacion.get(i);
			bytePos = 0;
			while(bytePos < byteLong){
				if(aux.esHoja()){
					tonos.add(aux.getTono());

					cantSimbolosDecod++;
					if(cantSimbolosDecod == longitudSecuencia)
						break;
					aux = arbol;
				}
				if((byte)(mask & b) == mask)
					aux = aux.getDer();
				else
					aux = aux.getIzq();
				bytePos++;
				b = (byte)( b<<1 ); 
			}
			i++;
		}
		return tonos;
	}

	void generarHash(ArbolHuffman arb, StringBuffer c){
		// generacion de estructura auxiliar con referencia a los tonos
		if (arb.esHoja())
			mapeado.put(arb.getTono(), new StringBuffer(c));
		else{
			generarHash(arb.getIzq(), c.append('0'));
			c.deleteCharAt(c.length()-1);
			generarHash(arb.getDer(), c.append('1'));
			c.deleteCharAt(c.length()-1);
		}
	}

	public String mostrarMapeado(){
		return mapeado.toString();
	}
}
