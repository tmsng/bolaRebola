package bolarebola.elemento;

import java.awt.Graphics2D;
import java.util.ArrayList;

import bolarebola.elemento.obstaculo.*;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa um n�vel do jogo.
 * O n�vel cont�m todos os elementos do n�vel: bola, buraco final,
 * paredes, colunas, etc, etc
 * � respons�vel por atualizar e garantir que os v�rios elementos
 * interagem uns com os outros
 */
public class Nivel {
	// elementos visuais presentes no jogo
	private ComponenteVisual fundo;
	private Bola bola;
	
	// vari�veis para controlo do tempo
	private int tempoLimite; 
	private int tempoAtual;
	private long tempoInicio; 
	
	// saber se est� a jogar e se ganhou
	private boolean estaJogar = false;
	private boolean ganhou = false;
	
	// verifica se algum obst�culo est� a capturar a bola
	private Object captor;
	private int tipoCaptor;
	
	public static final int CAPTOR_BURACO = 0;
	public static final int CAPTOR_COLUNA = 1;
	
	// As colunas e paredes
	private ArrayList<Parede> paredes = new ArrayList<Parede>();
	private ArrayList<Coluna> colunas = new ArrayList<Coluna>();
	
	// o buraco final
	private BuracoFinal buraco;

	/** Cria um n�vel
	 * @param tempoLimite tempo limite do n�vel
	 * @param fundo imagem de fundo do n�vel
	 */
	public Nivel(int tempoLimite, ComponenteVisual fundo) {
		this.tempoLimite = tempoLimite;
		this.fundo = fundo;
		tempoAtual = tempoLimite;
	}
	
	/** Come�ar a jogar o n�vel
	 */
	public void comecar() {
		estaJogar = true;
		tempoAtual = tempoLimite;		
		tempoInicio = System.currentTimeMillis();
		ganhou = false;
	}

	/** atualiza os v�rios elementos presentes
	 */
	public void atualizar() {
		if( !estaJogar )
			return;
		
		// mover a bola, se n�o estiver capturada
		if( captor == null )
			bola.mover();

		// ver os obst�culos, se por acaso nenhum estiver a captar a bola
		if(captor == null ) {
			for( Parede p : paredes )
				p.ricocheteBola( bola );
	
			for( Coluna c : colunas )
				c.ricocheteBola( bola );
			
			// ver se bateu no buraco final
			buraco.engoleBola( bola );
		}
		else {	
			if( tipoCaptor == CAPTOR_BURACO )
				((BuracoFinal)captor).engoleBola( bola );
			else if( tipoCaptor == CAPTOR_COLUNA )
				((Coluna)captor).ricocheteBola( bola );
		}
	}

	/** devolve a imagem de fundo do n�vel
	 * @return  a imagem de fundo do n�vel
	 */
	public ComponenteVisual getFundo() {
		return fundo;
	}

	/** altera a imagem de fundo
	 * @param fundo nova imagem de fundo
	 */
	public void setFundo(ComponenteVisual fundo) {
		this.fundo = fundo;
	}

	/** retorna a bola do n�vel
	 * @return a bola do n�vel
	 */
	public Bola getBola() {
		return bola;
	}

	/** define a bola a jogar no n�vel
	 * @param bola nova bola
	 */
	public void setBola(Bola bola) {
		this.bola = bola;
	}

	/** retorna o tempo limite
	 * @return o tempo limite
	 */
	public int getTempoLimite() {
		return tempoLimite;
	}

	/** define o tempo limite
	 * @param tempoLimite novo tempo limite
	 */
	public void setTempoLimite(int tempoLimite) {
		this.tempoLimite = tempoLimite;
	}

	/** retorna o tempo atual do jogo
	 * @return o tempo atual do jogo
	 */
	public int getTempoAtual() {
		if( estaJogar ) 
			tempoAtual = tempoLimite - ((int)(System.currentTimeMillis() - tempoInicio ) / 1000);
		return tempoAtual;
	}

	public void addParede(Parede parede) {
		paredes.add( parede );
		parede.setNivel( this );
	}

	public void removeParede(Parede parede) {
		paredes.remove( parede );
	}

	public void addColuna(Coluna coluna) {
		colunas.add( coluna );		
		coluna.setNivel( this );
	}

	public void removeColuna(Coluna coluna) {
		colunas.remove( coluna );		
	}
	
	public void setBuraco( BuracoFinal b ) {
		buraco = b;
		buraco.setNivel( this );
	}

	public BuracoFinal getBuraco() {
		return buraco;
	}
	
	/** desenha todos os elemenos do n�vel
	 * @param g ambiente gr�fico onde desenhar
	 */
	public void desenhar(Graphics2D g) {
		// desenhar todos os componentes visuais aqui			
		fundo.desenhar( g );
		
		for( Parede p : paredes )
			p.desenhar( g );
		
		for( Coluna c : colunas )
			c.desenhar( g );

		buraco.desenhar( g );
		
		// se n�o tem captor tamb�m desenha a bola, sen�o � o captor que desenha a bola
		if( captor == null )
			bola.desenhar( g );			
	}

	/** indica ao n�vel que a bola chegou ao destino
	 */
	public void chegou() {
		estaJogar = false;
		ganhou = tempoAtual >= 0;		
	}

	/** indica se o n�vel est� a ser jogado 
	 * @return true, se est� a ser jogado
	 */
	public boolean estaJogar() {
		return estaJogar;
	}
	
	/** indica se o n�vel foi completado com sucesso
	 * @return true se nivel foi ganho, false se ainda est� a jogar ou se perdeu
	 */
	public boolean ganhou() {
		return ganhou;
	}

	/** liberta a bola capturada por um obst�culo
	 * @param captor captor da bola
	 */
	public void libertarBola(Object captor ) {
		if( this.captor == captor )
			this.captor = null;		
	}

	/** informa o n�vel que um obst�culo capturou a bola 
	 * @param captor captor da bola
	 */
	public void captarBola(Object captor, int tipoCaptor ) {
		// s� aceita capta��es se ainda n�o tiver sido captada
		if( this.captor == null ) {
			this.captor = captor;
			this.tipoCaptor = tipoCaptor;
		}
	}

	/** indica ao n�vel que este foi perdido
	 */
	public void perdeNivel() {
		estaJogar = false;
		ganhou = false;
	}
}
