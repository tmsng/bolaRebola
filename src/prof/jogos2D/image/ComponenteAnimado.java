package prof.jogos2D.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Representa um componente visual que tem uma animação
 * @author F. Sérgio Barbosa
 */
public class ComponenteAnimado extends ComponenteSimples {

	private Image frames[];
	private int   frame = 0;
	private int   nFrames = 0;
	private int   delay = 0;
	private int   actualDelay = 0;
	private int   nCiclos = 0;
	
	public ComponenteAnimado() {
	}

	/**
	 * Cria o componente animado segundo os parâmetros indicados
	 * @param p posição no écran
	 * @param fichImagem ficheiro com a imagem
	 * @param nFrames número de frames na animação
	 * @param delay factor que serve para controlar a animação (quanto maios menor a velocidade da animação)
	 * @throws IOException
	 */
	public ComponenteAnimado(Point p, String fichImagem, int nFrames, int delay ) throws IOException {
		super( p, fichImagem );
		setPosicao( p );		
		BufferedImage img = ImageIO.read( new File( fichImagem ) );
		
		produzirFrames(nFrames, img);
		this.delay = delay;
	}

	/**
	 * Cria o componente animado segundo os parâmetros indicados
	 * @param p posição no écran
	 * @param img imagem do componente
	 * @param nFrames número de frames na animação
	 * @param delay factor que serve para controlar a animação (quanto maior menor a velocidade da animação)
	 */
	public ComponenteAnimado(Point p, BufferedImage img, int nFrames, int delay ) {
		setPosicao( p );
		produzirFrames(nFrames, img);
		this.delay = delay;
	}

	/**
	 * Cria o componente animado segundo os parâmetros indicados
	 * @param p posição no écran
	 * @param img array de imagens com as animações
	 * @param delay factor que serve para controlar a animação (quanto maios menor a velocidade da animação)
	 */
	public ComponenteAnimado(Point p, Image img[], int delay ) {
		setPosicao( p );
		frames = img;		
		this.delay = delay;
		nFrames = img.length;
		super.setSprite( frames[ 0 ] );
	}

	
	// vai produzir as frames apartir da imagem total
	private void produzirFrames(int nFrames, BufferedImage img) {
		this.nFrames = nFrames;
		frames = new Image[ nFrames ];
		int comp = img.getWidth( ) / nFrames;
		int alt = img.getHeight();
		for( int i = 0; i < nFrames; i++ ){
			frames[ i ] = img.getSubimage(i*comp, 0, comp, alt);
		}
		super.setSprite( frames[ frame ] );				
	}

	
	/**
	 * desenha este componente no ambiente gráfico g
	 */	
	public void desenhar(Graphics g) {
		super.desenhar(g);
		proximaFrame();
	}
	

	/**
	 * passa para a próxima frame
	 */
	public void proximaFrame() {
		actualDelay++;
		if( actualDelay >= delay ){
			actualDelay = 0;
			frame++;
			if( frame >= nFrames ) {
				frame = eCiclico()? 0: nFrames-1;
				nCiclos++;
			}
			super.setSprite( frames[ frame ] );
		}
	}
	
	/**
	 * indica quantas vezes já reproduziu as animações
	 * @return o número de vezes que fez a animação completa
	 */
	public int numCiclosFeitos(){
		return nCiclos;
	}
	
	/**
	 * começa a animação numa dada frame
	 * @param f a frame onde deve começar a animação
	 */	
	public void setFrameNum( int f ){
		frame = f;
		super.setSprite( frames[ frame ] );
	}
	
	/**
	 * indica qual a frame que está a ser desenhada
	 * @return a frame que está a ser desenhada
	 */	
	public int getFrameNum(  ){
		return frame;
	}

	/** define o delay a aplicar à animação.
	 * Quanto maior o delay mais lenta a animação
	 * @param d o delay a aplicar.
	 */
	public void setDelay(int d) {
		delay = d;
	}
	
	public void reset() {
		nCiclos = 0;
		setFrameNum( 0 );
	}

	/** indica quantas frames tem a animação deste componente
	 * @return o número de frames da animação
	 */
	public int totalFrames() {		
		return nFrames;
	}
	
	public ComponenteAnimado clone() {
		ComponenteAnimado sp = new ComponenteAnimado( getPosicao() != null? (Point)getPosicao().clone(): null, frames.clone(), delay );
		sp.setAngulo( getAngulo() );
		sp.setCiclico( eCiclico() );
		return sp;
	}
}
