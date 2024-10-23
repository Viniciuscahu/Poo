package br.com.cesarschool.poo.titulos.telas;

import br.com.cesarschool.poo.titulos.mediators.MediatorOperacao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TelaOperacao extends JFrame {

    private JComboBox<String> comboEntidadeCredito;
    private JComboBox<String> comboEntidadeDebito;
    private JComboBox<String> comboAcaoTitulo;
    private JTextField valorField;
    private JButton realizarOperacaoButton;
    private JCheckBox ehAcaoCheckBox;

    public TelaOperacao() {
        setTitle("Realizar Operação");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        comboEntidadeCredito = new JComboBox<>(new String[]{"Entidade 1", "Entidade 2", "Entidade 3"});
        comboEntidadeDebito = new JComboBox<>(new String[]{"Entidade 1", "Entidade 2", "Entidade 3"});
        comboAcaoTitulo = new JComboBox<>(new String[]{"Ação 1", "Título 1", "Ação 2", "Título 2"});

        valorField = new JTextField();

        ehAcaoCheckBox = new JCheckBox("É Ação");

        realizarOperacaoButton = new JButton("Realizar Operação");

        add(new JLabel("Entidade Crédito:"));
        add(comboEntidadeCredito);

        add(new JLabel("Entidade Débito:"));
        add(comboEntidadeDebito);

        add(new JLabel("Ação/Título:"));
        add(comboAcaoTitulo);

        add(new JLabel("Valor:"));
        add(valorField);

        add(ehAcaoCheckBox);
        add(realizarOperacaoButton);

        realizarOperacaoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    realizarOperacao();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setVisible(true);
    }

    private void realizarOperacao() throws IOException {

        String entidadeCredito = (String) comboEntidadeCredito.getSelectedItem();
        String entidadeDebito = (String) comboEntidadeDebito.getSelectedItem();
        String acaoOuTitulo = (String) comboAcaoTitulo.getSelectedItem();
        boolean ehAcao = ehAcaoCheckBox.isSelected();
        double valor = Double.parseDouble(valorField.getText());


        MediatorOperacao mediator = MediatorOperacao.getInstancia();
        String resultado = mediator.realizarOperacao(ehAcao, getEntidadeId(entidadeCredito), getEntidadeId(entidadeDebito), getAtivoId(acaoOuTitulo), valor);

        JOptionPane.showMessageDialog(this, resultado == null ? "Operação realizada com sucesso!" : resultado);
    }

    private int getEntidadeId(String entidadeNome) {

        switch (entidadeNome) {
            case "Entidade 1":
                return 1;
            case "Entidade 2":
                return 2;
            case "Entidade 3":
                return 3;
            default:
                return -1;
        }
    }

    private int getAtivoId(String ativoNome) {

        switch (ativoNome) {
            case "Ação 1":
                return 1;
            case "Título 1":
                return 2;
            case "Ação 2":
                return 3;
            case "Título 2":
                return 4;
            default:
                return -1;
        }
    }

    public static void main(String[] args) {
        new TelaOperacao();
    }
}
