package bolarebola.elemento.obstaculo;

import java.awt.geom.Point2D.Double;

import bolarebola.app.NivelReader;
import bolarebola.elemento.Bola;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;

public class VidaExtra extends ObstaculoDefault {
	
	private Double centro; //Ponto central do coracao
	private int raio;  // raio do coracao

	public VidaExtra(Double c, int r, ComponenteVisual cv) {
		super.setImagem(cv);
		this.centro = c;
		this.raio = r;
	}
	
	/** indica se a bola "bateu" no coracao
	 */
	public boolean bateu(Bola b) {
		return DetectorColisoes.intersectam( centro,  raio, b.getPosicaoCentro(), b.getRaio() );
	}

	public void maisVida(Bola bola) {
		if(!bateu(bola))
			return;

		//adicionar 1vida
		getNivel().addVida();
		//remover coracao
		getNivel().removeVidaExtra(this);
	}

}
