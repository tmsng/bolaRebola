package bolarebola.elemento.obstaculo;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import bolarebola.elemento.Bola;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;

public class Retardador extends ObstaculoDefault {
	
	private Point2D.Double inicio; //Ponto inicioal do retardador
	private double vel_x, vel_y; // velocidade em X e Y
	
	private boolean lento = false;
	
	private Rectangle r; //Retangulo

	public Retardador(Double ini, int comp, int alt, double fx, double fy, ComponenteVisual imagem) {
		super.setImagem(imagem);		
		this.inicio = ini;
		this.vel_x = fx;
		this.vel_y = fy;
		this.r = new Rectangle((int)inicio.x, (int)inicio.y, comp, alt);
	}

	public boolean emCima( Bola b ){
		return DetectorColisoes.intersectam(r, b.getPosicaoImagem(), (int)b.getRaio());				
	}
	
	public void retardar(Bola b) {
		if(!emCima(b)) {
			lento = false;
			return;
		}
		
		lento = true;
		
		// aplicar a velocidade à bola
		b.aceleraVel(vel_x, vel_y);
	}

	public void desenhar(Graphics2D g) {
		super.getImagem().desenhar(g);

		ComponenteMultiAnimado imagem = (ComponenteMultiAnimado)getImagem();
		if(!lento) {
			imagem.setAnim(0);
			imagem.setCiclico( true );
		}
		else {
			imagem.setAnim(1);
			imagem.setCiclico(true);
		}
	}
}
