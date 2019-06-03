package prof.jogos2D.image;

import java.awt.Graphics;
import java.util.Random;

/**
 * Classe que representa um componente cuja aparição é aleatória, isto é, só aparece de vez em quando.
 * Este tipo de componentes está invisível por um tempo mínimmo e até um tempo máximo. Dentro desta
 * gama de valores pode aparecer em qualquer altura 
 */
public class ComponenteAleatorio extends ComponenteDecorador {

	private boolean mostrar = false;  // se é para mostrar o componente
	private int proxAparicao;         // quando é a próxima aparição
	private int minCiclos;            // nº minimo de ciclos que deve permanecer escondido
	private int gama;                 // gama de tempo em que pode aparecer
	private int lastCiclo = 0;        // qual foi o último ciclo de animação que apresentou
	
	public ComponenteAleatorio( ComponenteVisual dec, int minInter, int maxInter ) {
		super( dec );
		minCiclos = minInter;
		gama = maxInter - minInter;
		proxAparicao = calculaProxAnim();
	}

	/**
	 * calcula quando será a próxima animação
	 * @return nº de ciclos de execução até proxima animação
	 */
	private int calculaProxAnim() {
		Random r = new Random();
		return minCiclos + r.nextInt( gama );
	}
	
	public void desenhar(Graphics g) {
		if( mostrar ){
			super.desenhar(g);
			if( numCiclosFeitos() > lastCiclo ){
				mostrar = false;
				proxAparicao = calculaProxAnim();
				lastCiclo = numCiclosFeitos();
			}
		}
		else {
			proxAparicao--;
			if( proxAparicao <= 0 )
				mostrar = true;			
		}
	}
	
	public ComponenteAleatorio clone() {
		return new ComponenteAleatorio( getDecorado(), minCiclos, minCiclos + gama );
	}
}
