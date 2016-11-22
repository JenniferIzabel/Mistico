/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import Entidades.Personagem;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author jennifer
 */
public class GUIDescricao extends JFrame {

    private Container cp = new Container();
    private JPanel pnNorte = new JPanel();
    private JPanel pnCentro = new JPanel();
    private JPanel pnSul = new JPanel();
    private JLabel[] labels;
    private JLabel lbTitulo = new JLabel("Personagens");
    private JButton btVoltar = new JButton("Voltar");
    private JScrollPane scroll;

    List<Personagem> listaPersonagens;

    public GUIDescricao(List<Personagem> personagens) {
        this.listaPersonagens = personagens;

        setSize(500, 500);
        setTitle("Descrições");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cp = getContentPane();
        cp.setLayout(new BorderLayout());


        ////NORTE
        Font f = new Font("Courier New", Font.ITALIC, 30);
        lbTitulo.setFont(f);
        pnNorte.add(lbTitulo);

        ////CENTRO
        scroll = new JScrollPane(pnCentro);
        add(scroll);
        
        labels = new JLabel[listaPersonagens.size()];

        pnCentro.setLayout(new GridLayout(listaPersonagens.size() * 2, 1));

        int i = 0;
        for (Personagem p : listaPersonagens) {
            JLabel nome = new JLabel(listaPersonagens.get(i).getNome());
            Font fo = new Font("Courier New", Font.CENTER_BASELINE, 16);
            nome.setFont(fo);

            String descricao = listaPersonagens.get(i).getDescricao();
            labels[i] = new JLabel(descricao);

            pnCentro.add(nome);
            pnCentro.add(labels[i]);
            i++;
        }

        ////SUL
        btVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                dispose();
            }
        });

        pnSul.add(btVoltar);

        cp.add(pnNorte, BorderLayout.NORTH);
        cp.add(pnCentro, BorderLayout.CENTER);
        cp.add(pnSul, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);

    }

}
