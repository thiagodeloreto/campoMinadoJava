package br.com.cod3r.cm.visao;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import br.com.cod3r.cm.modelo.Campo;
import br.com.cod3r.cm.modelo.CampoEvento;
import br.com.cod3r.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {
	
	private Campo campo;
	private final Color BG_PADRAO = new Color(184, 184, 184);
	private final Color BG_MARCAR = new Color(8, 179, 247);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);

	public BotaoCampo(Campo campo) {
		
		addMouseListener(this);
		campo.registrarObservador(this);
		this.campo = campo;
		
		setBackground(BG_PADRAO);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));
	}

	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch(evento) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloMarcar();
			break;
		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();
		}
		
	}


	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}


	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setText("X");
	}


	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		setForeground(Color.BLACK);
		setText("M");
	}


	private void aplicarEstiloAbrir() {
		
		if(campo.isMinado()) {
			setBackground(BG_EXPLODIR);
			setText("X");
			return;
		}
		
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		switch(campo.minasNaVizinhanca()) {
		case 1:
			setForeground(TEXTO_VERDE);
			break;
		case 2: 
			setForeground(Color.BLUE);
			break;
		case 3: 
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:	
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}
		
		String valor = !campo.vizinhancaSegura() ? 
							campo.minasNaVizinhanca() + "" : "";
		setText(valor);
		
	}
	
	//Métodos da interface MouseEvent
	@Override
	public void mouseClicked(java.awt.event.MouseEvent e) {
		if(e.getButton() == 1) {
			campo.abrirCampo();
		}else {
			campo.alternarMarcacao();
		}
		
	}


	
	public void mousePressed(java.awt.event.MouseEvent e) {}	
	public void mouseReleased(java.awt.event.MouseEvent e) {}	
	public void mouseEntered(java.awt.event.MouseEvent e) {}	
	public void mouseExited(java.awt.event.MouseEvent e) {}
	
	
}
