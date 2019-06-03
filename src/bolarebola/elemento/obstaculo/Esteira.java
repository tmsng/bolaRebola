package bolarebola.elemento.obstaculo;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import bolarebola.elemento.Bola;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;
import prof.jogos2D.util.Vector2D;

public class Esteira extends ObstaculoDefault {

	private Point2D.Double inicio; //Ponto inicioal da esteira
	private double vel_x, vel_y; // velocidade em X e Y
	
	private Rectangle r; //Retangulo
	
	
	public Esteira(Double ini, int comp, int alt, double fx, double fy, ComponenteVisual imagem) {
		super.setImagem(imagem);		
		this.inicio = ini;
		this.vel_x = -fx;
		this.vel_y = -fy;
		this.r = new Rectangle((int)inicio.x, (int)inicio.y, comp, alt);
	}
	
	
	public boolean emCima( Bola b ){
		return DetectorColisoes.intersectam(r, b.getPosicaoImagem(), (int)b.getRaio());				
	}

	public void alteraDirecao(Bola b) {
		if(!emCima(b))
			return;
		
		// criar um vetor que aponta a bola ao atrator e normalizar 
		Vector2D mover = new Vector2D( b.getPosicaoCentro(), inicio );
		mover.normalizar();
		
		// aplicar a velocidade à bola
		b.incrementaVel( vel_x, vel_y );
	}
	
	
	
}
