package GUIs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Observable;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author jennifer
 */
public class GUIMistico extends JFrame {

    private int idJog1;
    private int idJog2;
    private int x;
    private int y;

    private Container cp;
    private JPanel tabuleiro = new JPanel();
    private JButton[][] campos; //vetor com todos os botões

    public GUIMistico(int idJog1, int idJog2) {
        this.idJog1 = idJog1;
        this.idJog2 = idJog2;
        this.x = 8;
        this.y = 10;

        setSize(1000, 600);
        setTitle("Místico");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cp = getContentPane();
        cp.setLayout(new BorderLayout());
        tabuleiro.setLayout(new GridBagLayout());


        campos = new JButton[x][y];
        GridBagConstraints c = new GridBagConstraints();

        for (int i = 0; i < x; i++) {// laços que criam o tabuleiro
            for (int j = 0; j < y; j++) {
                campos[i][j] = new JButton("" + i + " " + j);// cria um novo botão
                //define propriedades para ele
                c.gridy = i;
                c.gridx = j;
                tabuleiro.add(campos[i][j],c);// adiciona no tabuleiro
            }
        }

        cp.add(tabuleiro, BorderLayout.CENTER);

        setLocation(new CentroDoMonitorMaior().getCentroMonitorMaior(this));
        setVisible(true);
    }
}
