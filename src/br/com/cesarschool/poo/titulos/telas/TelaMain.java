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
import java.time.format.DateTimeParseException;

public class TelaMain extends JFrame {

    private JTabbedPane tabbedPane;

    public TelaMain() {
        setTitle("Sistema de Operações");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();


        JPanel telaOperacao = criarTelaOperacao();
        JPanel telaIncluir = criarTelaIncluir();
        JPanel telaBuscar = criarTelaBuscar();
        JPanel telaExcluir = criarTelaExcluir();
        JPanel telaEditar = criarTelaEditar();


        tabbedPane.addTab("Realizar Operação", telaOperacao);
        tabbedPane.addTab("Incluir", telaIncluir);
        tabbedPane.addTab("Buscar", telaBuscar);
        tabbedPane.addTab("Excluir", telaExcluir);
        tabbedPane.addTab("Editar/Atualizar", telaEditar);

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel criarTelaOperacao() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> comboEntidadeCredito = new JComboBox<>(new String[]{"Entidade 1", "Entidade 2", "Entidade 3"});
        JComboBox<String> comboEntidadeDebito = new JComboBox<>(new String[]{"Entidade 1", "Entidade 2", "Entidade 3"});
        JComboBox<String> comboAcaoTitulo = new JComboBox<>(new String[]{"Ação 1", "Título 1", "Ação 2", "Título 2"});
        JTextField valorField = new JTextField();
        JCheckBox ehAcaoCheckBox = new JCheckBox("É Ação");
        JButton realizarOperacaoButton = new JButton("Realizar Operação");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Entidade Crédito:"), gbc);
        gbc.gridx = 1;
        panel.add(comboEntidadeCredito, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Entidade Débito:"), gbc);
        gbc.gridx = 1;
        panel.add(comboEntidadeDebito, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Ação/Título:"), gbc);
        gbc.gridx = 1;
        panel.add(comboAcaoTitulo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Valor:"), gbc);
        gbc.gridx = 1;
        panel.add(valorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(ehAcaoCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(realizarOperacaoButton, gbc);

        realizarOperacaoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String entidadeCredito = (String) comboEntidadeCredito.getSelectedItem();
                    String entidadeDebito = (String) comboEntidadeDebito.getSelectedItem();
                    String acaoOuTitulo = (String) comboAcaoTitulo.getSelectedItem();
                    boolean ehAcao = ehAcaoCheckBox.isSelected();
                    double valor = Double.parseDouble(valorField.getText());

                    MediatorOperacao mediator = MediatorOperacao.getInstancia();
                    String resultado = mediator.realizarOperacao(ehAcao, getEntidadeId(entidadeCredito), getEntidadeId(entidadeDebito), getAtivoId(acaoOuTitulo), valor);

                    JOptionPane.showMessageDialog(panel, resultado == null ? "Operação realizada com sucesso!" : resultado);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Erro no formato de valor! Certifique-se de inserir um número válido.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "Erro ao realizar a operação: " + ex.getMessage());
                }
            }
        });

        return panel;
    }

    private JPanel criarTelaIncluir() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField idField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField dataValidadeField = new JTextField();
        JTextField valorField = new JTextField();
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        JButton incluirButton = new JButton("Incluir");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Identificador:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Data de Validade (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(dataValidadeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Valor Unitário:"), gbc);
        gbc.gridx = 1;
        panel.add(valorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(tipoCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(incluirButton, gbc);

        incluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int identificador = Integer.parseInt(idField.getText());
                    String nome = nomeField.getText();

                    LocalDate dataValidade;
                    try {
                        dataValidade = LocalDate.parse(dataValidadeField.getText());
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(panel, "Data de validade inválida! Use o formato YYYY-MM-DD.");
                        return;
                    }

                    double valorUnitario = Double.parseDouble(valorField.getText());
                    String tipo = (String) tipoCombo.getSelectedItem();

                    String resultado;
                    if (tipo.equals("Ação")) {
                        Acao acao = new Acao(identificador, nome, dataValidade, valorUnitario);
                        resultado = MediatorAcao.getInstancia().incluir(acao);
                    } else {
                        TituloDivida tituloDivida = new TituloDivida(identificador, nome, dataValidade, valorUnitario);
                        resultado = MediatorTituloDivida.getInstancia().incluir(tituloDivida);
                    }

                    if (resultado == null) {
                        JOptionPane.showMessageDialog(panel, tipo + " incluído(a) com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(panel, resultado);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Erro nos dados numéricos! Verifique o identificador e valor unitário.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Erro ao incluir dados: " + ex.getMessage());
                }
            }
        });

        return panel;
    }

    private JPanel criarTelaBuscar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        JTextField idField = new JTextField();
        JButton buscarButton = new JButton("Buscar");
        JTextArea resultadoArea = new JTextArea(5, 20);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        resultadoArea.setEditable(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(tipoAtivoCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buscarButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        panel.add(scrollPane, gbc);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String tipo = (String) tipoAtivoCombo.getSelectedItem();
                    int id = Integer.parseInt(idField.getText());

                    if (tipo.equals("Ação")) {
                        Acao acao = MediatorAcao.getInstancia().buscar(id);
                        if (acao != null) {
                            resultadoArea.setText("Nome: " + acao.getNome() + "\n"
                                    + "Data de Validade: " + acao.getDataDeValidade().toString() + "\n"
                                    + "Valor Unitário: " + acao.getValorUnitario());
                        } else {
                            resultadoArea.setText("Ação não encontrada.");
                        }
                    } else {
                        TituloDivida titulo = MediatorTituloDivida.getInstancia().buscar(id);
                        if (titulo != null) {
                            resultadoArea.setText("Nome: " + titulo.getNome() + "\n"
                                    + "Data de Validade: " + titulo.getDataDeValidade().toString() + "\n"
                                    + "Taxa de Juros: " + titulo.getTaxaJuros());
                        } else {
                            resultadoArea.setText("Título não encontrado.");
                        }
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "ID inválido. Por favor, insira um número válido.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "Erro ao buscar os dados: " + ex.getMessage());
                }
            }
        });

        return panel;
    }

    private JPanel criarTelaExcluir() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        JTextField idField = new JTextField();
        JButton excluirButton = new JButton("Excluir");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(tipoAtivoCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(excluirButton, gbc);

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String tipo = (String) tipoAtivoCombo.getSelectedItem();
                    int id = Integer.parseInt(idField.getText());

                    String resultado;
                    if (tipo.equals("Ação")) {
                        resultado = MediatorAcao.getInstancia().excluir(id);
                    } else {
                        resultado = MediatorTituloDivida.getInstancia().excluir(id);
                    }
                    JOptionPane.showMessageDialog(panel, resultado == null ? tipo + " excluído(a) com sucesso!" : resultado);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Por favor, insira um número válido para o ID.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "Erro ao tentar excluir o item: " + ex.getMessage());
                }
            }
        });

        return panel;
    }

    private JPanel criarTelaEditar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<String> tipoAtivoCombo = new JComboBox<>(new String[]{"Ação", "Título de Dívida"});
        JTextField idField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField valorField = new JTextField();
        JTextField dataValidadeField = new JTextField();
        JButton buscarButton = new JButton("Buscar");
        JButton editarButton = new JButton("Atualizar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        panel.add(tipoAtivoCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buscarButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Valor/Taxa:"), gbc);
        gbc.gridx = 1;
        panel.add(valorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Data de Validade (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(dataValidadeField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(editarButton, gbc);

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
                        dataValidadeField.setText(acao.getDataDeValidade().toString());
                    } else {
                        JOptionPane.showMessageDialog(panel, "Ação não encontrada");
                    }
                } else {
                    TituloDivida titulo = MediatorTituloDivida.getInstancia().buscar(id);
                    if (titulo != null) {
                        nomeField.setText(titulo.getNome());
                        valorField.setText(String.valueOf(titulo.getTaxaJuros()));
                        dataValidadeField.setText(titulo.getDataDeValidade().toString());
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

                LocalDate dataValidade;
                try {
                    dataValidade = LocalDate.parse(dataValidadeField.getText());
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(panel, "Data de validade inválida! Use o formato YYYY-MM-DD.");
                    return;
                }

                if (tipo.equals("Ação")) {
                    Acao acao = new Acao(id, nome, dataValidade, valor);
                    String resultado = null;
                    try {
                        resultado = MediatorAcao.getInstancia().alterar(acao);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(panel, resultado == null ? "Ação atualizada com sucesso!" : resultado);
                } else {
                    TituloDivida titulo = new TituloDivida(id, nome, dataValidade, valor);
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

}
