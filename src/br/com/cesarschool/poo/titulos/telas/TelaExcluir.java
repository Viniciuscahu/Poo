package br.com.cesarschool.poo.titulos.telas;

import br.com.cesarschool.poo.titulos.mediators.MediatorAcao;
import br.com.cesarschool.poo.titulos.mediators.MediatorTituloDivida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TelaExcluir extends JFrame {

    private JComboBox<String> tipoAtivoCombo;
    private JTextField idField;
    private JButton excluirButton;

    public TelaExcluir() {
        setTitle("Excluir Ativo");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        idField = new JTextField();
        excluirButton = new JButton("Excluir");

        add(new JLabel("Tipo:"));
        add(tipoAtivoCombo);

        add(new JLabel("ID:"));
        add(idField);

        add(excluirButton);

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    excluirAtivo();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setVisible(true);
    }

    private void excluirAtivo() throws IOException {
        String tipo = (String) tipoAtivoCombo.getSelectedItem();
        int id = Integer.parseInt(idField.getText());

        if (tipo.equals("Ação")) {
            String resultado = MediatorAcao.getInstancia().excluir(id);
            JOptionPane.showMessageDialog(this, resultado == null ? "Ação excluída com sucesso!" : resultado);
        } else {
            String resultado = MediatorTituloDivida.getInstancia().excluir(id);
            JOptionPane.showMessageDialog(this, resultado == null ? "Título excluído com sucesso!" : resultado);
        }
    }

    public static void main(String[] args) {
        new TelaExcluir();
    }
}


