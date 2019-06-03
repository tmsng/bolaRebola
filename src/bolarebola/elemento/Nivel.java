package bolarebola.elemento;

import java.awt.Graphics2D;
import java.util.ArrayList;

import bolarebola.app.BolaRebola;
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
	public static final int CAPTOR_RETENTOR = 2;
	public static final int CAPTOR_TUNEL = 3;
	
	// As colunas e paredes
	private ArrayList<Parede> paredes = new ArrayList<Parede>();
	private ArrayList<Coluna> colunas = new ArrayList<Coluna>();
	
	// Obstaculos
	private ArrayList<Atrator> atratores = new ArrayList<Atrator>();
	private ArrayList<TunelIn> tuneisIn = new ArrayList<TunelIn>();
	private ArrayList<TunelOut> tuneisOut = new ArrayList<TunelOut>();
	private ArrayList<Esteira> esteiras = new ArrayList<Esteira>();
	private ArrayList<Retentor> retentores = new ArrayList<Retentor>();
	private ArrayList<Terminador> terminadores = new ArrayList<Terminador>();
	private ArrayList<VidaExtra> vidasExtras = new ArrayList<VidaExtra>();
	private ArrayList<Retardador> retardadores = new ArrayList<Retardador>();
	
	//Lista de obstaculos
	private ArrayList<ObstaculoDefault> obstaculos = new ArrayList<ObstaculoDefault>();
	
	// o buraco final
	private BuracoFinal buraco;

	//1vida
	private boolean controloVida = true;
	
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
		controloVida = true;
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
			
			for(Esteira e : esteiras)
				e.alteraDirecao(bola);
			
			for(TunelIn t : tuneisIn)
				t.entrar(bola);
			
//			for(TunelOut t : tuneisOut)
//				t.cuspir(bola);
			
			for(Atrator a : atratores)
				a.atraiBola(bola);	
			
			for(Retentor r : retentores) {
				r.localAntes(bola);
				r.captarBola(bola);
			}
				
			for(Terminador t : terminadores)
				t.terminar(bola);
				
			for(VidaExtra vext : vidasExtras)
				vext.maisVida(bola);
				
			for(Retardador retd : retardadores)
				retd.retardar(bola);
				
				
			// ver se bateu no buraco final
			buraco.engoleBola( bola );
		}
		else {	
			if( tipoCaptor == CAPTOR_BURACO )
				((BuracoFinal)captor).engoleBola( bola );
			else if( tipoCaptor == CAPTOR_COLUNA )
				((Coluna)captor).ricocheteBola( bola );
			else if( tipoCaptor == CAPTOR_RETENTOR )
				((Retentor)captor).captarBola( bola );
			else if( tipoCaptor == CAPTOR_TUNEL )
				((TunelIn)captor).engoleBola( bola );
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
	
	//---------------------------------------------------------------------------------- Parede <
	
	public void addParede(Parede parede) {
		paredes.add( parede );
		parede.setNivel( this );
		obstaculos.add(parede);
	}

	public void removeParede(Parede parede) {
		paredes.remove( parede );
		obstaculos.remove( parede );
	}
	
	//---------------------------------------------------------------------------------- Coluna <

	public void addColuna(Coluna coluna) {
		colunas.add( coluna );		
		coluna.setNivel( this );
		obstaculos.add( coluna );
	}

	public void removeColuna(Coluna coluna) {
		colunas.remove( coluna );
		obstaculos.remove( coluna );
	}
	
	//---------------------------------------------------------------------------------- Buraco <
	
	public void setBuraco( BuracoFinal b ) {
		buraco = b;
		buraco.setNivel( this );
	}

	public BuracoFinal getBuraco() {
		return buraco;
	}
	
	//---------------------------------------------------------------------------------- Esteira <
	
	public void addEsteira(Esteira esteira) {
		esteiras.add( esteira );
		esteira.setNivel( this );
		obstaculos.add( esteira );
	}
	
	public void removeEsteira(Esteira esteira) {
		esteiras.remove( esteira );
		obstaculos.remove( esteira );
	}
	
	//---------------------------------------------------------------------------------- Tunel <
	
	public void addTunelIn(TunelIn tunel) {
		tuneisIn.add( tunel );
		tunel.setNivel( this );
		obstaculos.add( tunel );
	}
	
	public void addTunelOut(TunelOut tunel) {
		tuneisOut.add( tunel );
		tunel.setNivel( this );
		obstaculos.add( tunel );
	}
	
	public void removeTunelIn(TunelIn tunel) {
		tuneisIn.remove( tunel );
		obstaculos.remove( tunel );
	}
	
	public void removeTunelOut(TunelOut tunel) {
		tuneisOut.remove( tunel );
		obstaculos.remove( tunel );
	}
	
	//---------------------------------------------------------------------------------- Atrator <
	
	public void addAtrator(Atrator atrator) {
		atratores.add( atrator );
		atrator.setNivel( this );
		obstaculos.add( atrator );
	}
	
	public void removeAtrator(Atrator atrator) {
		atratores.remove( atrator );
		obstaculos.remove( atrator );
	}
	
	//---------------------------------------------------------------------------------- Retentor <
	
	public void addRetentor(Retentor retentor) {
		retentores.add( retentor );
		retentor.setNivel( this );
		obstaculos.add( retentor );
	}
	
	public void removeRetentor(Retentor retentor) {
		retentores.remove( retentor );
		obstaculos.remove( retentor );
	}
		
	//---------------------------------------------------------------------------------- Terminador <
	
	public void addTerminator(Terminador terminador) {
		terminadores.add( terminador );
		terminador.setNivel( this );
		obstaculos.add( terminador );
	}
	
	public void removeTerminator(Terminador terminador) {
		terminadores.remove( terminador );
		obstaculos.remove( terminador );
	}
	
	//---------------------------------------------------------------------------------- VidaExtra <
	
	public void addVidaExtra(VidaExtra vidaExtra) {
		vidasExtras.add( vidaExtra );
		vidaExtra.setNivel( this );
		obstaculos.add( vidaExtra );
	}
	
	public void removeVidaExtra(VidaExtra vidaExtra) {
		//vidasExtras.remove( vidaExtra );
		obstaculos.remove( vidaExtra );
	}
		
	//---------------------------------------------------------------------------------- Retardador <
	
	public void addRetardador(Retardador retardador) {
		retardadores.add( retardador );
		retardador.setNivel( this );
		obstaculos.add( retardador );
	}
	
	public void removeRetardador(Retardador retardador) {
		retardadores.remove( retardador );
		obstaculos.remove( retardador );
	}
		
	
	/** desenha todos os elemenos do n�vel
	 * @param g ambiente gr�fico onde desenhar
	 */
	public void desenhar(Graphics2D g) {
		// desenhar todos os componentes visuais aqui			
		fundo.desenhar( g );
		
		for (ObstaculoDefault o : obstaculos)
			o.desenhar(g);
		
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
	
	/** adiciona uma vida ao jogador
	 */
	public void addVida() {
		if(controloVida) {
			BolaRebola.addVida();
			controloVida = false;
		}
	}
}
