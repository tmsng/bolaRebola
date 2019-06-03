package bolarebola.app;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import bolarebola.elemento.*;
import bolarebola.elemento.obstaculo.*;
import prof.jogos2D.image.ComponenteAnimado;
import prof.jogos2D.image.ComponenteMultiAnimado;
import prof.jogos2D.image.ComponenteSimples;
import prof.jogos2D.image.ComponenteVisual;

/**
 * Esta classe contém o código para ler o ficheiro de nível
 */
public class NivelReader {
	
	private static int lvl;
	
	/**
	 * método para ler o ficheiro de nível
	 * @param level número do nivel
	 * @return o Nivel criado
	 */
	public static Nivel lerNivel( int level ){
		String file = "niveis\\nivel" + level + ".txt";
		
		lvl = level;

		try (BufferedReader fnivel = new BufferedReader( new FileReader( file ) );){
			// ler o tempo que tem para acabar o nivel
			int tempoLimite = Integer.parseInt( fnivel.readLine() );

			// ler o fundo
			String fundoFich = fnivel.readLine();
			ComponenteVisual fundo = new ComponenteSimples( new Point(0,0), fundoFich );
			Nivel novoNivel = new Nivel( tempoLimite, fundo );

			// ler a posição inicial da bola, as várias infos estão separadas por \t (TAB)
			// no formato x y nomeFicheiroImagem
			String infoBola[] = fnivel.readLine().split( "\t" );
			Point centro = lerPosicao( infoBola[ 0 ] , infoBola[ 1 ] );
			int raioBola = Integer.parseInt( infoBola[ 2 ] );
			int nAnimsBola = Integer.parseInt( infoBola[ 4 ] );
			int nDelayBola = Integer.parseInt( infoBola[ 5 ] );
			ComponenteAnimado b = new ComponenteAnimado( centro, infoBola[3], nAnimsBola, nDelayBola );
			novoNivel.setBola( new Bola( centro, raioBola, b ) );

			// ler a informação dos obstáculos
			// cada obstáculo terá de ter na primera posição uma string que o caracterize
			// cada informação deverá estar separada por \t (TAB)
			String linha = fnivel.readLine();
			while( linha != null ){
				String info[] = linha.split( "\t" );
				if( info[ 0 ].equals("BURACO" ))
					criarBuraco( novoNivel, info );
				else if( info[ 0 ].equals("PAREDE" ))
					criarParede( novoNivel, info );
				else if( info[ 0 ].equals("COLUNA" ))
					criarColuna( novoNivel, info );
				else if( info[ 0 ].equals("ESTEIRA" ))
					criarEsteira( novoNivel, info );
				else if( info[ 0 ].equals("TUNEL" ))
					criarTunel( novoNivel, info );
				else if( info[ 0 ].equals("ATRATOR" ))
					criarAtrator( novoNivel, info );
				else if( info[ 0 ].equals("RETENTOR" ))					
					criarRetentor( novoNivel, info );
				else if( info[ 0 ].equals("TERMINATOR" ))
					criarTerminator( novoNivel, info );
				else if( info[ 0 ].equals("CORACAO" ))
					criarVidaExtra( novoNivel, info );
				else if( info[ 0 ].equals("RETARDADOR" ))
					criarRetardador( novoNivel, info );
				linha = fnivel.readLine();
			}			
			return novoNivel;
		}
		catch( Exception e ){
			// se falhou a ler o nível sai do jogo 
			JOptionPane.showMessageDialog( null, "Erro - " + e.getMessage() + " -\na leitura do nivel " + file );
			System.exit( 1 );
			return null;
		}		
	}
	
	
	//------------------------------------------------------------------------------------------------------------ Buraco <

	/** Buraco Final
	 * 
	 * BURACO	pos x + pos y + raio + <info imagem>
	 */
	private static void criarBuraco(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double centro = lerPosicaoPrecisa(info[ 1 ] , info[ 2 ] );
		int raio = Integer.parseInt( info[ 3 ] );
		ComponenteMultiAnimado burImg = (ComponenteMultiAnimado)lerComponenteVisual( info, 4);
		novoNivel.setBuraco( new BuracoFinal( centro, raio , burImg ) );
	}
	
	//------------------------------------------------------------------------------------------------------------ Parede <
	
	/** 
	 * Parede
	 * 
	 * PAREDE ini x + ini y + fim x + fim y + <info imagem>
	 */
	private static void criarParede(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double ini = lerPosicaoPrecisa( info[1], info[2] );
		Point2D.Double fim = lerPosicaoPrecisa( info[3], info[4] );
		ComponenteVisual cv = lerComponenteVisual( info, 5 );
		Parede p =  new Parede( ini, fim, cv );
		novoNivel.addParede( p );
	}

	//------------------------------------------------------------------------------------------------------------ Coluna <
	
	/** 
	 * Coluna
	 * 
	 * COLUNA	centro x + centro x + raio + <info imagem>
	 */
	private static void criarColuna(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double centro = lerPosicaoPrecisa( info[1], info[2] );
		int raio = Integer.parseInt( info[3] );
		ComponenteVisual cv = lerComponenteVisual( info, 4 );
		Coluna c = new Coluna( centro, raio, cv );
		novoNivel.addColuna( c );
	}
	
	//------------------------------------------------------------------------------------------------------------ Esteira <

	/** 
	 * Esteira
	 * 
	 * ESTEIRA ini x + ini y + fim x + fim y + incrementoVeloc X + incrementoVeloc Y + <info imagem>
	 */
	private static void criarEsteira(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double ini = lerPosicaoPrecisa( info[1], info[2] );
		int comp = Integer.parseInt( info[3] );
		int alt = Integer.parseInt( info[4] );
		double fx = Double.parseDouble( info[5] );
		double fy = Double.parseDouble( info[6] );
		ComponenteVisual cv = lerComponenteVisual( info, 7 );
		if(lvl == 4) {
			cv.rodar(4.71);
		}
		
		Esteira e = new Esteira(ini, comp, alt, fx, fy, cv);
		novoNivel.addEsteira(e);
	}
	
	//------------------------------------------------------------------------------------------------------------ Tunel <
	
	/** 
	 * Tunel
	 * 
	 * TUNEL centro in x + centro in y + raio in + centro out x + centro out y + <info imagem in > + <info imagem out>
	 */
	private static void criarTunel(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double centroIn = lerPosicaoPrecisa(info[1], info[2] );
		int raio = Integer.parseInt( info[3] );
		Point2D.Double centroOut = lerPosicaoPrecisa( info[4], info[5] );
		ComponenteVisual cvIn = lerComponenteVisual( info, 6 );
		ComponenteVisual cvOut = lerComponenteVisual( info, 13 );

		TunelIn t_in = new TunelIn(centroIn, raio, cvIn);
		TunelOut t_out = new TunelOut(centroOut, cvOut);
		novoNivel.addTunelIn(t_in);
		novoNivel.addTunelOut(t_out);
	}
	
	//------------------------------------------------------------------------------------------------------------ Atrator <

	/** 
	 * Atrator
	 * 
	 * ATRATOR	pos x + pos	y + fator atração + <info imagem>
	 */
	private static void criarAtrator(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double pos = lerPosicaoPrecisa( info[1], info[2] );
		double g = Double.parseDouble( info[3] );
		ComponenteVisual cv = lerComponenteVisual( info, 4 );

		Atrator a = new Atrator( pos, g, cv );
		novoNivel.addAtrator(a);	
	}
	
	//------------------------------------------------------------------------------------------------------------ Retentor <
	
	/** 
	 * Retentor
	 * 
	 * RETENTOR	pos x + pos	y + raio + nº ciclos de retenção + <info imagem>
	 */
	private static void criarRetentor(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double centro = lerPosicaoPrecisa( info[1], info[2] );
		int raio = Integer.parseInt( info[3] );
		int ciclos = Integer.parseInt( info[4] );
		ComponenteVisual cv = lerComponenteVisual( info, 5 );
	
		Retentor r = new Retentor(centro, raio, ciclos, cv);
		novoNivel.addRetentor(r);
	}
	
	//------------------------------------------------------------------------------------------------------------ Terminador <

	/** 
	 * Terminator
	 * 
	 * TERMINATOR centro x + centro y + raio + <info imagem>
	 */
	private static void criarTerminator(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double centro = lerPosicaoPrecisa( info[1], info[2] );
		int raio = Integer.parseInt( info[3] );
		ComponenteVisual cv = lerComponenteVisual( info, 4 );
	
		Terminador t = new Terminador(centro, raio, cv);
		novoNivel.addTerminator(t);
	}
	
	//------------------------------------------------------------------------------------------------------------ VidaExtra <
	
	/** 
	 * VidaExtra
	 * 
	 * VIDAEXTRA centro x + centro y + raio + <info imagem>
	 */
	private static void criarVidaExtra(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double centro = lerPosicaoPrecisa( info[1], info[2] );
		int raio = Integer.parseInt( info[3] );
		ComponenteVisual cv = lerComponenteVisual( info, 4 );
		
		VidaExtra vext = new VidaExtra(centro, raio, cv);
		novoNivel.addVidaExtra(vext);
	}
	
	//------------------------------------------------------------------------------------------------------------ Retardador <
	
	/** 
	 * Retardador
	 * 
	 * RETARDADOR centro x + centro y + incrementoVel_x + incrementoVel_y + <info imagem>
	 */
	private static void criarRetardador(Nivel novoNivel, String[] info) throws IOException {
		Point2D.Double ini = lerPosicaoPrecisa( info[1], info[2] );
		int comp = Integer.parseInt( info[3] );
		int alt = Integer.parseInt( info[4] );
		double fx = Double.parseDouble( info[5] );
		double fy = Double.parseDouble( info[6] );
		ComponenteVisual cv = lerComponenteVisual( info, 7 );
		
		Retardador retd = new Retardador(ini, comp, alt, fx, fy, cv);
		novoNivel.addRetardador(retd);
	}
	

	/** leitura de um componente visual
	 * @param info informação da linha
	 * @param idx índice a partir do qual está presente a info do componente
	 * @return o componente visual criado
	 * @throws IOException
	 */
	private static ComponenteVisual lerComponenteVisual(String[] info, int idx ) throws IOException {
		switch( info[idx] ) {
		case "CS":  return criarComponenteSimples( info, idx+1 );
		case "CA":  return criarComponenteAnimado( info, idx+1 );
		case "CMA": return criarComponenteMultiAnimado( info, idx+1 );
		}
		return null;
	}

	/** le a info e cria um componente simples
	 * Na linha a info é <br>
	 * CS pos x + pos y + nome da imagem
	 */
	private static ComponenteSimples criarComponenteSimples(String[] info, int idx) throws IOException {
		Point p = lerPosicao( info[idx], info[idx+1] );
		return new ComponenteSimples(p, info[idx+2] );
	}
	
	/** le a info e cria um componente animado
	 * Na linha a info é <br>
	 * CA pos x + pos y + nome da imagem + número de frames + delay na animação
	 */
	private static ComponenteAnimado criarComponenteAnimado(String[] info, int idx ) throws IOException {
		Point p = lerPosicao( info[idx], info[idx+1] );
		String nomeImg =  info[idx+2];
		int nFrames = Integer.parseInt( info[idx+3] );
		int delay = Integer.parseInt( info[idx+4] );
		return new ComponenteAnimado( p, nomeImg, nFrames, delay );
	}

	/** le a info e cria um componente multianimado
	 * Na linha a info é <br>
	 * CMA	pos x + pos y + nome da imagem + número de animações + número de frames + delay
	 */
	private static ComponenteMultiAnimado criarComponenteMultiAnimado(String[] info, int idx ) throws IOException {
		Point p = lerPosicao( info[idx], info[idx+1] );
		String nomeImg =  info[idx+2];
		int nAnims =  Integer.parseInt( info[idx+3] );
		int nFrames = Integer.parseInt( info[idx+4] );
		int delay = Integer.parseInt( info[idx+5] );
		return new ComponenteMultiAnimado( p, nomeImg, nAnims, nFrames, delay );
	}

	/**lê uma posição em point2D.Double
	 * @param strx coordenada x
	 * @param stry coordenada y
	 * @return a posição criada
	 */
	private static Point2D.Double lerPosicaoPrecisa(String strx, String stry) {
		double x = Double.parseDouble(strx);
		double y = Double.parseDouble(stry);
		return new Point2D.Double(x,y);
	}
	
	/**lê uma posição em point
	 * @param strx coordenada x
	 * @param stry coordenada y
	 * @return a posição criada
	 */
	private static Point lerPosicao(String strx, String stry) {
		int x = Integer.parseInt(strx);
		int y = Integer.parseInt(stry);
		return new Point(x,y);
	}
}
