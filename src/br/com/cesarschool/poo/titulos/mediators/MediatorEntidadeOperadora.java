package br.com.cesarschool.poo.titulos.mediators;

import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;
import br.com.cesarschool.poo.titulos.repositorios.RepositorioEntidadeOperadora;

public class MediatorEntidadeOperadora {
    private static MediatorEntidadeOperadora instancia;
    private final RepositorioEntidadeOperadora repositorioEntidadeOperadora = new RepositorioEntidadeOperadora();

    private MediatorEntidadeOperadora() {
        // Construtor privado para singleton
    }

    public static MediatorEntidadeOperadora getInstancia() {
        if (instancia == null) {
            instancia = new MediatorEntidadeOperadora();
        }
        return instancia;
    }

    private String validar(EntidadeOperadora entidade) {
        if (entidade.getIdentificador() <= 100 || entidade.getIdentificador() >= 1000000) {
            return "Identificador deve estar entre 100 e 1000000.";
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            return "Nome deve ser preenchido.";
        }
        if (entidade.getNome().length() < 5 || entidade.getNome().length() > 60) {
            return "Nome deve ter entre 5 e 60 caracteres.";
        }
        return null; // Entidade válida
    }

    public String incluir(EntidadeOperadora entidade) {
        String validacao = validar(entidade);
        if (validacao != null) {
            return validacao; // Se a validação falhar, retorna a mensagem de erro
        }
        boolean sucesso = repositorioEntidadeOperadora.incluir(entidade);
        return sucesso ? null : "Entidade já existente";
    }

    public String alterar(EntidadeOperadora entidade) {
        String validacao = validar(entidade);
        if (validacao != null) {
            return validacao; // Se a validação falhar, retorna a mensagem de erro
        }
        boolean sucesso = repositorioEntidadeOperadora.alterar(entidade);
        return sucesso ? null : "Entidade inexistente";
    }

    public String excluir(long identificador) {
        if (identificador <= 100 || identificador >= 1000000) {
            return "Identificador deve estar entre 100 e 1000000.";
        }
        boolean sucesso = repositorioEntidadeOperadora.excluir(identificador);
        return sucesso ? null : "Entidade inexistente";
    }

    public EntidadeOperadora buscar(long identificador) {
        if (identificador <= 100 || identificador >= 1000000) {
            return null; // Identificador inválido
        }
        return repositorioEntidadeOperadora.buscar(identificador);
    }
}
