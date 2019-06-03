package bolarebola.elemento.obstaculo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import bolarebola.elemento.Bola;
import bolarebola.elemento.Nivel;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.Vector2D;

/** Um Atrator puxa a bola para si, independentemente de onde ela está.
 * A força com que a puxa depende da distância a que a bola está e da força do atrator. 
 */
public class Atrator {

	private Point2D.Double pos;  // posição do atrator
	private double g;            // faror de atração (força da gravidade)
	private Nivel nivel;
	private Bola bolaCaptada;
	private ComponenteVisual imagem;
	
	/** Cria um atrator.
	 * @param pos posição onde está ao atrator
	 * @param g força de atração
	 * @param imagem imagem representativa do atrator
	 */
	public Atrator(Point2D.Double pos, double g, ComponenteVisual imagem) {
		this.imagem = imagem;
		this.g = g;
		this.pos = pos;
	}

	/** indica se a bola "bateu" no atrator 
	 */
	public boolean bateu(Bola b) {
		return true;
	}

	/** processa a bola a ser puxada pelo atrator
	 */
	public void atraiBola(Bola b) {
		// criar um vetor que aponta a bola ao atrator e normalizar 
		Vector2D puxar = new Vector2D( b.getPosicaoCentro(), pos );
		double dist = puxar.getComprimento();		
		puxar.normalizar();
		
		// calacular a força de atração e mutiplicar isso pelo direção da bola
		double fa = g * 1000/ (dist * dist);
		puxar.escalar( fa );		
		
		// aplicar a atração à bola
		b.incrementaVel( puxar );
		
		// rodar a imagem de modo a ficar apontada para a bola
		getImagem().setAngulo(Math.PI+puxar.getAngulo());
	}

	public Nivel getNivel() {
		return nivel;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;		
	}

	public boolean temBolaCaptada() {
		return bolaCaptada != null;
	}

	public ComponenteVisual getImagem() {
		return imagem;
	}

	public void setImagem(ComponenteMultiAnimado img) {
		this.imagem = img;
	}

	public Point getPosicaoImagem() {
		return imagem.getPosicao();
	}

	public void setPosicaoImagem(Point p) {
		imagem.setPosicao(p);
	}
	
	public void desenhar(Graphics2D g) {
		imagem.desenhar(g);
	}
}
