/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.io.Serializable;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author jennifer
 */
public class GUIPrincipal extends JFrame implements Serializable {

    private int idJog1;
    private int idJog2;

    private int x;
    private int y;

    private JPanel menuLateral = new JPanel();

    public GUIPrincipal(int idJog1, int idJog2) {
        this.idJog1 = idJog1;
        this.idJog2 = idJog2;

        this.x = 16;// tamanho do tabuleiro x
        this.y = 16;// tamanho do tabuleiro y

        
        
        setSize(this.y*50+50, this.x*29+50);
        setTitle("Místico");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        Tabuleiro tabuleiro = new Tabuleiro(x,y);
        tabuleiro.setSize(this.x*29, this.y*29);
        
        JLabel jLabel = new JLabel("Menu Lateral");
        menuLateral.add(jLabel);
        menuLateral.setBackground(Color.CYAN);
        
        add(tabuleiro, BorderLayout.CENTER);
        add(menuLateral, BorderLayout.EAST);
        
        

        setLocationRelativeTo(null);
        setVisible(true);
    }

}
