package bolarebola.elemento.obstaculo;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import bolarebola.elemento.Bola;
import bolarebola.elemento.Nivel;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;
import prof.jogos2D.util.DetectorColisoes;
import prof.jogos2D.util.Vector2D;

public class Retentor extends ObstaculoDefault {

	private Point2D.Double centro;  // posição central do retentor
	private int raio;  // raio do retentor
	private int ciclos;	// ciclos de retencao (tempo da retencao)
	
	private boolean estaCaptada = false;
	private int countCiclos = 0;
	
	private boolean antiga = false;
	private Bola bolaCaptada;
	private double velAntiga;
	private Point2D.Double posAntiga;
	private Vector2D dirAntiga;
	
	
	public Retentor(Double c, int r, int ccl, ComponenteVisual cv) {
		super.setImagem(cv);
		this.centro = c;
		this.raio = r;
		this.ciclos = ccl;
	}
	
	/** indica se a bola "bateu" no retentor 
	 */
	public boolean bateu(Bola b) {
		return DetectorColisoes.intersectam( centro,  raio, b.getPosicaoCentro(), b.getRaio() );
	}
	
	public void localAntes(Bola bolaCaptada) {
		if(!antiga) {
			posAntiga = bolaCaptada.getPosicaoCentro();
			velAntiga = bolaCaptada.getVel();
			dirAntiga = bolaCaptada.getDirecao();
//			System.out.println(velAntiga + " | " + posAntiga + " | " + dirAntiga);
		}
	}
	
	/** capta a bola
	 * @param bolaCaptada a bola a captar
	 */
	public void captarBola(Bola bolaCaptada) {
		if(!bateu(bolaCaptada))
			return;
		
		antiga = true;
		
		this.bolaCaptada = bolaCaptada;
		
		temBolaCaptada(bolaCaptada);
		
		if(estaCaptada)
			libertarBola(posAntiga, velAntiga, dirAntiga);
	}
	
	/** testa se tem bola captada
	 * @return true, se tem bola captada
	 */
	private void temBolaCaptada(Bola bola) {
//		System.out.println(countCiclos + "|" + ciclos);
		
		if(countCiclos == ciclos)
			estaCaptada = true;
		else
			bolaCaptada.setVelocidade(0, 0);
			bolaCaptada.setPosicaoCentro(centro);
			countCiclos++;
	}

	/** liberta a bola
	 * @param vel 
	 * @param pos 
	 * @param dir 
	 */
	private void libertarBola(Double pos, double vel, Vector2D dir) {
//		System.out.println("livre");
//		System.out.println(vel + " | " + pos + " | " + dir);
		
		pos.x = pos.x - 40;
		pos.y = pos.y + 20;
		bolaCaptada.setPosicaoCentro(pos);
		bolaCaptada.setVelocidade(vel, vel);
		dir.x = -dir.x;
		dir.y = -dir.y;
		bolaCaptada.setDirecao(dir);
		
		estaCaptada = false;
		antiga = false;
		countCiclos = 0;
	}
	
	public void desenhar(Graphics2D g) {
		super.getImagem().desenhar(g);
		

		ComponenteMultiAnimado imagem = (ComponenteMultiAnimado)getImagem();
		if(!antiga) {
			imagem.setAnim(0);
			imagem.reset();
			imagem.setCiclico( false );
		}
		else {
			imagem.setAnim(1);
			imagem.reset();
			imagem.setCiclico( false );
		}
	}
}
