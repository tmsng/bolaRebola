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
	// Apenas para n�o dar warnings
	private static final long serialVersionUID = 1L;
	
	// zona de jogo
	private JPanel zonaJogo;
	
	// Controlo do n�vel
	private Nivel nivel;
	private int nivelAtual = 1;
	private Timer temporizador;
	
	// cria��o do teclado
	private SKeyboard keyboard = new SKeyboard( );
	
	// elementos de interface do tempo, n�vel e vidas
	private final Font fonteTempo = new Font("Arial", Font.BOLD, 40 );
	private final Color corTempo = new Color( 50, 230, 50 );

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

	/** iniciar a parte gr�fica
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setMinimumSize(new Dimension(1024, 768));
		this.setContentPane(getJContentPane());
		this.setTitle("Bola Rebola");
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		// vai come�ar por ler o nivel atual
		nivel = NivelReader.lerNivel( nivelAtual );

		// iniciar a execu��o do m�todo que vai mandar redesenhar
		temporizador = new Timer( 33, new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				atualizar();		
			}			
		});
		temporizador.start();	
	}

	/**
	 * m�todo que come�a a jogar o n�vel
	 */
	private void comecarNivel(){
		nivel.comecar();
	}

	/**
	 * m�todo que processa os eventos do jogo e actualiza o n�vel
	 */
	private void atualizar(){
		// se n�o est� a jogar n�o tem de actualizar nada
		if( !nivel.estaJogar() ) {
			// se n�o est� a jogar e se carrega numa tecla, come�a a jogar
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
				
		testarFim();  // ver se j� acabou o jogo
		
		// mandar desenhar todos elementos do jogo
		zonaJogo.repaint();
	}

	/** desenha todos os elementos do jogo
	 * @param g ambiente gr�fico onde desenhar
	 */
	private void desenharJogo(Graphics2D g) {
		nivel.desenhar( g );

		// desenhar o n�mero do n�vel
		
		// desenhar o n�mero de tentativas
		
		// desenhar o tempo que falta
		g.setFont( fonteTempo );
		g.setColor( corTempo );
		g.drawString( "" + nivel.getTempoAtual(), 50, 40 );
	}		

	/** 
	 * vai testar o fim de jogo
	 */
	public void testarFim() {
		if( nivel.estaJogar() )
			return;
		
		if( nivel.ganhou() ) {
			JOptionPane.showMessageDialog( this, "Passou de n�vel!!!");

			// passar para o nivel seguinte
			nivelAtual++;
			
			// se j� acabaram os n�veis volta ao primeiro
			if( !existeNivel(nivelAtual) ) {
				JOptionPane.showMessageDialog( this, "JOGO VENCIDO!!! Vamos reiniciar!");
				nivelAtual = 1;
			}
		}
		else {
			JOptionPane.showMessageDialog( this, "PERDEU!!!");
		}
		
		// ler o novo n�vel (ou o mesmo se perdeu)
		nivel = NivelReader.lerNivel( nivelAtual );
	}

	/**
	 * verifica se o n�vel indicado existe
	 * @param level o n�mero do n�vel a verificar
	 * @return true se o n�vel existe
	 */
	private boolean existeNivel(int level ) {
		String fileName = "niveis\\nivel" + level + ".txt";
		File f = new File( fileName );
		return f.exists();
	}

	/**** M�todos auuxiliares de contru��o da interface gr�fica
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
