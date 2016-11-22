/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import Entidades.Jogador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author jennifer
 */
public class GUIJogo extends JFrame implements Serializable {

    private int idJog1;
    private int idJog2;

    private int x;
    private int y;

    private JPanel menuLateral = new JPanel();
    private JPanel pnBotoesAtDf = new JPanel();
    private JPanel pnNorte = new JPanel();

    private JLabel titulo = new JLabel("Místico");
    
    private JButton btQuit = new JButton("Quit");
    private JButton btAtaque = new JButton("Ataque");
    private JButton btMovimento = new JButton("Movimento");
    
    public GUIJogo(int idJog1, int idJog2) {
        this.idJog1 = idJog1;
        this.idJog2 = idJog2;

        this.x = 40;// tamanho do tabuleiro x
        this.y = 40;// tamanho do tabuleiro y

        setSize(this.y*50+50, this.x*29+50);
        setTitle("Místico");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        
        titulo.setFont( new Font("Courier New", Font.ITALIC, 40));
        pnNorte.add(titulo);

        Tabuleiro tabuleiro = new Tabuleiro(x,y);
        tabuleiro.setSize(this.x*29, this.y*29);
        
        menuLateral.setLayout(new BorderLayout());
        menuLateral.setBackground(Color.CYAN);
        menuLateral.add(btQuit, BorderLayout.NORTH);
        menuLateral.add(new JLabel("Menu Lateral"), BorderLayout.CENTER);
        
        menuLateral.add(pnBotoesAtDf, BorderLayout.SOUTH);
        pnBotoesAtDf.setLayout(new GridLayout(2,1));
        pnBotoesAtDf.add(btAtaque);
        pnBotoesAtDf.add(btMovimento);
        
        
        
        add(tabuleiro, BorderLayout.CENTER);
        add(menuLateral, BorderLayout.EAST);
        add(pnNorte, BorderLayout.NORTH);
        
        btQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

}
