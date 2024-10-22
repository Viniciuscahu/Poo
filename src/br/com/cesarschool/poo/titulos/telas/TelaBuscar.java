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

public class TelaBuscar extends JFrame {

    private JComboBox<String> tipoAtivoCombo;
    private JTextField idField;
    private JButton buscarButton;
    private JTextArea resultadoArea;

    public TelaBuscar() {
        setTitle("Buscar Ativo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        idField = new JTextField();
        buscarButton = new JButton("Buscar");
        resultadoArea = new JTextArea();

        add(new JLabel("Tipo:"));
        add(tipoAtivoCombo);

        add(new JLabel("ID:"));
        add(idField);

        add(buscarButton);
        add(resultadoArea);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarAtivo();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setVisible(true);
    }

    private void buscarAtivo() throws IOException {
        String tipo = (String) tipoAtivoCombo.getSelectedItem();
        int id = Integer.parseInt(idField.getText());

        if (tipo.equals("Ação")) {
            Acao acao = MediatorAcao.getInstancia().buscar(id);
            resultadoArea.setText(acao != null ? acao.getNome() : "Ação não encontrada");
        } else {
            TituloDivida titulo = MediatorTituloDivida.getInstancia().buscar(id);
            resultadoArea.setText(titulo != null ? titulo.getNome() : "Título não encontrado");
        }
    }

    public static void main(String[] args) {
        new TelaBuscar();
    }
}
