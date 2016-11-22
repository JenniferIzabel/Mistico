/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import DAOs.JogadorJpaController;
import DAOs.PersonagemJpaController;
import Entidades.Jogador;
import Entidades.Personagem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author jennifer
 */
public class GUISelecionaPersonagens extends JFrame {

    private int idJog1;
    private int idJog2;

    private JPanel pnNorte = new JPanel();
    private JPanel pnDireito = new JPanel();
    private JPanel pnEsquerdo = new JPanel();
    private JPanel pnSul = new JPanel();

    private JLabel titulo = new JLabel("Selecionem os seus personagens");

    private JButton btCancelar = new JButton("CANCELAR");
    private JButton btDescricao = new JButton("DESCRIÇÃO DOS PERSONAGENS");
    private JButton btPronto = new JButton("PRONTO!");

    List<Personagem> listaPersonagems;
    List<Personagem> listaSelecionadosA;
    List<Personagem> listaSelecionadosB;

    public GUISelecionaPersonagens(int idJog1, int idJog2) {
        this.idJog1 = idJog1;
        this.idJog2 = idJog2;

        setSize(2050, 1200);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        obtemListaPersonagem();

        ////////NORTE
        titulo.setFont(new Font("Courier New", Font.ITALIC, 20));
        pnNorte.add(titulo);

        ////////DIREITA
        Selecao selecaoDireita = new Selecao(listaPersonagems);
        listaSelecionadosA = selecaoDireita.getPersonagensSelecionados();
        pnDireito.add(selecaoDireita);

        ////////ESQUERDA
        Selecao selecaoEsquerda = new Selecao(listaPersonagems);
        listaSelecionadosB = selecaoEsquerda.getPersonagensSelecionados();
        pnEsquerdo.add(selecaoEsquerda);

        ////////SUL
        btCancelar.setBackground(Color.red);
        pnSul.add(btCancelar);
        
        btDescricao.setBackground(Color.BLACK);
        btDescricao.setForeground(Color.WHITE);
        pnSul.add(btDescricao);
        
        btPronto.setBackground(Color.GREEN);
        pnSul.add(btPronto);

        //////////////BOTÂO
        btPronto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                GUIJogo guiPrincipal = new GUIJogo(idJog1, idJog2);
            }
        });
        
        btDescricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                GUIDescricao guiDescricao = new GUIDescricao(listaPersonagems);
            }
        });

        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GUIInicioLogin guiInicioLogin = new GUIInicioLogin();
                dispose();
            }
        });

        add(pnNorte, BorderLayout.NORTH);
        add(pnDireito, BorderLayout.WEST);
        add(pnEsquerdo, BorderLayout.EAST);
        add(pnSul, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void obtemListaPersonagem() {

        try {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("UP");
            PersonagemJpaController pjc = new PersonagemJpaController(factory);

            this.listaPersonagems = pjc.findPersonagemEntities();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Erro ao carregar os personagens. Tente novamente mais tarde"+"\n\n("+e+")");
        }
    }
}
