package br.com.cesarschool.poo.titulos.telas;

import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import br.com.cesarschool.poo.titulos.mediators.MediatorAcao;
import br.com.cesarschool.poo.titulos.mediators.MediatorTituloDivida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

public class TelaIncluir extends JFrame {

    private JTextField nomeField;
    private JTextField taxaOuValorField;
    private JComboBox<String> tipoAtivoCombo;
    private JButton incluirButton;

    public TelaIncluir() {
        setTitle("Incluir Título/Ação");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        nomeField = new JTextField();
        taxaOuValorField = new JTextField();
        tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});

        incluirButton = new JButton("Incluir");

        add(new JLabel("Nome:"));
        add(nomeField);

        add(new JLabel("Taxa ou Valor:"));
        add(taxaOuValorField);

        add(new JLabel("Tipo:"));
        add(tipoAtivoCombo);

        add(incluirButton);

        incluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    incluirAtivo();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setVisible(true);
    }

    private void incluirAtivo() throws IOException {
        String nome = nomeField.getText();
        String tipo = (String) tipoAtivoCombo.getSelectedItem();
        double taxaOuValor = Double.parseDouble(taxaOuValorField.getText());

        if (tipo.equals("Ação")) {
            Acao acao = new Acao(1, nome, LocalDate.now().plusDays(365), taxaOuValor);
            MediatorAcao.getInstancia().incluir(acao);
        } else {
            TituloDivida titulo = new TituloDivida(1, nome, LocalDate.now().plusDays(365), taxaOuValor);
            MediatorTituloDivida.getInstancia().incluir(titulo);
        }

        JOptionPane.showMessageDialog(this, "Inclusão realizada com sucesso!");
    }

    public static void main(String[] args) {
        new TelaIncluir();
    }
}

