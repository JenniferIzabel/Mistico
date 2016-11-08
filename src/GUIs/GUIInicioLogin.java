/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author jennifer
 */
public class GUIInicioLogin extends JFrame{

    private Container cp = new Container();
    private JPanel pnNorte = new JPanel();
    private JPanel pnCentro = new JPanel();
    private JPanel pnSul = new JPanel();
    
    //NORTE
    private JLabel lbTitulo = new JLabel("MÍSTICO");
    
    //CENTRO
    private JLabel lbJogadorA = new JLabel("JOGADOR A"); 

    private JLabel lbJogador1 = new JLabel("Login"); 
    private JTextField tfJogador1 = new JTextField();

    private JLabel lbSenha1 = new JLabel("Senha"); 
    private JTextField tfSenha1 = new JTextField(); 

    private JLabel lbJogadorB = new JLabel("JOGADOR B"); 

    private JLabel lbJogador2 = new JLabel("Login"); 
    private JTextField tfJogador2 = new JTextField(); 

    private JLabel lbSenha2 = new JLabel("Senha");
    private JTextField tfSenha2 = new JTextField();

    private JButton btCriarUsuario = new JButton("Novo Usuario");
    private JButton btOk = new JButton("OK");
    
    private JLabel vazia1 = new JLabel("");
    private JLabel vazia2 = new JLabel("");
    private int i = 0; // iterador da lista vazia
    
    //SUL
    private JLabel lbAviso = new JLabel(".");
    

    public GUIInicioLogin() {
        setSize(300, 300);
        setTitle("Login");
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cp = getContentPane();
        cp.setLayout(new BorderLayout());
       
        pnCentro.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        //formatando e adicionando o título
        Font f = new Font("Courier New", Font.ITALIC, 40);
        lbTitulo.setFont(f);
        pnNorte.add(lbTitulo);

        //adicionando companentes no painel central
        addC(vazia1, lbJogadorA, pnCentro);
        addC(lbJogador1, tfJogador1, pnCentro);
        addC(lbSenha1, tfSenha1, pnCentro);
        addC(vazia2, lbJogadorB, pnCentro);
        addC(lbJogador2, tfJogador2, pnCentro);
        addC(lbSenha2, tfSenha2, pnCentro);
        addC(btCriarUsuario, btOk, pnCentro);
        
        //adicionando label de aviso
        pnSul.add(lbAviso);
        
        btOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GUIPrincipal guiPrincipal = new GUIPrincipal(1, 2, 16, 16);
                dispose();
            }
        });
        
        btCriarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GUINovoJogador guicrudJogador = new GUINovoJogador();
            }
        });
        
        cp.add(pnNorte, BorderLayout.NORTH);
        cp.add(pnCentro, BorderLayout.CENTER);
        cp.add(pnSul, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
   
////////////////////////////////////////////////////////////////////////////////
    }///// FIM DO CONSTRUTOR DA GUI ////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

    public void addC(JLabel label, JComponent componente, JPanel panel) {
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.NORTHEAST;
        cons.insets = new Insets(4, 4, 4, 4);

        cons.weightx = 0;
        cons.gridwidth = 1;
        panel.add(label, cons);

        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(componente, cons);
    }

    public void addC(JButton botao, JComponent componente, JPanel panel) {
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.NORTHEAST;
        cons.insets = new Insets(4, 4, 4, 4);

        cons.weightx = 0;
        cons.gridwidth = 1;
        panel.add(botao, cons);

        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(componente, cons);
    }

}
