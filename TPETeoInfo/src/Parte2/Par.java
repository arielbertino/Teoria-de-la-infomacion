package Parte2;

public class Par implements Comparable<Par>{
	private	double probabilidad;
	private int tono;
	
	// constructores
	public Par(double prob, int ton){
		this.probabilidad = prob;
		this.tono = ton;
	}

	public Par(Par otroPar){
		this.probabilidad = otroPar.getProbabilidad();
		this.tono = otroPar.getTono();
	}
	
	// getters
	public double getProbabilidad()					{return probabilidad;}
	public int getTono()							{return tono;}
	
	// setters
	public void setProbabilidad(double probabilidad){this.probabilidad = probabilidad;}
	public void setTono(int tono)					{this.tono = tono;}
	
	// metodos varios
	void sumarProbs(Par otroPar){
		this.probabilidad += otroPar.getProbabilidad(); 
	} 
	
	@Override
	public int compareTo(Par otroPar) {
		double resultado = this.getProbabilidad() - otroPar.getProbabilidad();
		if (resultado == 0)		 return 0;
		else if ( resultado > 0) return 1;
			else 				return -1;
	}
	
}
