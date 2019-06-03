package prof.jogos2D.movimento;

import java.awt.geom.Point2D;

import prof.jogos2D.util.Vector2D;

/** Interface que representa um movimento. Um movimento vai
 * deslocar um ponto ao longo do seu trajeto
 * 
 * @author F. S�rgio BArbosa
 *
 */
public interface Movimento extends Cloneable {

	/** come�ar o movimento, deslocando o ponto p
	 * @param p ponto qeu vai sendo alterado pelo movimento
	 */
	void startMovimento( Point2D.Double p );
	
	/** move o ponto de acordo com a velocidade indicada
	 * @param vel velocidade a aplicar ao movimento
	 */
	void move( double vel );
	
	/** Indica qual o ponto onde o movimento ir� estar ao aplicar-se a 
	 * velocidade indicada. N�o move o ponto.
	 * @param vel velocidade a aplicar ao movimento
	 * @return o ponto onde iria ficar ap�s o movimento, se este fosse aplicado
	 */
	Point2D.Double getNextPoint( double vel );
	
	/** Retorna a posi��o atual do movimento
	 * @return a posi��o atual do movimento
	 */
	Point2D.Double getPosicao();
	
	/** Indica a dire��o atual do movimento
	 * @return a dire��o atual do movimento
	 */
	Vector2D getDirecao();
	
	/** Indica se o movimento chegou ao fim
	 * @return true, se o movimento chegou ao fim
	 */
	boolean estaFim( );
	
	/** Indica qual o �ngulo associado ao movimento 
	 * @return qual o �ngulo associado ao movimento
	 */
	double getAngulo();
	
	/** retorna um clone deste movimento
	 * @return um clone deste movimento
	 */
	Movimento clone();
}