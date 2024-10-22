package br.com.cesarschool.poo.titulos.mediators;

import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.repositorios.RepositorioAcao;

import java.time.LocalDate;

public class MediatorAcao {
    private static MediatorAcao instancia;
    private final RepositorioAcao repositorioAcao = new RepositorioAcao();

    private MediatorAcao() {
        // Construtor privado para singleton
    }

    public static MediatorAcao getInstancia() {
        if (instancia == null) {
            instancia = new MediatorAcao();
        }
        return instancia;
    }

    private String validar(Acao acao) {
        if (acao.getIdentificador() <= 0 || acao.getIdentificador() >= 100000) {
            return "Identificador deve estar entre 1 e 99999.";
        }
        if (acao.getNome() == null || acao.getNome().trim().isEmpty()) {
            return "Nome deve ser preenchido.";
        }
        if (acao.getNome().length() < 10 || acao.getNome().length() > 100) {
            return "Nome deve ter entre 10 e 100 caracteres.";
        }
        if (acao.getDataDeValidade().isBefore(LocalDate.now().plusDays(30))) {
            return "Data de validade deve ter pelo menos 30 dias a partir da data atual.";
        }
        if (acao.getValorUnitario() <= 0) {
            return "Valor unitário deve ser maior que zero.";
        }
        return null; // Objeto é válido
    }

    public String incluir(Acao acao) {
        String validacao = validar(acao);
        if (validacao != null) {
            return validacao; // Se a validação falhar, retorna a mensagem de erro
        }
        boolean sucesso = repositorioAcao.incluir(acao);
        return sucesso ? null : "Ação já existente";
    }

    public String alterar(Acao acao) {
        String validacao = validar(acao);
        if (validacao != null) {
            return validacao;
        }
        boolean sucesso = repositorioAcao.alterar(acao);
        return sucesso ? null : "Ação inexistente";
    }

    public String excluir(int identificador) {
        if (identificador <= 0 || identificador >= 100000) {
            return "Identificador deve estar entre 1 e 99999.";
        }
        boolean sucesso = repositorioAcao.excluir(identificador);
        return sucesso ? null : "Ação inexistente";
    }

    public Acao buscar(int identificador) {
        if (identificador <= 0 || identificador >= 100000) {
            return null; // Identificador inválido
        }
        return repositorioAcao.buscar(identificador);
    }
}
