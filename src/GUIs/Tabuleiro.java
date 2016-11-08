package GUIs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author jennifer
 */
public class Tabuleiro extends JPanel {

    private int x;
    private int y;

    private JPanel tabuleiro = new JPanel();
    private JButton[][] campos; //vetor com todos os botões

    public Tabuleiro(int x, int y) {
//        super(new GridBagLayout());
        super(new GridLayout(y,x));

        this.x = x;
        this.y = y;

        campos = new JButton[x][y];
//        GridBagConstraints c = new GridBagConstraints();

        for (int i = 0; i < x; i++) {// laços que criam o tabuleiro
            for (int j = 0; j < y; j++) {      
                campos[i][j] = new JButton("   ");// cria um novo botão
                //define propriedades para ele
//                c.gridy = i;
//                c.gridx = j;
                campos[i][j].setBackground(Color.WHITE);
                campos[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
//                campos[i][j].setMargin(new Insets(3,3,3,3));
                add(campos[i][j]);// adiciona no tabuleiro
            }
        }

    }
}
