package trabajoEspecial2019;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class MotorCalculo {
	public static double[][] getMatrizPasajeColores(BufferedImage imagen) {
		double[][] matriz = new double[256][256];
		int[] freq = new int[256];
		// Inicializacion
		for (int i = 0; i < 256; i++){
			freq[i] = 0;
			for (int j = 0; j < 256; j++)
				matriz[i][j] = 0;
		}

		// generacion matriz pasaje
		Color color1, color2;
		int pixelAct, pixelSig;

		for (int y = 0; y < imagen.getHeight(); y++) {
			for (int x = 0; x < imagen.getWidth(); x++) {
				pixelAct = imagen.getRGB(x, y);
				color1 = new Color(pixelAct);
				if(x+1 == imagen.getWidth()){ //Estoy en la ultima columna?
					if(y+1 == imagen.getHeight()) //Es la ultima fila tambien?
						pixelSig = imagen.getRGB(x, y); //Si es la ultima fila y columna entonces repito el pixel
					else
						pixelSig = imagen.getRGB(0, y+1); //Si no es la ultima fila me fijo en el primer pixel de la siguiente fila
				}else
					pixelSig = imagen.getRGB(x+1, y); //Caso general
				color2 = new Color(pixelSig);
				matriz[color2.getRed()][color1.getRed()]++;
				freq[color1.getRed()]++;
			}
		}
		
		// generar probs
		for (int x = 0; x < 256; x++){
			for (int y = 0; y < 256; y++)
				matriz[x][y] = (freq[y] != 0 ) ? matriz[x][y] / freq[y] : 0;
		}
		return matriz;
	}

	public static double[] getVectorProbs(BufferedImage imagen) {
		double[] vector = new double[256];
		int tam = imagen.getWidth() * imagen.getHeight();

		// inicializacion
		for (int i = 0; i < 256; i++)
			vector[i] = 0;

		// recorrer imagen grande completa
		for (int y = 0; y < imagen.getHeight(); y++)
			for (int x = 0; x < imagen.getWidth(); x++) {
				int pixel = imagen.getRGB(x, y);
				Color color = new Color(pixel);
				vector[color.getRed()]++;
			}

		// generacion de probs basado en el tamaño total de la inagen grande
		for (int i = 0; i < 256; i++) {
			vector[i] = vector[i] / tam;
		}

		return vector;
	}

	public static double getEntropiaSinMemoria(double[] vector) {
		double entropia = 0;
		for (int i = 0; i < vector.length; i++)
			if(vector[i] != 0)
				entropia -= vector[i] * (Math.log10(vector[i]) / Math.log10(2));

		return entropia;
	}

	public static double getEntropiaCondicional(double[] v, double[][] m) {
		double ent = 0, hColumna = 0;
		for (int j = 0; j < m.length; j++){
			for (int i = 0; i < m.length; i++)
				if(m[i][j] != 0)
					hColumna += (m[i][j] * (Math.log10(m[i][j]) / Math.log10(2)));
			ent += (v[j] * hColumna);
			hColumna= 0;
		}
		return -ent;
	}

	// Random para obtener el primer elemento.
	private static int generarPrimero(double[] probs) {
		double p = Math.random(), acum = 0;
		for(int i=0; i < probs.length; i++){
			acum += probs[i];
			if(p < acum) return i;
		}
		return -1;
	}

	// Genera el segundo simbolo dado la ocurrencia del primero.
	private static int generarSegundo(int col, double[][] m) {
		double p = Math.random();
		double acumulado = 0;
		for (int i = 0; i < m.length; i++) {
			acumulado += m[i][col];
			if (p < acumulado)
				return i;
		}
		return -1;
	}

	private static boolean converge(double pAnt, double pAct, double error) {
		return (Math.abs(pAnt - pAct) < error);
	}

	public static double getMedia(int minTiradas, double error,double[] probs, double[][] matPasaje) {
		double suma = 0.0;
		int tiradas = 0;
		double mediaAnt = -1.0;
		double mediaAct = 0.0;

		int c1 = generarPrimero(probs);
		while (!converge(mediaAnt, mediaAct, error) || (tiradas < minTiradas)) {
			c1 = generarSegundo(c1, matPasaje);
			suma += c1;
			tiradas++;
			mediaAnt = mediaAct;
			mediaAct = suma / tiradas;
		}
		return mediaAct;
	}

	public static double getDesvio(int minTiradas, double error,double[] probs, double[][] matPasaje) {
		double mediaParcial = 0;
		double suma = 0;
		int tiradas = 0;
		double varianzaAnterior = -1;
		double varianza = 0;
		double sumac = 0;
		int c1 = generarPrimero(probs);
		while (!converge(varianzaAnterior, varianza, error) || (tiradas < minTiradas)) {
			c1 = generarSegundo(c1, matPasaje);
			tiradas++;
			sumac += c1;
			mediaParcial = sumac / tiradas;
			suma = suma + Math.pow(c1 - mediaParcial,2);
			varianzaAnterior = varianza;
			varianza = suma / tiradas;
		}
		return Math.sqrt(varianza);
	}
	public static double[][] getCanal(BufferedImage salida, BufferedImage llegada){
		double[][] sol = new  double[256][256];
		int[] vec = new  int[256];
		//inicializar
		for (int i = 0; i < sol.length; i++) {
			for (int j = 0; j < sol.length; j++) {
				sol[i][j] = 0;
			}
			vec[i]=0;
		}
		// obtener la matriz conjunta para canales
		for (int y = 0; y < salida.getHeight(); y++) {
			for (int x = 0; x < salida.getWidth(); x++) {
				Color cMS = new Color(salida.getRGB(x, y));
				Color cML = new Color(llegada.getRGB(x, y));
				int pixelMS = cMS.getRed();
				int pixelML = cML.getRed();
				vec[pixelMS]++;
				sol[pixelML][pixelMS]++;
			}
		}
		// obtener la matriz condicional para canales
		for (int i = 0; i < sol.length; i++) {
			for (int j = 0; j < sol.length; j++) {
				if (vec[j] !=0)
					sol[i][j] /= vec[j];
			}
		}
		
		return sol;
	}

}