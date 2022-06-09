package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;



public class Campo {

	// Físico do campo
	private final int linha;
	private final int coluna;

	// Características do campo
	private boolean aberto = false;
	private boolean minado = false;
	private boolean marcado = false;

	List<Campo> vizinhos = new ArrayList<>();
	List<CampoObservador> observadores = new ArrayList<>();

	public Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public void registrarObservador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	private void notificarObservador(CampoEvento evento) {
		observadores.stream()
					.forEach(o -> o.eventoOcorreu(this, evento));
	}

	public boolean adicionarVizinho(Campo vizinho) {

		boolean linhaDiferente = this.linha != vizinho.linha;
		boolean colunaDiferente = this.coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;

		int deltaLinha = Math.abs(this.linha - vizinho.linha);
		int deltaColuna = Math.abs(this.coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;

		boolean estaAoLado = deltaGeral == 1 && !diagonal;
		boolean estaNaDiagonal = deltaGeral == 2 && diagonal;

		if (estaAoLado) {
			vizinhos.add(vizinho);
			return true;
		} else if (estaNaDiagonal) {
			vizinhos.add(vizinho);
			return true;
		} else
			return false;
	}

	public void alternarMarcacao() {
		if (!aberto) {
			marcado = !marcado;
			
			if(marcado) {
				notificarObservador(CampoEvento.MARCAR);
			}else {
				notificarObservador(CampoEvento.DESMARCAR);
			}
		}
	}

	public boolean abrirCampo() {
		if (!aberto && !marcado) {
			

			if (minado) {
				notificarObservador(CampoEvento.EXPLODIR);
				return true;
			}
				setAberto(true);
				

			if (vizinhancaSegura()) {
				vizinhos.forEach(v -> v.abrirCampo());
			}
			return true;
		} return false;
	}

	public boolean vizinhancaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	public void minar() {
		this.minado = true;
	}
	
	public boolean isMarcado() {
		return this.marcado;
	}
	
	public boolean isMinado() {
		return minado;
	}
	
	public boolean isAberto() {
		return this.aberto;
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		if(aberto) {
			notificarObservador(CampoEvento.ABRIR);
		}
	}

	public boolean isFechado(){
		return !isAberto();
	}
	
	public int getLinha() {
		return this.linha;
	}
	
	public int getColuna() {
		return this.coluna;
	}

	public boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
	public int minasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();
	}
	
	public void reiniciar() {
		this.aberto = false;
		this.minado = false;
		this.marcado = false;
		notificarObservador(CampoEvento.REINICIAR);
	}
	
	
}
