package br.com.cesarschool.poo.titulos.telas;
import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import br.com.cesarschool.poo.titulos.mediators.MediatorAcao;
import br.com.cesarschool.poo.titulos.mediators.MediatorOperacao;
import br.com.cesarschool.poo.titulos.mediators.MediatorTituloDivida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;

public class TelaMain extends JFrame {

    private JTabbedPane tabbedPane;

    public TelaMain() {
        setTitle("Sistema de Operações");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();

        // Criar as telas de cada operação
        JPanel telaOperacao = criarTelaOperacao();
        JPanel telaIncluir = criarTelaIncluir();
        JPanel telaBuscar = criarTelaBuscar();
        JPanel telaExcluir = criarTelaExcluir();
        JPanel telaEditar = criarTelaEditar();

        // Adicionar as telas como abas
        tabbedPane.addTab("Realizar Operação", telaOperacao);
        tabbedPane.addTab("Incluir", telaIncluir);
        tabbedPane.addTab("Buscar", telaBuscar);
        tabbedPane.addTab("Excluir", telaExcluir);
        tabbedPane.addTab("Editar/Atualizar", telaEditar);

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel criarTelaOperacao() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        JComboBox<String> comboEntidadeCredito = new JComboBox<>(new String[]{"Entidade 1", "Entidade 2", "Entidade 3"});
        JComboBox<String> comboEntidadeDebito = new JComboBox<>(new String[]{"Entidade 1", "Entidade 2", "Entidade 3"});
        JComboBox<String> comboAcaoTitulo = new JComboBox<>(new String[]{"Ação 1", "Título 1", "Ação 2", "Título 2"});
        JTextField valorField = new JTextField();
        JCheckBox ehAcaoCheckBox = new JCheckBox("É Ação");
        JButton realizarOperacaoButton = new JButton("Realizar Operação");

        panel.add(new JLabel("Entidade Crédito:"));
        panel.add(comboEntidadeCredito);

        panel.add(new JLabel("Entidade Débito:"));
        panel.add(comboEntidadeDebito);

        panel.add(new JLabel("Ação/Título:"));
        panel.add(comboAcaoTitulo);

        panel.add(new JLabel("Valor:"));
        panel.add(valorField);

        panel.add(ehAcaoCheckBox);
        panel.add(realizarOperacaoButton);

        realizarOperacaoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String entidadeCredito = (String) comboEntidadeCredito.getSelectedItem();
                String entidadeDebito = (String) comboEntidadeDebito.getSelectedItem();
                String acaoOuTitulo = (String) comboAcaoTitulo.getSelectedItem();
                boolean ehAcao = ehAcaoCheckBox.isSelected();
                double valor = Double.parseDouble(valorField.getText());

                MediatorOperacao mediator = MediatorOperacao.getInstancia();
                String resultado = null;
                try {
                    resultado = mediator.realizarOperacao(ehAcao, getEntidadeId(entidadeCredito), getEntidadeId(entidadeDebito), getAtivoId(acaoOuTitulo), valor);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(panel, resultado == null ? "Operação realizada com sucesso!" : resultado);
            }
        });

        return panel;
    }

    private JPanel criarTelaIncluir() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JTextField nomeField = new JTextField();
        JTextField taxaOuValorField = new JTextField();
        JComboBox<String> tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        JButton incluirButton = new JButton("Incluir");

        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);

        panel.add(new JLabel("Taxa ou Valor:"));
        panel.add(taxaOuValorField);

        panel.add(new JLabel("Tipo:"));
        panel.add(tipoAtivoCombo);

        panel.add(incluirButton);

        incluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String tipo = (String) tipoAtivoCombo.getSelectedItem();
                double taxaOuValor = Double.parseDouble(taxaOuValorField.getText());

                if (tipo.equals("Ação")) {
                    Acao acao = new Acao(1, nome, LocalDate.now().plusDays(365), taxaOuValor);
                    try {
                        MediatorAcao.getInstancia().incluir(acao);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    TituloDivida titulo = new TituloDivida(1, nome, LocalDate.now().plusDays(365), taxaOuValor);
                    MediatorTituloDivida.getInstancia().incluir(titulo);
                }

                JOptionPane.showMessageDialog(panel, "Inclusão realizada com sucesso!");
            }
        });

        return panel;
    }

    private JPanel criarTelaBuscar() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JComboBox<String> tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        JTextField idField = new JTextField();
        JButton buscarButton = new JButton("Buscar");
        JTextArea resultadoArea = new JTextArea();

        panel.add(new JLabel("Tipo:"));
        panel.add(tipoAtivoCombo);

        panel.add(new JLabel("ID:"));
        panel.add(idField);

        panel.add(buscarButton);
        panel.add(resultadoArea);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipo = (String) tipoAtivoCombo.getSelectedItem();
                int id = Integer.parseInt(idField.getText());

                if (tipo.equals("Ação")) {
                    Acao acao = null;
                    try {
                        acao = MediatorAcao.getInstancia().buscar(id);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    resultadoArea.setText(acao != null ? acao.getNome() : "Ação não encontrada");
                } else {
                    TituloDivida titulo = MediatorTituloDivida.getInstancia().buscar(id);
                    resultadoArea.setText(titulo != null ? titulo.getNome() : "Título não encontrado");
                }
            }
        });

        return panel;
    }

    private JPanel criarTelaExcluir() {
        JPanel panel = new JPanel(new GridLayout(3, 2));

        JComboBox<String> tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        JTextField idField = new JTextField();
        JButton excluirButton = new JButton("Excluir");

        panel.add(new JLabel("Tipo:"));
        panel.add(tipoAtivoCombo);

        panel.add(new JLabel("ID:"));
        panel.add(idField);

        panel.add(excluirButton);

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipo = (String) tipoAtivoCombo.getSelectedItem();
                int id = Integer.parseInt(idField.getText());

                if (tipo.equals("Ação")) {
                    String resultado = null;
                    try {
                        resultado = MediatorAcao.getInstancia().excluir(id);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(panel, resultado == null ? "Ação excluída com sucesso!" : resultado);
                } else {
                    String resultado = MediatorTituloDivida.getInstancia().excluir(id);
                    JOptionPane.showMessageDialog(panel, resultado == null ? "Título excluído com sucesso!" : resultado);
                }
            }
        });

        return panel;
    }

    private JPanel criarTelaEditar() {
        JPanel panel = new JPanel(new GridLayout(6, 2));

        JComboBox<String> tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        JTextField idField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField valorField = new JTextField();
        JButton buscarButton = new JButton("Buscar");
        JButton editarButton = new JButton("Atualizar");

        panel.add(new JLabel("Tipo:"));
        panel.add(tipoAtivoCombo);

        panel.add(new JLabel("ID:"));
        panel.add(idField);

        panel.add(buscarButton);
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);

        panel.add(new JLabel("Valor/Taxa:"));
        panel.add(valorField);

        panel.add(editarButton);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipo = (String) tipoAtivoCombo.getSelectedItem();
                int id = Integer.parseInt(idField.getText());

                if (tipo.equals("Ação")) {
                    Acao acao = null;
                    try {
                        acao = MediatorAcao.getInstancia().buscar(id);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (acao != null) {
                        nomeField.setText(acao.getNome());
                        valorField.setText(String.valueOf(acao.getValorUnitario()));
                    } else {
                        JOptionPane.showMessageDialog(panel, "Ação não encontrada");
                    }
                } else {
                    TituloDivida titulo = MediatorTituloDivida.getInstancia().buscar(id);
                    if (titulo != null) {
                        nomeField.setText(titulo.getNome());
                        valorField.setText(String.valueOf(titulo.getTaxaJuros()));
                    } else {
                        JOptionPane.showMessageDialog(panel, "Título não encontrado");
                    }
                }
            }
        });

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipo = (String) tipoAtivoCombo.getSelectedItem();
                int id = Integer.parseInt(idField.getText());
                String nome = nomeField.getText();
                double valor = Double.parseDouble(valorField.getText());

                if (tipo.equals("Ação")) {
                    Acao acao = new Acao(id, nome, LocalDate.now().plusDays(365), valor);
                    String resultado = null;
                    try {
                        resultado = MediatorAcao.getInstancia().alterar(acao);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(panel, resultado == null ? "Ação atualizada com sucesso!" : resultado);
                } else {
                    TituloDivida titulo = new TituloDivida(id, nome, LocalDate.now().plusDays(365), valor);
                    String resultado = MediatorTituloDivida.getInstancia().alterar(titulo);
                    JOptionPane.showMessageDialog(panel, resultado == null ? "Título atualizado com sucesso!" : resultado);
                }
            }
        });

        return panel;
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
        new TelaMain();
    }
}
