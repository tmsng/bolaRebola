package bolarebola.elemento.obstaculo;

import java.awt.geom.Point2D.Double;

import bolarebola.elemento.Bola;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;

public class Terminador extends ObstaculoDefault {
	
	private Double centro; //Ponto central do terminador
	private int raio;  // raio do terminador
	

	public Terminador(Double c, int r, ComponenteVisual cv) {
		super.setImagem(cv);
		this.centro = c;
		this.raio = r;
	}
	
	/** indica se a bola "bateu" no terminador 
	 */
	public boolean bateu(Bola b) {
		return DetectorColisoes.intersectam( centro,  raio, b.getPosicaoCentro(), b.getRaio() );
	}
	
	/** terminar o jogo
	 * @param bola
	 */
	public void terminar(Bola bola) {
		if(!bateu(bola))
			return;
		
		//Perder o nivel
		getNivel().perdeNivel();
	}

}
