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

public class TelaEditar extends JFrame {

    private JComboBox<String> tipoAtivoCombo;
    private JTextField idField;
    private JTextField nomeField;
    private JTextField valorField;
    private JButton buscarButton;
    private JButton editarButton;

    public TelaEditar() {
        setTitle("Editar Título/Ação");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        idField = new JTextField();
        nomeField = new JTextField();
        valorField = new JTextField();

        buscarButton = new JButton("Buscar");
        editarButton = new JButton("Atualizar");

        add(new JLabel("Tipo:"));
        add(tipoAtivoCombo);

        add(new JLabel("ID:"));
        add(idField);

        add(buscarButton);
        add(new JLabel("Nome:"));
        add(nomeField);

        add(new JLabel("Valor/Taxa:"));
        add(valorField);

        add(editarButton);

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

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editarAtivo();
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
            if (acao != null) {
                nomeField.setText(acao.getNome());
                valorField.setText(String.valueOf(acao.getValorUnitario()));
            } else {
                JOptionPane.showMessageDialog(this, "Ação não encontrada");
            }
        } else {
            TituloDivida titulo = MediatorTituloDivida.getInstancia().buscar(id);
            if (titulo != null) {
                nomeField.setText(titulo.getNome());
                valorField.setText(String.valueOf(titulo.getTaxaJuros()));
            } else {
                JOptionPane.showMessageDialog(this, "Título não encontrado");
            }
        }
    }

    private void editarAtivo() throws IOException {
        String tipo = (String) tipoAtivoCombo.getSelectedItem();
        int id = Integer.parseInt(idField.getText());
        String nome = nomeField.getText();
        double valor = Double.parseDouble(valorField.getText());

        if (tipo.equals("Ação")) {
            Acao acao = new Acao(id, nome, LocalDate.now().plusDays(365), valor);
            String resultado = MediatorAcao.getInstancia().alterar(acao);
            JOptionPane.showMessageDialog(this, resultado == null ? "Ação atualizada com sucesso!" : resultado);
        } else {
            TituloDivida titulo = new TituloDivida(id, nome, LocalDate.now().plusDays(365), valor);
            String resultado = MediatorTituloDivida.getInstancia().alterar(titulo);
            JOptionPane.showMessageDialog(this, resultado == null ? "Título atualizado com sucesso!" : resultado);
        }
    }

    public static void main(String[] args) {
        new TelaEditar();
    }
}

