package bolarebola.elemento.obstaculo;

import java.awt.Graphics2D;
import java.awt.Point;

import bolarebola.elemento.Nivel;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteVisual;

public interface Obstaculos {

	Nivel getNivel();

	void setNivel(Nivel nivel);

	ComponenteVisual getImagem();

	void setImagem(ComponenteVisual img);

	Point getPosicaoImagem();

	void setPosicaoImagem(Point p);

	void desenhar(Graphics2D g);

}