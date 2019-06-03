package bolarebola.elemento.obstaculo;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import prof.jogos2D.image.ComponenteVisual;

public class TunelOut extends ObstaculoDefault {

	private static Point2D.Double centroOut;
	
	public TunelOut(Double cOut, ComponenteVisual cvOut) {
		super.setImagem(cvOut);
		this.centroOut = cOut;
	}

	public static Point2D.Double getCentroOut() {
		return centroOut;
	}
}
