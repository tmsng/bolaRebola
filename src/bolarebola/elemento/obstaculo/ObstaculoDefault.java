package bolarebola.elemento.obstaculo;

import java.awt.Graphics2D;
import java.awt.Point;

import bolarebola.elemento.Nivel;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;

public abstract class ObstaculoDefault implements Obstaculos {

	private Nivel nivel;
	private ComponenteVisual imagem;
	
	@Override
	public Nivel getNivel() {
		return nivel;
	}

	@Override
	public void setNivel(Nivel nivel) {
		this.nivel = nivel;		
	}
	
	@Override
	public ComponenteVisual getImagem() {
		return imagem;
	}

	@Override
	public void setImagem(ComponenteVisual img) {
		this.imagem = img;
	}
	
	@Override
	public Point getPosicaoImagem() {
		return imagem.getPosicao();
	}

	@Override
	public void setPosicaoImagem(Point p) {
		imagem.setPosicao(p);
	}

	@Override
	public void desenhar(Graphics2D g) {
		imagem.desenhar(g);
	}
}
