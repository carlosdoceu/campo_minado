package codm.code.visao;

import codm.code.modelo.Tabuleiro;
import javax.swing.JFrame;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal(){
        
        Tabuleiro Tabuleiro = new Tabuleiro(16,30, 5);
        PainelTabuleiro painelTabuleiro = new PainelTabuleiro(Tabuleiro);
        
        add(painelTabuleiro);
        
        
        
        setTitle("Campo Minado");
        setSize(690,438);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

    }




    public static void main(String[] args) {
        new TelaPrincipal();
    }

}
