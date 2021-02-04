package Parte2;

public class ArbolHuffman {
	private ArbolHuffman izq;
	private ArbolHuffman der;
	private Par dato;

	public ArbolHuffman(ArbolHuffman izq, ArbolHuffman der, Par dato) {
		this.izq = izq;
		this.der = der;
		this.dato = dato;
	}
		
	public ArbolHuffman getIzq()	{return izq;}
	public ArbolHuffman getDer() 	{return der;}
	public double   getProb() 		{return dato.getProbabilidad();}
	public int      getTono() 		{return dato.getTono();}
	
	public boolean esHoja() {
		if (izq==null && der==null)	return true;
		else 			            return false;
	}
	
	public void setIzq(ArbolHuffman izq) 	{this.izq = izq;}
	public void setDer(ArbolHuffman der) 	{this.der = der;}
	public void setDato(Par dato) 	{this.dato = dato;}
	
}
