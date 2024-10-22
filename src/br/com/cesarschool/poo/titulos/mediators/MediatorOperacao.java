package br.com.cesarschool.poo.titulos.mediators;

import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;
import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import br.com.cesarschool.poo.titulos.entidades.Transacao;
import br.com.cesarschool.poo.titulos.repositorios.RepositorioTransacao;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MediatorOperacao {

    private static MediatorOperacao instancia;
    private static final MediatorAcao mediatorAcao = MediatorAcao.getInstancia();
    private static final MediatorTituloDivida mediatorTituloDivida = MediatorTituloDivida.getInstancia();
    private static final MediatorEntidadeOperadora mediatorEntidadeOperadora = MediatorEntidadeOperadora.getInstancia();
    private static final RepositorioTransacao repositorioTransacao = new RepositorioTransacao();
    private MediatorOperacao() { }

    public static MediatorOperacao getInstancia() {
        if (instancia == null) {
            instancia = new MediatorOperacao();
        }
        return instancia;
    }

    public String realizarOperacao(boolean ehAcao, int entidadeCredito, int idEntidadeDebito, int idAcaoOuTitulo, double valor) throws IOException {
        if (valor <= 0) {
            return "Valor inválido.";
        }
        EntidadeOperadora entidadeCredora = mediatorEntidadeOperadora.buscar(entidadeCredito);
        if (entidadeCredora == null) {
            return "Entidade crédito inexistente.";
        }
        EntidadeOperadora entidadeDevedora = mediatorEntidadeOperadora.buscar(idEntidadeDebito);
        if (entidadeDevedora == null) {
            return "Entidade débito inexistente.";
        }
        if (ehAcao && !entidadeCredora.getAutorizadoAcao()) {
            return "Entidade de crédito não autorizada para ação.";
        }
        if (ehAcao && !entidadeDevedora.getAutorizadoAcao()) {
            return "Entidade de débito não autorizada para ação.";
        }
        Acao acao = null;
        TituloDivida tituloDivida = null;
        if (ehAcao) {
            acao = mediatorAcao.buscar(idAcaoOuTitulo);
            if (acao == null) {
                return "Ação não encontrada.";
            }
        } else {
            tituloDivida = mediatorTituloDivida.buscar(idAcaoOuTitulo);
            if (tituloDivida == null) {
                return "Título de dívida não encontrado.";
            }
        }
        if (ehAcao) {
            if (entidadeDevedora.getSaldoAcao() < valor) {
                return "Saldo da entidade débito insuficiente.";
            }
        } else {
            if (entidadeDevedora.getSaldoTituloDivida() < valor) {
                return "Saldo da entidade débito insuficiente.";
            }
        }
        if (ehAcao && acao.getValorUnitario() > valor) {
            return "Valor da operação é menor do que o valor unitário da ação.";
        }
        double valorOperacao;
        if (ehAcao) {
            valorOperacao = valor;
        } else {
            valorOperacao = tituloDivida.calcularPrecoTransacao(valor);
        }
        if (ehAcao) {
            entidadeCredora.creditarSaldoAcao(valorOperacao);
        } else {
            entidadeCredora.creditarSaldoTituloDivida(valorOperacao);
        }
        if (ehAcao) {
            entidadeDevedora.debitarSaldoAcao(valorOperacao);
        } else {
            entidadeDevedora.debitarSaldoTituloDivida(valorOperacao);
        }
        String retornoAlterarCredora = mediatorEntidadeOperadora.alterar(entidadeCredora);
        if (retornoAlterarCredora != null) {
            return retornoAlterarCredora;
        }
        String retornoAlterarDevedora = mediatorEntidadeOperadora.alterar(entidadeDevedora);
        if (retornoAlterarDevedora != null) {
            return retornoAlterarDevedora;
        }
        Transacao transacao = new Transacao(
                entidadeCredora,
                entidadeDevedora,
                ehAcao ? acao : null,
                ehAcao ? null : tituloDivida,
                valorOperacao,
                LocalDateTime.now()
        );
        repositorioTransacao.incluir(transacao);

        return null;
    }
    public Transacao[] gerarExtrato(int entidade) {
        List<Transacao> todasTransacoes = new ArrayList<>();

        Transacao[] transacoesCredoras = repositorioTransacao.buscarPorEntidadeCredora(entidade);

        todasTransacoes.addAll(Arrays.asList(transacoesCredoras));

        Transacao[] transacoesDevedoras = repositorioTransacao.buscarPorEntidadeDevedora(entidade);

        todasTransacoes.addAll(Arrays.asList(transacoesDevedoras));

        Transacao[] resultado = todasTransacoes.toArray(new Transacao[0]);

        Arrays.sort(resultado, Comparator.comparing(Transacao::getDataHoraOperacao).reversed());

        return resultado;
    }
}
