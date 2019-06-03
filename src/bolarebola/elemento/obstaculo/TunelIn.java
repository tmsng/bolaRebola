package bolarebola.elemento.obstaculo;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import bolarebola.elemento.obstaculo.TunelOut;
import bolarebola.elemento.Bola;
import bolarebola.elemento.Nivel;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;

public class TunelIn extends ObstaculoDefault {

	private Point2D.Double centroIn;
	private Point2D.Double centroOut;
	private double raio;
	
	private boolean bolaCaptada = false;
//	private boolean bolaSolta = true;
	
	private Bola bola;
	
	public TunelIn(Double cIn, int r, ComponenteVisual cvIn) {
		super.setImagem(cvIn);
		this.centroIn = cIn;		
	}

	public boolean tocou(Bola b) {
		return DetectorColisoes.intersectam( centroIn,  raio, b.getPosicaoCentro(), b.getRaio() );
	}

	public void entrar(Bola b) {
		if(!tocou(b))
			return;
		
		bolaCaptada = true;
			
		captarBola(b);
		
		temBolaCaptada();
		
		centroOut = TunelOut.getCentroOut();
		
		bola.setPosicaoCentro(centroOut);
		
		cuspir(bola);
		
		bolaCaptada = false;
		
//		bolaSolta = false;
	
	}
	
	public void engoleBola( Bola b ){
		// se tem a bola captada é preciso ver se já acabou a animação de engolir a bola
		
		b.setPosicaoCentro( centroIn );
		
		// se bateu é preciso captar a bola e dar inicio à animação de captura
		ComponenteMultiAnimado imagem = (ComponenteMultiAnimado)getImagem();
		imagem.setAnim(1);
		imagem.setCiclico( false );
	}
	
	/** desenhar o tunelIn
	 */
	public void desenhar(Graphics2D g) {
		super.getImagem().desenhar(g);
		if( bolaCaptada ) {
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
			bola.desenhar( g );			
			g.setTransform( old );
		}
		
//		if( !bolaSolta ) {
//			// se em bola captada é preciso aplicar o efeito especial sobre a bola
//			// o efeito especial é mingar a bola
//			AffineTransform old = g.getTransform();
//			ComponenteMultiAnimado cma = (ComponenteMultiAnimado)getImagem();
//			// escalar a bola para ficar mais pequena
//			double scale = ((cma.totalFrames() + cma.getFrameNum()) / (double)cma.totalFrames());
//			double tx = getImagem().getPosicaoCentro().x;
//			double ty = getImagem().getPosicaoCentro().y;
//			g.translate(tx, ty);
//			g.scale(scale, scale);
//			g.translate(tx, ty);
//			bola.desenhar( g );			
//			g.setTransform( old );
//		}
	}
	
	public boolean temBolaCaptada() {
		return bola != null;
	}
	
	private void captarBola(Bola bolaCaptada) {
		this.bola = bolaCaptada;
		super.getNivel().captarBola( this, Nivel.CAPTOR_TUNEL);
	}
	
	public void cuspir(Bola bola) {
		super.getNivel().libertarBola( this );
	}
}
