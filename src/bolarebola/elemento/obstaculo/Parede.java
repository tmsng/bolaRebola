package bolarebola.elemento.obstaculo;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.*;

import bolarebola.elemento.Bola;
import bolarebola.elemento.Nivel;
import prof.jogos2D.image.*;
import prof.jogos2D.util.DetectorColisoes;
import prof.jogos2D.util.Vector2D;

/** classe que representa uma parede
 * Uma parede � uma linha que impede a bola de a atravessar. 
 * Para definir a linha � preciso dar o ponto inicial e o ponto final da mesma.
 * A linha apenas impede a passagem da bola num sentido, pelo que a ordem
 * dos pontos influencia o comportamento da linha.
 * Por exemplo a linha horizontal (100,100) -> (200,100) apenas impede
 * a bola que vem de baixo de a atravessar e n�o a bola que vem de cima. 
 * Para impedir as bolas que v�m de cima, teria-se de usar
 * os pontos (200,100) -> (100,100).
 */
public class Parede extends ObstaculoDefault{
	
	private Point2D.Double inicio, fim;
	
	// vari�veis para as colis�es: a dire��o da linha, ou seja,
	// para onde a linha aponta e a normal da linha, isto �,
	// um vetor perpendicular � linha para calcular o �ngulo de 
	// rebate da bola
	private Vector2D dir, normal;  // dire��o da linha e normal

	/** Constroi uma parede
	 * @param ini inicio da linha
	 * @param fim fim da linha
	 * @param c aspeto visual da parede
	 */
	public Parede( Point2D.Double ini, Point2D.Double fim, ComponenteVisual c)  {
		super.setImagem(c);
		inicio = ini;
		this.fim = fim;
		dir = new Vector2D( fim.x - ini.x, fim.y - ini.y);
		dir.normalizar();
		normal = dir.getOrtogonalEsquerda();
	}
	
	/**
	 * indica se a bola bateu na parede
	 * @param b a bola
	 * @return true, se a bola bateu na parede 
	 */
	public boolean bateu( Bola b ){
		return DetectorColisoes.intersetam( inicio, fim, b.getPosicaoCentro(), (double) b.getRaio() );
	}
	
	/**
	 * m�todo que processa a interac��o com a bola
	 * @param b a bola
	 */
	public void ricocheteBola( Bola b ){
		// se n�o bateu n�o faz nada
		if( !bateu( b ) )
			return;		
		// ATEN��O: ESTES M�TODOS DE DETEC��O DE COLIS�ES T�M UMA IMPLEMENTA��O B�SICA
		//          funcionam para a maior parte dos casos mas por vezes
		//          falham com alguns resultados visiveis. Tornar estes m�todos
		//          mais eficientes n�o � inten��o deste trabalho
		Point2D.Double cb = b.getPosicaoCentro();
		double r = b.getRaio();

		// ver se bateu numa ponta
		Point2D prox = DetectorColisoes.pontoMaisProximo( inicio, fim, cb );
		if( prox == inicio || prox == fim ) {
			// � porque bateu numa ponta e tem-se de processar isso
			double dist = prox.distance( cb );
			// neste caso a normal � a liga��o entre a ponta e o centro da bola
			Vector2D n = new Vector2D( new Point2D.Double(prox.getX(),prox.getY()), cb );
			n.normalizar();
			double desloc = (r - dist + 0.00000001 );
			b.setPosicaoCentro( new Point2D.Double(  cb.x + desloc*n.x, cb.y + desloc*n.y ));
			double vel = b.getVel();
			b.setVelocidade( vel * n.x, vel*n.y );
			return;
		}
			
		Vector2D db = b.getDirecao().clone();

		// ver de onde veio a bola
		Vector2D vb = new Vector2D(-db.x,-db.y);
		
		Point2D.Double i = new Point2D.Double( inicio.x + normal.x*b.getRaio(),
				                               inicio.y + normal.y*b.getRaio() );
		
		Point2D.Double choque = DetectorColisoes.intersecao(i, dir, cb, vb);//DetectorColisoes.getIntersecao(i, dir, cb, vb );
		
		double prodInterDir = normal.produtoInterno( db );
		Vector2D refletido = new Vector2D( db.x - 2*prodInterDir * normal.x,
				                           db.y - 2*prodInterDir * normal.y );
		
		b.setPosicaoCentro( new Point2D.Double(  choque.x + refletido.x, choque.y + refletido.y ));
		b.setVelocidade( refletido.x,  refletido.y );
	}
}
