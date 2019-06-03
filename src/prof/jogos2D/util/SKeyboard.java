package prof.jogos2D.util;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Classe que permite saber quais as teclas que estão premidas, em cada momento.
 * @author F. Sérgio Barbosa
 */
public class SKeyboard {

	// conjunto com as teclas premidas 
	private HashSet<Integer> teclas = new HashSet<Integer>();
	
	/**
	 * Cria um SKeyboard para ler o estado das teclas
	 */ 
	public SKeyboard( ) {				
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
				new KeyEventDispatcher() {
					@Override
					public boolean dispatchKeyEvent(KeyEvent e) {
						if( e.getID() == KeyEvent.KEY_PRESSED )
							teclas.add( e.getKeyCode() );
						else if( e.getID() == KeyEvent.KEY_RELEASED )
							teclas.remove( e.getKeyCode() );
						return false;
					}
				}
		);		
	}
	
	/**
	 * Indica se uma dada tecla está premida
	 * @param keyCode o código da tecla que se pretende ver se está premida
	 * @return true se a tecla está premida, false caso contrário
	 */
	public boolean estaPremida( int keyCode ){
		return teclas.contains( keyCode );
	}
	
	/** indica as teclas premidas
	 * @return uma lista com os códigos das teclas premidas
	 */
	public Collection<Integer> getTeclasPremidas() {
		return Collections.unmodifiableCollection( teclas );
	}
	
	/** indica se há alguma tecla premida 
	 * @return true, se pelo menos uma tecla estiver premida
	 */
	public boolean temTeclaPremida() {
		return teclas.size() > 0;
	}
	
	/** reinicia as teclas. Isto significa que todas as teclas ficam
	 * como não estando pressionadas. Vai obrigar a libertar e voltar a 
	 * pressionar as teclas que estiverem premidas para que corresponda
	 * às ações do teclado. É útil quando se muda de janela ativa
	 * por algum momento. 
	 */
	public void limpaTeclas() {
		teclas.clear();;
	}
}
