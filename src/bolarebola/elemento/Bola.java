package bolarebola.elemento;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.util.Vector2D;


/**
 * Representa a bola do jogo
 */
public class Bola {

	private double max_veloc = 8; 	 // indica a velocidade m�xima	
	private ComponenteAnimado image; // imagem visual
	private Vector2D direcao;        // para onde se est� a dirigir a bola (vetor normalizado)  
	private Point2D.Double posicao;  // posi��o atual
	private double raio;             
	
	/**
	 * cria a bola 
	 * @param p posi��o da bola
	 * @param raio raio da bola 
	 * @param img componente visual que representa a bola  
	 */
	public Bola( Point p, double raio, ComponenteAnimado img ) {
		image = img;
		image.setPosicaoCentro(p);
		posicao = new Point2D.Double( p.x, p.y );
		direcao = new Vector2D(0,0);
		this.raio = raio;
	}

	/**
	 * incrementa a velocidade da bola
	 * @param vx incremento da velocidade em x
	 * @param vy incremento da velocidade em y
	 */
	public void incrementaVel( double vx, double vy){
		direcao.x += vx;
		direcao.y += vy;
		verificaVel();
	}

	/** incrementa a velocidade da bola
	 * @param inc vetor que corresponde ao incremento a efetuar
	 */
	public void incrementaVel( Vector2D inc ){
		direcao.soma( inc );
		verificaVel();
	}
	
	/**
	 * acelera a bola (multiplica a velocidade de um factor)
	 * @param ax acelera��o em x
	 * @param ay acelera��o em y
	 */
	public void aceleraVel( double ax, double ay ){
		direcao.x *= ax;
		direcao.y *= ay;
		verificaVel();
	}

	/**
	 * acelera a bola (multiplica a velocidade de um factor)
	 * @param acel vetor de acelara��o
	 */
	public void aceleraVel( Vector2D acel ){
		direcao.x *= acel.x;
		direcao.y *= acel.y;
		verificaVel();
	}
	
	/**
	 * mover a bola de acordo com a velocidade desta
	 */
	public void mover() {
		direcao.deslocaPonto( posicao );
		image.setPosicaoCentro( new Point((int)posicao.x,(int)posicao.y) );
	}
	
	/** Desenha a bola no ambiente gr�fico g
	 * @param g ambiente onde desenhar a bola
	 */
	public void desenhar( Graphics2D g ){
		image.setAngulo( direcao.getAngulo() );
		image.desenhar(g);
	}
		
	/**
	 * indica em que posi��o est� o topo da imagem da bola
	 */
	public Point getPosicaoImagem(){	
		return image.getPosicao();
	}
	
	/**
	 * indica em que posi��o est� o centro da bola
	 */
	public Point2D.Double getPosicaoCentro(){	
		return posicao;
	}

	/**
	 * coloca a imagem da bola numa dada posi��o
	 * @param p nova posi��o da bola
	 */
	public void setPosicaoImagem( Point p ){	
		posicao.x = p.x+image.getComprimento() / 2;
		posicao.y = p.y+image.getAltura() / 2;
		image.setPosicao( p );
	}
	
	/**
	 * devolve o raio da bola
	 * @return o raio da bola
	 */
	public double getRaio() {
		return raio;
	}
	
	/**
	 * altera a velocidade da bola
	 * @param vx nova componente da velocidade em x
	 * @param vy nova componente da velocidade em x
	 */
	public void setVelocidade( double vx, double vy ){
		direcao.x = vx;
		direcao.y = vy;
		verificaVel();
	}
	
	/** altera a dire��o da bola
	 * @param direcao nova dire��o da bola
	 */
	public void setDirecao(Vector2D direcao) {
		this.direcao = direcao.clone();
		this.direcao.normalizar();
	}

	/** devolve o vetor de dire��o da bola
	 * @return  o vetor de dire��o da bola
	 */
	public Vector2D getDirecao() {
		return direcao;
	}
	
	/** 
	 * roda a direc��o da bola de um �ngulo.
	 * @param ang o �ngulo que a bola vai rodar
	 */
	public void rodar( double ang ){
		direcao.rodar( ang );
	}
	
	/**
	 * aponta a bola na direc��o dada por um �ngulo (em radianos)
	 * @param ang �ngulo (em radianos) da direc��o da bola
	 */
	public void setAngulo( double ang ){
		direcao.setAngulo( ang );
	}
	
	/**
	 * indica em que direc��o a bola se mexe
	 * @return o angulo de direc��o da bola, em radianos
	 */
	public double getAngulo( ){
		return direcao.getAngulo();
	}
	
	/** 
	 * Indica a velocidade total da bola
	 * @return a velocidade total da bola
	 */
	public double getVelocidade() {
		return direcao.getComprimento();
	}
	
	/** verifica a velocidade e impede que a bola ande
	* a uma velocidade superior ao permitido
	*/
	private void verificaVel(){
		double velActual = direcao.getComprimento();
		if( velActual > max_veloc ) { 		
			direcao.x = direcao.x * max_veloc / velActual;
			direcao.y = direcao.y * max_veloc / velActual; 
		}
		int delays[] = { 20, 12, 10, 8, 7, 5, 3, 2, 1 };
		velActual = direcao.getComprimento();
		int delay = delays[((int)velActual)  % delays.length];
		if( velActual < 0.0000001 )
			delay = Integer.MAX_VALUE;
		image.setDelay( delay );
	}

	/** retorna a velocidade da bola
	 * @return a velocidade da bola
	 */
	public double getVel() {
		return direcao.getComprimento();
	}

	/** define a posi��o do centro da bola
	 * @param point a nova posi��o do centro da bola
	 */
	public void setPosicaoCentro(Point2D.Double point) {
		posicao.x = point.x;
		posicao.y = point.y;
		image.setPosicaoCentro( new Point((int)posicao.x, (int)posicao.y) );
	}
}