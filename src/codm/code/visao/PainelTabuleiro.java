package codm.code.visao;

import codm.code.modelo.Tabuleiro;
import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tabuleiro) {

        setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));

        tabuleiro.paraCadaCampo(c -> add(new CampoBotao(c)));

        tabuleiro.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {

                if (e.isGanhou()) {
                    JOptionPane.showMessageDialog(this, "Parabéns, você venceu!");
                } else {
                    JOptionPane.showMessageDialog(this, "Você perdeu!");
                }

                tabuleiro.reiniciarJogo();
            });

        });

    }

}
