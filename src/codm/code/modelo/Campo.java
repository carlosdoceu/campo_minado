package codm.code.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

	private final int linha, coluna;
	private boolean aberto = false, minado = false, marcado = false;

	private List<Campo> listVizinhos = new ArrayList<>();
	private List<CampoObserver> listObservers = new ArrayList<>();

	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;

	}

	public void regristarObserver(CampoObserver observable) {
		listObservers.add(observable);

	}

	private void notificarObservers(EnumCampoEvento evento) {
		listObservers.stream().forEach(o -> o.eventoOcorreu(this, evento));

	}

	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;

		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);

		int deltaGeral = deltaColuna + deltaLinha;

		if (deltaGeral == 1 && !diagonal) {
			listVizinhos.add(vizinho);
			return true;
		} else if (deltaGeral == 2 && diagonal) {
			listVizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}

	}

	public void alternarMarcacao() {
		if (!aberto) {
			marcado = !marcado;
			if (marcado) {
				notificarObservers(EnumCampoEvento.MARCAR);
			} else {
				notificarObservers(EnumCampoEvento.DESMARCAR);
			}
		}

	}

	public boolean abrir() {
		if (!aberto && !marcado) {
			aberto = true;
			if (minado) {
				notificarObservers(EnumCampoEvento.EXPLODIR);
				return true;
			}
			setAberto(true);
			notificarObservers(EnumCampoEvento.ABRIR);

			if (vizinhancaSegura()) {
				listVizinhos.forEach(v -> v.abrir());
			}
		
		}
		return false;
	}

	public boolean vizinhancaSegura() {
		return listVizinhos.stream().noneMatch(v -> v.minado);

	}

	void minar() {
		minado = true;
	}

	public boolean isMarcado() {
		return marcado;
	}

	void setAberto(boolean aberto) {
		this.aberto = aberto;
		if (aberto) {
			notificarObservers(EnumCampoEvento.ABRIR);
		}
	}

	public boolean isAberto() {
		return aberto;
	}

	public boolean isFechado() {
		return !isAberto();
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	public boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}

	public int minasNaVizinhanca() {
		return (int) listVizinhos.stream().filter(v -> v.minado).count();
	}

	public void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservers(EnumCampoEvento.REINICIAR);

	}

	public boolean isMinado() {
		return minado;
	}

}
