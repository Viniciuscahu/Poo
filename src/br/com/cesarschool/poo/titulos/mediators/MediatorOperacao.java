package br.com.cesarschool.poo.titulos.mediators;

import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;
import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import br.com.cesarschool.poo.titulos.entidades.Transacao;
import br.com.cesarschool.poo.titulos.repositorios.RepositorioTransacao;

import java.time.LocalDateTime;

public class MediatorOperacao {
    private static MediatorOperacao instancia;
    private final MediatorAcao mediatorAcao = MediatorAcao.getInstancia();
    private final MediatorTituloDivida mediatorTituloDivida = MediatorTituloDivida.getInstancia();
    private final MediatorEntidadeOperadora mediatorEntidadeOperadora = MediatorEntidadeOperadora.getInstancia();
    private final RepositorioTransacao repositorioTransacao = new RepositorioTransacao();

    private MediatorOperacao() {
        // Construtor privado para singleton
    }

    public static MediatorOperacao getInstancia() {
        if (instancia == null) {
            instancia = new MediatorOperacao();
        }
        return instancia;
    }

    public String realizarOperacao(boolean ehAcao, long idEntidadeCredito, long idEntidadeDebito, int idAcaoOuTitulo, double valor) {
        if (valor <= 0) {
            return "Valor inválido";
        }

        // Buscar entidades de crédito e débito
        EntidadeOperadora entidadeCredito = mediatorEntidadeOperadora.buscar(idEntidadeCredito);
        if (entidadeCredito == null) {
            return "Entidade crédito inexistente";
        }

        EntidadeOperadora entidadeDebito = mediatorEntidadeOperadora.buscar(idEntidadeDebito);
        if (entidadeDebito == null) {
            return "Entidade débito inexistente";
        }

        if (ehAcao) {
            // Verificar autorização para ações
            if (!entidadeCredito.getAutorizadoAcao()) {
                return "Entidade de crédito não autorizada para ação";
            }
            if (!entidadeDebito.getAutorizadoAcao()) {
                return "Entidade de débito não autorizada para ação";
            }

            // Buscar ação e validar
            Acao acao = mediatorAcao.buscar(idAcaoOuTitulo);
            if (acao == null) {
                return "Ação não encontrada";
            }

            if (entidadeDebito.getSaldoAcao() < valor) {
                return "Saldo da entidade débito insuficiente";
            }

            if (acao.getValorUnitario() > valor) {
                return "Valor da operação é menor do que o valor unitário da ação";
            }

            // Realizar operação de crédito/débito para ação
            entidadeCredito.creditarSaldoAcao(valor);
            entidadeDebito.debitarSaldoAcao(valor);

        } else {
            // Buscar título e validar
            TituloDivida tituloDivida = mediatorTituloDivida.buscar(idAcaoOuTitulo);
            if (tituloDivida == null) {
                return "Título não encontrado";
            }

            if (entidadeDebito.getSaldoTituloDivida() < valor) {
                return "Saldo da entidade débito insuficiente";
            }

            // Calcular valor da transação para título de dívida
            double valorOperacao = tituloDivida.calcularPrecoTransacao(valor);

            // Realizar operação de crédito/débito para título
            entidadeCredito.creditarSaldoTituloDivida(valorOperacao);
            entidadeDebito.debitarSaldoTituloDivida(valorOperacao);
        }

        // Alterar entidades no sistema
        String resultadoCredito = mediatorEntidadeOperadora.alterar(entidadeCredito);
        if (resultadoCredito != null) {
            return resultadoCredito;
        }

        String resultadoDebito = mediatorEntidadeOperadora.alterar(entidadeDebito);
        if (resultadoDebito != null) {
            return resultadoDebito;
        }

        // Criar e registrar transação
        Transacao transacao = new Transacao(
                entidadeCredito,
                entidadeDebito,
                ehAcao ? mediatorAcao.buscar(idAcaoOuTitulo) : null,
                ehAcao ? null : mediatorTituloDivida.buscar(idAcaoOuTitulo),
                valor,
                LocalDateTime.now()
        );
        repositorioTransacao.incluir(transacao);

        return null; // Operação bem-sucedida
    }

    public Transacao[] gerarExtrato(long idEntidade) {
        Transacao[] transacoesCredora = repositorioTransacao.buscarPorEntidadeCredora(idEntidade);
        Transacao[] transacoesDevedora = repositorioTransacao.buscarPorEntidadeDevedora(idEntidade);

        // Combinar arrays e ordenar por data/hora
        Transacao[] todasTransacoes = combinarArrays(transacoesCredora, transacoesDevedora);
        ordenarPorDataHora(todasTransacoes);

        return todasTransacoes;
    }

    private Transacao[] combinarArrays(Transacao[] transacoesCredora, Transacao[] transacoesDevedora) {
        Transacao[] todas = new Transacao[transacoesCredora.length + transacoesDevedora.length];
        System.arraycopy(transacoesCredora, 0, todas, 0, transacoesCredora.length);
        System.arraycopy(transacoesDevedora, 0, todas, transacoesCredora.length, transacoesDevedora.length);
        return todas;
    }

    private void ordenarPorDataHora(Transacao[] transacoes) {
        // Ordenar por data e hora de operação em ordem decrescente
        java.util.Arrays.sort(transacoes, (t1, t2) -> t2.getDataHoraOperacao().compareTo(t1.getDataHoraOperacao()));
    }
}
