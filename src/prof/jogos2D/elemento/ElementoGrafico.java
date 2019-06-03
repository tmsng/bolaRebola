package prof.jogos2D.elemento;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import prof.jogos2D.image.ComponenteVisual;

/**
 * Interface que define o comportamento que todos os elementos gr�ficos ter�o de implememtar
 * Assume-se que cada elemento gr�fico ter� uma imagem que o reprezenta e uma posi��o no �cran
 * 
 * @author F. S�rgio Barbosa
  */
public interface ElementoGrafico {

	/** desenho o elemento gr�fico 
	 * @param g amiente gr�fico onde desenhar o elemento gr�fico
	 */
	public void desenhar( Graphics2D g );
	
	/** atualiza o elemento gr�fico, pois este poder� ter algum comportamento
	 */
	public void atualizar( );

	/** retorna o elemento visual associado ao elemento gr�fico
	 * @return o elemento visual associado ao elemento gr�fico
	 */
	public ComponenteVisual getImage();
	
	/** altera a imagem associada ao elemento gr�fico
	 * @param image a nova imagem a associar ao elemento gr�fico
	 */
	public void setImage( ComponenteVisual image );
	
	/** define a posi��o do elemento gr�fico
	 * @param p a nova posi��o do elemento gr�fico
	 */
	public void setPosicao( Point p );
	
	/** retorna a posi��o onde est� 
	 * @return a posi��o onde est�
	 */
	public Point getPosicao( );
	
	/** retorna a �rea que este elemento gr�fico ocupa
	 * @return a �rea que este elemento gr�fico ocupa
	 */
	public Rectangle getBounds();
}
