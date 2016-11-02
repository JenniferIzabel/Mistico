package GUIs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Esta classe representa a grade.
 *
 * @author higor
 */
public class Grid extends JLabel {
    private int x;      // X posição no jogo.
    private int y;      // Y posição no jogo.

   
    public Grid(int x, int y) {
        super("", CENTER);
        this.x = x;
        this.y = y;
        
        setPreferredSize(new Dimension(40, 40));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
        setOpaque(true);
    }

    /**
     * muda a cor do numero diferenciando
     * o do usuario com o da grade
     *
     * @param number        numero a ser trocado a cor.
     * @param userInput     é o usuario ou não.
     */
    public void setNumber(int number, boolean userInput) {
        setForeground(userInput ? Color.BLUE : Color.BLACK);
        setText(number > 0 ? number + "" : "");
    }

    public int getGridX() {
        return x;
    }

    public int getGridY() {
        return y;
    }
}