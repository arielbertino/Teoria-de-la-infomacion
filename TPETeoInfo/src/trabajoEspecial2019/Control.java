package trabajoEspecial2019;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import Parte2.Huffman;
import Parte2.Par;
import Parte2.RLC;

public class Control {
	private int w, h;
	private final Integer SIZE = 500;
	double[] h_SIN;
	double[] h_Mem;
	ArrayList<double[]> probs;
	private BufferedImage img;

	final String ls = System.getProperty("line.separator");
	final String s = System.getProperty("file.separator");
	public final String ruta = System.getProperty("user.home") + s + "Desktop" + s + "Resultados TPE";

	public Control(){}
	
	public Control(BufferedImage image) {
		img = image;
		h = img.getHeight(); // Cantidad de Filas
		w = img.getWidth(); // Cantidad de Columnas
		h_Mem = new double[(w * h) / (SIZE * SIZE)];
		h_SIN = new double[(w * h) / (SIZE * SIZE)];
		probs = new ArrayList<double[]>((w * h) / (SIZE * SIZE));
	}

	public File crearArchivo(String nombre) {
		new File(ruta).mkdirs();
		File f = new File(ruta, nombre);
		try {
			f.createNewFile();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		return f;
	}

	public void guardarImagen(BufferedImage imagen, String nombre) {
		try {
			ImageIO.write(imagen, "png", new File(ruta, nombre + ".bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String mostrarArreglo(double[] a) {
		String pal = "";
		for (int i = 0; i < a.length; i++) {
			pal += String.format("%.2f", a[i]) + " ";
		}
		pal += ls;
		return pal;
	}

	public String mostrarMatriz(double[][] m) {
		String pal = "";
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				pal += String.format("%.2f", m[i][j]) + " ";
			}
			pal += ls;
		}
		return pal;
	}

	public void calcularVectoresProbabilidades() {
		for (int y = 0; y < h; y += SIZE) {
			for (int x = 0; x < w; x += SIZE) {
				BufferedImage bloque = img.getSubimage(x, y, SIZE, SIZE);
				probs.add(MotorCalculo.getVectorProbs(bloque));
			}
		}
	}

	public void calcularEntropiasSinMemoria() {
		int filas = h / SIZE, cols = w / SIZE;
		for (int y = 0; y < filas; y++) {
			for (int x = 0; x < cols; x++) {
				h_SIN[x + cols * y] = MotorCalculo.getEntropiaSinMemoria(probs.get(x + cols * y));
			}
		}
	}

	public void calcularEntropiasConMemoria() {
		int filas = h / SIZE, cols = w / SIZE;
		for (int y = 0; y < filas; y++) {
			for (int x = 0; x < cols; x++) {
				BufferedImage bloque = img.getSubimage(x * SIZE, y * SIZE, SIZE, SIZE);
				double[][] mat_pasaje = MotorCalculo.getMatrizPasajeColores(bloque);
				h_Mem[cols * y + x] = MotorCalculo.getEntropiaCondicional(probs.get(cols * y + x), mat_pasaje);
			}
		}
	}

	public int bloqueMenorEntropia() {
		int menor = 0;
		for (int i = 1; i < h_Mem.length; i++) {
			if (h_Mem[i] < h_Mem[menor])
				menor = i;
		}
		return menor;
	}

	public int bloqueMayorEntropia() {
		int mayor = 0;
		for (int i = 1; i < h_Mem.length; i++) {
			if (h_Mem[i] > h_Mem[mayor])
				mayor = i;
		}
		return mayor;
	}

	public int bloqueEntropiaPromedio() {
		int cerca = 0;
		double prom = 0;
		for (int i = 0; i < h_Mem.length; i++) {
			prom += h_Mem[i];
		}
		prom = prom / h_Mem.length;
		for (int i = 1; i < h_Mem.length; i++) {
			if (Math.abs(h_Mem[i] - prom) < Math.abs(h_Mem[cerca] - prom))
				cerca = i;
		}
		return cerca;
	}

	public double[][] calcularMatrizPasaje(int bloque) {
		int x = Math.floorDiv(bloque, h / SIZE), y = Math.floorMod(bloque, h / SIZE);

		BufferedImage imagen = img.getSubimage(x * SIZE, y * SIZE, SIZE, SIZE);
		return MotorCalculo.getMatrizPasajeColores(imagen);
	}

	public void comprimir(double ht) {
		int filas = h / SIZE, cols = w / SIZE, nBloque = 0;
		BufferedImage subImg;
		FileOutputStream comp;
		ArrayList<Byte> codificacion;
		for (int y = 0; y < filas; y++) {
			for (int x = 0; x < cols; x++) {
				subImg = img.getSubimage(x * SIZE, y * SIZE, SIZE, SIZE);
				try {
					if (ht < h_Mem[nBloque]) {
						comp = new FileOutputStream(crearArchivo("Bloque_" + nBloque + ".huf"));
						ArrayList<Par> arr = new ArrayList<Par>();
						double[] prob = probs.get(nBloque);
						for (int i = 0; i < prob.length; i++) {
							arr.add(new Par(prob[i], i));
						}
						Huffman h = new Huffman(arr);
						codificacion = h.codificar(subImg);
//						System.out.println("Tamaño de la codificacion: "+codificacion.size());
						for (int i = 0; i < prob.length; i++) {
							int p = (int) (prob[i] * (SIZE * SIZE));
							comp.write(intToByteArray(p));
						}
					} else {
						comp = new FileOutputStream(crearArchivo("Bloque_" + nBloque + ".rlc"));
						RLC r = new RLC(0);
						codificacion = r.codificarRLC(subImg);
					}
					byte[] size = intToByteArray(SIZE);
					comp.write(size);
					comp.write(size);

					for (Byte b : codificacion) {
						comp.write(b.byteValue());
					}

					comp.close();
					++nBloque;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

	byte[] intToByteArray(int data) {

		byte[] result = new byte[4];

		result[0] = (byte) ((data & 0xFF000000) >> 24);
		result[1] = (byte) ((data & 0x00FF0000) >> 16);
		result[2] = (byte) ((data & 0x0000FF00) >> 8);
		result[3] = (byte) ((data & 0x000000FF) >> 0);

		return result;
	}

	private int byteArrayToInt(byte[] array) {
		int num = 0;
		int exp;
		for (int i = array.length - 1; i >= 0; i--) {
			exp = array.length - 1 - i;
			num += Byte.toUnsignedInt(array[i]) * Math.pow(256, exp);
		}
		return num;
	}

	public BufferedImage descomprimir(String path, int ancho, int alto) {
		// CREAR IMAGEN DE SALIDA
		BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = salida.createGraphics();
		byte[] stream;
		File f;
		RLC rlc = new RLC(0);
		BufferedImage bloque = null;
		byte[] intBuffer = new byte[4];
		int width = 0, height = 0;
		int x = 0, y = 0;
		for (int i = 0; i < 20; i++) {
			ArrayList<Byte> codificacion = new ArrayList<Byte>();
			try {
				f = new File(ruta, "Bloque_" + i + ".rlc");
				if (f.exists()) {
					stream = Files.readAllBytes(f.toPath());
					int j = 0;
					intBuffer[0] = stream[j++];
					intBuffer[1] = stream[j++];
					intBuffer[2] = stream[j++];
					intBuffer[3] = stream[j++];
					width = byteArrayToInt(intBuffer);

					intBuffer[0] = stream[j++];
					intBuffer[1] = stream[j++];
					intBuffer[2] = stream[j++];
					intBuffer[3] = stream[j++];
					height = byteArrayToInt(intBuffer);

					while(j < stream.length) {
						codificacion.add(stream[j++]);
					}

					bloque = rlc.decodificarRLC(codificacion, height, width);
				} else {
					
					f = new File(ruta, "Bloque_" + i + ".huf");
					if (f.exists()) {
						ArrayList<Par> freq = new ArrayList<Par>();
						stream = Files.readAllBytes(f.toPath());
						int j = 0, tono = 0;
						// LEER FRECUENCIAS DESDE EL ENCABEZADO
						for(j = 0; j < 1024; j+=4) {
							intBuffer[0] = stream[j];
							intBuffer[1] = stream[j+1];
							intBuffer[2] = stream[j+2];
							intBuffer[3] = stream[j+3];
							freq.add(new Par(byteArrayToInt(intBuffer),tono++));
						}

						Huffman huf = new Huffman(freq);
//						huf.mostrarMapeado();
						// LEER EL ANCHO Y EL ALTO
						intBuffer[0] = stream[j++];
						intBuffer[1] = stream[j++];
						intBuffer[2] = stream[j++];
						intBuffer[3] = stream[j++];
						width = byteArrayToInt(intBuffer);

						intBuffer[0] = stream[j++];
						intBuffer[1] = stream[j++];
						intBuffer[2] = stream[j++];
						intBuffer[3] = stream[j++];
						height = byteArrayToInt(intBuffer);

						while (j< stream.length) {
							codificacion.add(stream[j++]);
						}

						bloque = huf.decodificar(codificacion, height, width, height * width);
					}
				}
				g.drawImage(bloque, null, x * width, y * height);
				x++;
				if (x == 4) {
					x = 0;
					y++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			ImageIO.write(salida, "png", new File(path, "imagen descomprimida.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salida;
	}

	public double getRuido(BufferedImage llegada) {
		double[][] matCond = MotorCalculo.getCanal(img, llegada);
		double[] vecProb = MotorCalculo.getVectorProbs(img);
		return MotorCalculo.getEntropiaCondicional(vecProb, matCond);
	}

}
