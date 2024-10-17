package codm.code.visao;

import codm.code.modelo.Campo;
import codm.code.modelo.CampoObserver;
import codm.code.modelo.EnumCampoEvento;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class CampoBotao extends JButton implements CampoObserver, MouseListener {

    private Campo campo;

    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_MARCADO = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TEXTO_VERDE = new Color(0, 100, 0);

    public CampoBotao(Campo campo) {
        this.campo = campo;
        campo.regristarObserver(this);
        setBackground(BG_PADRAO);
        // setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(1));

        addMouseListener(this);
        setBorder(BorderFactory.createBevelBorder(0));

    }

    public void eventoOcorreu(Campo campo, EnumCampoEvento campoEvento) {
        switch (campoEvento) {
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

        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    public void aplicarEstiloAbrir() {
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createLineBorder(Color.gray));

        if (campo.isMinado()) {
            setText("X");
            setBackground(BG_EXPLODIR);
            return;
        }

        switch (campo.minasNaVizinhanca()) {
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
                setForeground(Color.red);
            default:
                setForeground(Color.PINK);

        }

        String valor = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
        setText(valor);

    }

    public void aplicarEstiloMarcar() {
        setBackground(BG_MARCADO);
        setText("M");
        setForeground(Color.BLACK);
    }

    public void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setForeground(Color.WHITE);
        setText("X");

    }

    public void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == 1) {
            campo.abrir();

        } else {
            campo.alternarMarcacao();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
