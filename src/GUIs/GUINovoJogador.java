/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import DAOs.JogadorJpaController;
import Entidades.Jogador;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author a1136976
 */
public class GUINovoJogador extends JFrame {

    private Container cp;
    private String caminho = "";

    private JPanel pnNorte = new JPanel();
    private JPanel pnSul = new JPanel();
    private JPanel pnCentro = new JPanel();
    private JPanel pnLeste = new JPanel();
    private JPanel pnOeste = new JPanel();
    //norte
    private ImageIcon iconeNovo;
    private ImageIcon iconeExcluir;
    private ImageIcon iconeLimparCampos;
    private JButton btNovo;
    private JButton btExcluir;
    private JButton btLimparCampos;
    //oeste
    private JLabel lbUsuario = new JLabel("Usuario:");
    private JLabel lbSenha = new JLabel("Senha:");
    private JLabel lbConfSenha = new JLabel("Confirme a senha:");
    //centro
    private JTextField tfUsuario = new JTextField();
    private JPasswordField tfSenha = new JPasswordField();
    private JPasswordField tfConfSenha = new JPasswordField();
    //sul
    private JLabel lbAviso = new JLabel(".");


    public GUINovoJogador() {

        ///// Definições da janela /////////////////////////////////////////////////////
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cp = getContentPane();
        cp.setLayout(new BorderLayout());
        setTitle("Novo Jogador");

        ///// Painel Norte /////////////////////////////////////////////////////
        iconeNovo = new ImageIcon(getClass().getResource("/icones/simple-document-save.png"));
        btNovo = new JButton(iconeNovo);
        pnNorte.add(btNovo);

//        iconeExcluir = new ImageIcon(getClass().getResource("/icones/simple-cancel.png"));
//        btExcluir = new JButton(iconeExcluir);
//        pnNorte.add(btExcluir);

        iconeLimparCampos = new ImageIcon(getClass().getResource("/icones/simple-edit-clear-all.png"));
        btLimparCampos = new JButton(iconeLimparCampos);
        pnNorte.add(btLimparCampos);

        ///// Painel Oeste /////////////////////////////////////////////////////
        pnOeste.setLayout(new GridLayout(6, 1));

        pnOeste.add(lbSenha);

        ///// Painel Centro /////////////////////////////////////////////////////
        pnCentro.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        addC(lbUsuario, tfUsuario, pnCentro);
        addC(lbSenha, tfSenha, pnCentro);
        addC(lbConfSenha, tfConfSenha, pnCentro);

        ///// Painel Sul ///////////////////////////////////////////////////////
        pnSul.add(lbAviso);

        ////////////////////////////////////////////////////////////////////////
        cp.add(pnNorte, BorderLayout.NORTH);
        cp.add(pnSul, BorderLayout.SOUTH);
        cp.add(pnCentro, BorderLayout.CENTER);
        cp.add(pnLeste, BorderLayout.EAST);
        cp.add(pnOeste, BorderLayout.WEST);

        ///// Botões////////////////////////////////////////////////////////////
        btNovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tfSenha.getText().compareTo(tfConfSenha.getText()) != 0) {
                    lbAviso.setText("As senhas estão diferentes!!");
                    pnSul.setBackground(Color.RED);
                } else if (!verificaCampos()) {
                    lbAviso.setText("Preencha todos os campos");
                    pnSul.setBackground(Color.red);
                } else {
                    pnSul.setBackground(Color.GREEN);

                    int num, p = 2000;
                    
                    boolean aux = true; 
                    Jogador jogador = new Jogador(tfUsuario.getText(), tfSenha.getText(), 0, 0);

                    EntityManagerFactory factory = Persistence.createEntityManagerFactory("UP");

                    
                    ///encontrar id aleatorio
                    while (aux) {
                        num = encontraAleatorio(p);
                        try {
                            //tenta colocar o id gerado
                            jogador.setIdJogador(num);
                            aux = false;
                        } catch (Exception f) {
                            throw new RuntimeException(f);
                        }
                        p *= 10;
                    }
                    ///adiciona o jogador no banco
                    
                        System.out.println("entrou");
                        JogadorJpaController jjc = new JogadorJpaController(factory);
                        try {
                            jjc.create(jogador);
                        } catch (Exception ex) {
                            Logger.getLogger(GUINovoJogador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
                    lbAviso.setText("Inserido com sucesso");
                    pnSul.setBackground(Color.green);
                    btLimparCampos.doClick();
                }

            }
        });

//        btExcluir.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                btLimparCampos.doClick();
//            }
//        });

        btLimparCampos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfUsuario.setText("");
                tfSenha.setText("");
                tfConfSenha.setText("");
            }
        });

        ////////////////////////////////////////////////////////////
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

    public void addC(JComboBox combobox, JComponent componente, JPanel panel) {
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.NORTHEAST;
        cons.insets = new Insets(4, 4, 4, 4);

        cons.weightx = 0;
        cons.gridwidth = 1;
        panel.add(combobox, cons);

        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(componente, cons);
    }

    private boolean verificaCampos() {
        if (tfUsuario.getText().equals("")
                || tfSenha.getText().equals("")
                || tfConfSenha.getText().equals("")) {

            return false;
        }
        return true;
    }

    private int encontraAleatorio(int p) {
        Random gerador = new Random();
        return gerador.nextInt(p);
    }
}
