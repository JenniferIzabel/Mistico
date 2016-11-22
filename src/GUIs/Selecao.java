/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIs;

import Entidades.Personagem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author jennifer
 */
public class Selecao extends JPanel {

    private JPanel painel = new JPanel();
    private JCheckBox[] personagens; //Lista com todo os personagens
    private List<Personagem> listaPersonagem;
    private List<Personagem> personagensSelecionados;
    private JScrollPane scroll;

    public Selecao(List<Personagem> lista) {
        this.listaPersonagem = lista;

        int tamanho = this.listaPersonagem.size();
        
        painel.setPreferredSize(new Dimension(550,600));
        painel.setBackground(Color.WHITE);
        painel.setLayout(new GridLayout(tamanho,1));
        
        personagens = new JCheckBox[tamanho];

        scroll = new JScrollPane(painel);
        add(scroll);
        
        int i = 0;
        for (Personagem p : this.listaPersonagem) {
            this.personagens[i] = new JCheckBox(p.toString());
            this.personagens[i].setSelected(false);
            painel.add(this.personagens[i]);
            i++;
        }
        
        for (int j = 0; j < personagens.length; j++) {
            if(personagens[j].isSelected()){
                personagensSelecionados.add(listaPersonagem.get(j));
        }
            
        }
    }

    public List<Personagem> getPersonagensSelecionados() {
        return personagensSelecionados;
    }
    
    

}
