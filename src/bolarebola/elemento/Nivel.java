package bolarebola.elemento;

import java.awt.Graphics2D;
import java.util.ArrayList;

import bolarebola.elemento.obstaculo.*;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Classe que representa um nível do jogo.
 * O nível contém todos os elementos do nível: bola, buraco final,
 * paredes, colunas, etc, etc
 * É responsável por atualizar e garantir que os vários elementos
 * interagem uns com os outros
 */
public class Nivel {
	// elementos visuais presentes no jogo
	private ComponenteVisual fundo;
	private Bola bola;
	
	// variáveis para controlo do tempo
	private int tempoLimite; 
	private int tempoAtual;
	private long tempoInicio; 
	
	// saber se está a jogar e se ganhou
	private boolean estaJogar = false;
	private boolean ganhou = false;
	
	// verifica se algum obstáculo está a capturar a bola
	private Object captor;
	private int tipoCaptor;
	
	public static final int CAPTOR_BURACO = 0;
	public static final int CAPTOR_COLUNA = 1;
	
	// As colunas e paredes
	private ArrayList<Parede> paredes = new ArrayList<Parede>();
	private ArrayList<Coluna> colunas = new ArrayList<Coluna>();
	
	// o buraco final
	private BuracoFinal buraco;

	/** Cria um nível
	 * @param tempoLimite tempo limite do nível
	 * @param fundo imagem de fundo do nível
	 */
	public Nivel(int tempoLimite, ComponenteVisual fundo) {
		this.tempoLimite = tempoLimite;
		this.fundo = fundo;
		tempoAtual = tempoLimite;
	}
	
	/** Começar a jogar o nível
	 */
	public void comecar() {
		estaJogar = true;
		tempoAtual = tempoLimite;		
		tempoInicio = System.currentTimeMillis();
		ganhou = false;
	}

	/** atualiza os vários elementos presentes
	 */
	public void atualizar() {
		if( !estaJogar )
			return;
		
		// mover a bola, se não estiver capturada
		if( captor == null )
			bola.mover();

		// ver os obstáculos, se por acaso nenhum estiver a captar a bola
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

	/** devolve a imagem de fundo do nível
	 * @return  a imagem de fundo do nível
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

	/** retorna a bola do nível
	 * @return a bola do nível
	 */
	public Bola getBola() {
		return bola;
	}

	/** define a bola a jogar no nível
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
	
	/** desenha todos os elemenos do nível
	 * @param g ambiente gráfico onde desenhar
	 */
	public void desenhar(Graphics2D g) {
		// desenhar todos os componentes visuais aqui			
		fundo.desenhar( g );
		
		for( Parede p : paredes )
			p.desenhar( g );
		
		for( Coluna c : colunas )
			c.desenhar( g );

		buraco.desenhar( g );
		
		// se não tem captor também desenha a bola, senão é o captor que desenha a bola
		if( captor == null )
			bola.desenhar( g );			
	}

	/** indica ao nível que a bola chegou ao destino
	 */
	public void chegou() {
		estaJogar = false;
		ganhou = tempoAtual >= 0;		
	}

	/** indica se o nível está a ser jogado 
	 * @return true, se está a ser jogado
	 */
	public boolean estaJogar() {
		return estaJogar;
	}
	
	/** indica se o nível foi completado com sucesso
	 * @return true se nivel foi ganho, false se ainda está a jogar ou se perdeu
	 */
	public boolean ganhou() {
		return ganhou;
	}

	/** liberta a bola capturada por um obstáculo
	 * @param captor captor da bola
	 */
	public void libertarBola(Object captor ) {
		if( this.captor == captor )
			this.captor = null;		
	}

	/** informa o nível que um obstáculo capturou a bola 
	 * @param captor captor da bola
	 */
	public void captarBola(Object captor, int tipoCaptor ) {
		// só aceita captações se ainda não tiver sido captada
		if( this.captor == null ) {
			this.captor = captor;
			this.tipoCaptor = tipoCaptor;
		}
	}

	/** indica ao nível que este foi perdido
	 */
	public void perdeNivel() {
		estaJogar = false;
		ganhou = false;
	}
}
