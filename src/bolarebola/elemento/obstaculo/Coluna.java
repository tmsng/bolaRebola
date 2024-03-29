package bolarebola.elemento.obstaculo;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import bolarebola.elemento.Bola;
import bolarebola.elemento.Nivel;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;
import prof.jogos2D.util.Vector2D;

/**
 * Representa o obst�culo Coluna
 */
public class Coluna extends ObstaculoDefault{
	
	private Point2D.Double centro;
	private double raio;
	private Bola bolaCaptada;

	/** Cria uma coluna
	 * @param centro centro da coluna
	 * @param raio raio da coluna
	 * @param c imagem que representa a coluna
	 */
	public Coluna( Point2D.Double centro, double raio, ComponenteVisual c )  {
		super.setImagem(c);
		this.centro = centro;
		this.raio = raio; 
	}
	
	/**
	 * Efectua o tratamento que a coluna d� � bola
	 * @param b a bola
	 */
	public void ricocheteBola(Bola b) {
		// ATEN��O: ESTES M�TODOS DE DETEC��O DE COLIS�ES T�M UMA IMPLEMENTA��O B�SICA
		//          funcionam para a maior parte dos casos mas por vezes
		//          falham com alguns resultados visiveis. Tornar estes m�todos
		//          mais eficientes n�o � inten��o deste trabalho
	
		// calcular o angulo entre bola e coluna
		Point2D.Double cb = b.getPosicaoCentro();
		Vector2D vc = new Vector2D( getCentro(), cb );
		
		// se bateu faz ricochete 
		if( bateu( b ) ){
			double dist = vc.getComprimento();
			double somaRaios = b.getRaio() + getRaio();
			vc.normalizar();
			b.setPosicaoCentro( new Point2D.Double(  cb.x + (somaRaios-dist)*vc.x, cb.y + (somaRaios-dist)*vc.y ));
			double vel = b.getVel();
			b.setVelocidade( vel * vc.x, vel*vc.y );
		}
	}

	/** indica se a bola bateu na coluna
	 * @param b a bola a testar
	 * @return true, se a bola bateu
	 */
	public boolean bateu(Bola b) {
		return DetectorColisoes.intersectam( centro,  raio, b.getPosicaoCentro(), b.getRaio() );
	}

	/** retorna o centro da coluna
	 * @return o centro da coluna
	 */
	public Point2D.Double getCentro() {
		return centro;
	}

	/** define o centro da coluna
	 * @param centro novo centro
	 */
	public void setCentro(Point2D.Double centro) {
		this.centro = centro;
	}

	/** devolve o raio da coluna
	 * @return o raio da coluna
	 */
	public double getRaio() {
		return raio;
	}

	/** define o raio da coluna
	 * @param raio o novo raio
	 */
	public void setRaio(double raio) {
		this.raio = raio;
	}
}
