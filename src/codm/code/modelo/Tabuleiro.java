package codm.code.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObserver {

	private final  int linhas, colunas, minas;

	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {

		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;

		gerarCampos();
		associarVizinhos();
		sortearMinas();

	}


	public void paraCadaCampo(Consumer<Campo> funcao){
		campos.forEach(funcao);
	}

	public void abrir(int linha, int coluna) {

		campos.parallelStream()
				.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
				.findFirst()
				.ifPresent(c -> c.abrir());

	}

	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream()
				.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
				.findFirst()
				.ifPresent(c -> c.alternarMarcacao());

	}

	private void gerarCampos() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Campo campo = new Campo(i, j);
				campo.regristarObserver(this);
				campos.add(campo);
			}
		}

	}

	public void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}

	}

	public void sortearMinas() {
		long minasArmadas = 0;

		Predicate<Campo> minado = c -> c.isMinado();
		do {
			int aleatorio = (int) (Math.random() * campos.size()); // (int) faz chache para um valor do tipo inteiro
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		} while (minasArmadas < minas);

	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());

	}

	public void reiniciarJogo() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}

	@Override
	public void eventoOcorreu(Campo campo, EnumCampoEvento evento) {
		if (evento == EnumCampoEvento.EXPLODIR) {
			// System.out.println("PERDEU");
			notificarObservers(false);
			mostrarMinas();
		} else if (objetivoAlcancado()) {
			// System.out.println("USUARIO GANHOU");
			notificarObservers(true);
		}
	}

	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}

	private void notificarObservers(boolean resultado) {
		observadores.stream().forEach(o-> o.accept(new ResultadoEvento(resultado)));

	}

	private void mostrarMinas() {
		campos.stream()
				.filter(c -> c.isMinado())
				.filter(c-> !c.isMarcado())
				.forEach(c -> c.setAberto(true));
	}

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }


	

}
