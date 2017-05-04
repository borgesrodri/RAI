package modelo;

public class PesoPolisemia {
	private int peso;
	private boolean polisemia;
	public PesoPolisemia(int peso, boolean polisemia){
		this.peso = peso;
		this.polisemia = polisemia;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	public boolean getPolisemia() {
		return polisemia;
	}
	public void setPolisemia(boolean polisemia) {
		this.polisemia = polisemia;
	}
}
