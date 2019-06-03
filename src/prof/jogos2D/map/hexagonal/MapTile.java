package prof.jogos2D.map.hexagonal;

import java.awt.Graphics2D;
import java.awt.Point;


/** Representa uma c�lula, tile, de um mapa hexagonal
 * 
 * @author F. S�rgio Barbosa
 *
 */
public interface MapTile {

	/** Desenha plano de fundo da c�lula 
	 * @param g ambiente gr�fico onde desenhar
	 * @param x coordenada x onde desenhar
	 * @param y coordenada y onde desenhar
	 */
	void drawBackground(Graphics2D g, int x, int y);
	
	/** Desenha o plano da frente da c�lula 
	 * @param g ambiente gr�fico onde desenhar
	 * @param x coordenada x onde desenhar
	 * @param y coordenada y onde desenhar
	 */
	void drawForeground(Graphics2D g, int x, int y);
	
	/** define a posi��o da c�lula
	 * @param c a posi��o da c�lula, em coordenadas de mapa
	 */
	void setPosition(Point c);
	
	/** retorna a posi��o da c�lula
	 * @return a posi��o da c�lula, em coordenadas de mapa
	 */
	Point getPosition();
	
	/** associada esta c�lula a um dado mapa
	 * @param map mapa ao qual a c�lula ficar� associada
	 */
	void setMap( MapaHexagonal map );
	
	/** retorna o mapa ao qual a c�lula pertence
	 * @return o mapa ao qual a c�lula pertence
	 */
	MapaHexagonal getMap();
}
