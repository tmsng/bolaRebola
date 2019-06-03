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
 * Representa o obstáculo Coluna
 */
public class Coluna {
	
	private Point2D.Double centro;
	private double raio;
	private Nivel nivel;
	private Bola bolaCaptada;
	private ComponenteVisual imagem;

	/** Cria uma coluna
	 * @param centro centro da coluna
	 * @param raio raio da coluna
	 * @param c imagem que representa a coluna
	 */
	public Coluna( Point2D.Double centro, double raio, ComponenteVisual c )  {
		imagem = c;
		this.centro = centro;
		this.raio = raio; 
	}
	
	/**
	 * Efectua o tratamento que a coluna dá à bola
	 * @param b a bola
	 */
	public void ricocheteBola(Bola b) {
		// ATENÇÃO: ESTES MÉTODOS DE DETECÇÃO DE COLISÕES TÊM UMA IMPLEMENTAÇÃO BÁSICA
		//          funcionam para a maior parte dos casos mas por vezes
		//          falham com alguns resultados visiveis. Tornar estes métodos
		//          mais eficientes não é intenção deste trabalho
	
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

	/** devolve o nível onde a coluna está inserida
	 * @return o nível onde a coluna está inserida
	 */
	public Nivel getNivel() {
		return nivel;
	}

	/** define o nível onde a coluna está inserida
	 * @param nivel o novel onde a coluna está inserida
	 */
	public void setNivel(Nivel nivel) {
		this.nivel = nivel;		
	}

	/** testa se tem bola captada
	 * @return true, se tem bola captada
	 */
	private boolean temBolaCaptada() {
		return bolaCaptada != null;
	}

	/** capta a bola
	 * @param bolaCaptada a bola a captar
	 */
	private void captarBola(Bola bolaCaptada) {
		this.bolaCaptada = bolaCaptada;
		nivel.captarBola( this, Nivel.CAPTOR_COLUNA );
	}

	/** liberta a bola
	 */
	private void libertarBola() {
		this.bolaCaptada = null;
		nivel.libertarBola( this );
	}

	/** devolve a imagem que representa a coluna
	 * @return a imagem que representa a coluna
	 */
	public ComponenteVisual getImagem() {
		return imagem;
	}

	/** define a imagem que representa a coluna
	 * @param img a nova imagem
	 */
	public void setImagem(ComponenteMultiAnimado img) {
		this.imagem = img;
	}

	/** desenha a coluna
	 * @param g onde desenhar
	 */
	public void desenhar(Graphics2D g) {
		imagem.desenhar(g);
	}
}
