package bolarebola.app;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;

import bolarebola.elemento.Bola;
import bolarebola.elemento.Nivel;
import prof.jogos2D.util.SKeyboard;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

public class BolaRebola extends JFrame {
	// Apenas para não dar warnings
	private static final long serialVersionUID = 1L;
	
	// zona de jogo
	private JPanel zonaJogo;
	
	// Controlo do nível
	private Nivel nivel;
	private int nivelAtual = 1;
	private Timer temporizador;
	
	// Controlo da vida
	private static int vida = 5;
	
	// criação do teclado
	private SKeyboard keyboard = new SKeyboard( );
	
	// elementos de interface do tempo, nível e vidas
	private final Font fonteTempo = new Font("Arial", Font.BOLD, 40 );
	private final Color corTempoMais10 = new Color( 50, 230, 50 );
	private final Color corTempoMenos10 = new Color( 250, 250, 0 );
	private final Color corTempoPerdeu = new Color( 250, 0, 0 );
	
	private final Font fonteNivel = new Font("Arial", Font.BOLD, 40 );
	private final Color corNivel = new Color( 250, 250, 250);
	
	private final Font fonteVidas = new Font("Arial", Font.BOLD, 40 );
	private final Color corVidas = new Color( 250, 0, 0);
	

	// teclas para controlar a bola
	private static int SUBIR = KeyEvent.VK_Q;
	private static int DESCER = KeyEvent.VK_A;
	private static int ESQUERDA = KeyEvent.VK_O;
	private static int DIREITA = KeyEvent.VK_P;
	private static int REINICIAR = KeyEvent.VK_R;
	
	/** construtor por defeito
	 */
	public BolaRebola() {
		super();
		initialize();
	}

	/** iniciar a parte gráfica
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setMinimumSize(new Dimension(1024, 768));
		this.setContentPane(getJContentPane());
		this.setTitle("Bola Rebola");
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		// vai começar por ler o nivel atual
		nivel = NivelReader.lerNivel( nivelAtual );

		// iniciar a execução do método que vai mandar redesenhar
		temporizador = new Timer( 33, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				atualizar();		
			}			
		});
		temporizador.start();	
	}

	/**
	 * método que começa a jogar o nível
	 */
	private void comecarNivel(){
		nivel.comecar();
	}

	/**
	 * método que processa os eventos do jogo e actualiza o nível
	 */
	private void atualizar(){
		// se não está a jogar não tem de actualizar nada
		if( !nivel.estaJogar() ) {
			// se não está a jogar e se carrega numa tecla, começa a jogar
			if( keyboard.estaPremida( SUBIR ) || keyboard.estaPremida( DESCER ) ||
				keyboard.estaPremida( ESQUERDA ) || keyboard.estaPremida( DIREITA )	)
 				comecarNivel();
			return;
		}

		// verificar as teclas premidas e controlar a bola
		Bola bola = nivel.getBola();
		if( keyboard.estaPremida( SUBIR ) ){
			bola.incrementaVel( 0, -0.3 );
		}
		else if( keyboard.estaPremida( DESCER ) ){
			bola.incrementaVel( 0, +0.3 );
		}
		if( keyboard.estaPremida( ESQUERDA ) ){
			bola.incrementaVel( -0.3, 0 );
		}
		else if( keyboard.estaPremida( DIREITA ) ){
			bola.incrementaVel( 0.3, 0 );
		}
		
		// ver o reiniciar
		if( keyboard.estaPremida( REINICIAR )) {
			nivel.perdeNivel();
		}

		nivel.atualizar(); // atualizar o nivel
				
		testarFim();  // ver se já acabou o jogo
		
		// mandar desenhar todos elementos do jogo
		zonaJogo.repaint();
	}

	public static void addVida() {
		vida += 1;
	}
	
	/** desenha todos os elementos do jogo
	 * @param g ambiente gráfico onde desenhar
	 */
	private void desenharJogo(Graphics2D g) {
		nivel.desenhar( g );
		Color c = corTempoMais10;

		// desenhar o número do nível
		g.setFont(fonteNivel);
		g.setColor(corNivel);
		g.drawString( "Nivel: " + nivelAtual, 820, 50 );
		
		// desenhar o número de tentativas
		g.setFont(fonteVidas);
		g.setColor(corVidas);
		
		switch (vida) {
		case 7: g.drawString( "♥ ♥ ♥ ♥ ♥ ♥ ♥" , 365, 50 );
			break;
		case 6: g.drawString( "♥ ♥ ♥ ♥ ♥ ♥" , 385, 50 );
			break;
		case 5: g.drawString( "♥ ♥ ♥ ♥ ♥" , 405, 50 );
			break;
		case 4: g.drawString( "♥ ♥ ♥ ♥" , 425, 50 );
			break;
		case 3: g.drawString( "♥ ♥ ♥" , 445, 50 );
			break;
		case 2: g.drawString( "♥ ♥" , 465, 50 );
			break;
		case 1: g.drawString( "♥" , 485, 50 );
			break;
		}
		
		// desenhar o tempo que falta
		g.setFont( fonteTempo );
		if(nivel.getTempoAtual() <= 10 && nivel.getTempoAtual() > 0){
			c = corTempoMenos10;
		}
		else if(nivel.getTempoAtual() <= 0){
			c = corTempoPerdeu;
		}
		g.setColor( c );
		g.drawString( "" + nivel.getTempoAtual(), 50, 50 );
	}		

	/** 
	 * vai testar o fim de jogo
	 */
	public void testarFim() {
		if( nivel.estaJogar() )
			return;
		
		if( nivel.ganhou() ) {
			JOptionPane.showMessageDialog( this, "Passou de nível!!!");

			// passar para o nivel seguinte
			nivelAtual++;
			
			// se já acabaram os níveis volta ao primeiro
			if( !existeNivel(nivelAtual) ) {
				JOptionPane.showMessageDialog( this, "JOGO VENCIDO!!! Vamos reiniciar!");
				nivelAtual = 1;
			}
		}
		else {
			vida -= 1;
			
			//se perdeu as vidas todas
			if(vida == 0){
				JOptionPane.showMessageDialog( this, "FICOU SEM VIDAS!!! Vamos reiniciar!");
				nivelAtual = 1;
				vida = 5;
			}
			else {
				JOptionPane.showMessageDialog( this, "PERDEU!!!");
			}
		}
		
		// ler o novo nível (ou o mesmo se perdeu)
		nivel = NivelReader.lerNivel( nivelAtual );
	}

	/**
	 * verifica se o nível indicado existe
	 * @param level o número do nível a verificar
	 * @return true se o nível existe
	 */
	private boolean existeNivel(int level ) {
		String fileName = "niveis\\nivel" + level + ".txt";
		File f = new File( fileName );
		return f.exists();
	}

	/**** Métodos auuxiliares de contrução da interface gráfica
	 */
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		JPanel jContentPane = new JPanel();
		jContentPane.setLayout(new BorderLayout());
		jContentPane.add(getZonaJogo(), BorderLayout.CENTER);
		return jContentPane;
	}

	/**
	 * This method initializes zonaJogo	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getZonaJogo() {
		zonaJogo = new JPanel() {
			public void paint( Graphics g ){
				super.paint( g );
				desenharJogo( (Graphics2D) g );
			}
		};
		zonaJogo.setLayout(new GridBagLayout());
		zonaJogo.setPreferredSize(new Dimension(1024, 768));
		return zonaJogo;
	}

	
	public static void main(String[] args) {
		BolaRebola br = new BolaRebola();
		br.setVisible( true );
	}
}
