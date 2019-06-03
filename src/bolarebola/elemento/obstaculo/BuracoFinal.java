package bolarebola.elemento.obstaculo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import bolarebola.elemento.Bola;
import bolarebola.elemento.Nivel;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;

/**
 * Representa o buraco final
 */
public class BuracoFinal extends ObstaculoDefault{

	private Point2D.Double centro;
	private double raio;
	private Bola bolaCaptada;

	/**
	 * Cria o buraco final
	 * @param centro centro do buraco
	 * @param raio raio do buraco
	 * @param c componente visual que representa o buraco
	 */
	public BuracoFinal( Point2D.Double centro, double raio, ComponenteMultiAnimado c ){
		super.setImagem(c);
		this.centro = centro;
		this.raio = raio; 
	}
	
	/**
	 * processa a bola a cair no buraco
	 * @param b a bola
	 */
	public void engoleBola( Bola b ){
		// se tem a bola captada é preciso ver se já acabou a animação de engolir a bola
		if( temBolaCaptada() ) {
			b.setPosicaoCentro( getCentro() );
			ComponenteMultiAnimado imagem = (ComponenteMultiAnimado)getImagem();
			if( imagem.numCiclosFeitos() >= 1 ) {				
				// avisa o nível que a bola chegou
				getNivel().chegou();			
			}
		}
		
		// se bateu é preciso captar a bola e dar inicio à animação de captura
		else if( tocou( b ) ) {
			captarBola( b );
			ComponenteMultiAnimado imagem = (ComponenteMultiAnimado)getImagem();
			imagem.setAnim(1);
			imagem.reset();
			imagem.setCiclico( false );
		}
	}

	/** desenhar o buraco
	 */
	public void desenhar(Graphics2D g) {
		super.getImagem().desenhar(g);
		if( temBolaCaptada() ) {
			// se em bola captada é preciso aplicar o efeito especial sobre a bola
			// o efeito especial é mingar a bola
			AffineTransform old = g.getTransform();
			ComponenteMultiAnimado cma = (ComponenteMultiAnimado)getImagem();
			// escalar a bola para ficar mais pequena
			double scale = ((cma.totalFrames() - cma.getFrameNum()) / (double)cma.totalFrames());
			double tx = getImagem().getPosicaoCentro().x;
			double ty = getImagem().getPosicaoCentro().y;
			g.translate(tx, ty);
			g.scale(scale, scale);
			g.translate(-tx, -ty);
			bolaCaptada.desenhar( g );			
			g.setTransform( old );
		}
	}

	public boolean tocou(Bola b) {
		return DetectorColisoes.intersectam( centro,  raio, b.getPosicaoCentro(), b.getRaio() );
	}

	public Point2D.Double getCentro() {
		return centro;
	}

	public void setCentro(Point2D.Double centro) {
		this.centro = centro;
	}

	public double getRaio() {
		return raio;
	}

	public void setRaio(double raio) {
		this.raio = raio;
	}

	public boolean temBolaCaptada() {
		return bolaCaptada != null;
	}

	private void captarBola(Bola bolaCaptada) {
		this.bolaCaptada = bolaCaptada;
		super.getNivel().captarBola( this, Nivel.CAPTOR_BURACO );
	}

}
