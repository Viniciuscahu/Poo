package br.com.cesarschool.poo.titulos.mediators;

import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import br.com.cesarschool.poo.titulos.repositorios.RepositorioTituloDivida;

import java.time.LocalDate;

public class MediatorTituloDivida {
    private static MediatorTituloDivida instancia;
    private final RepositorioTituloDivida repositorioTituloDivida = new RepositorioTituloDivida();

    private MediatorTituloDivida() {
        // Construtor privado para singleton
    }

    public static MediatorTituloDivida getInstancia() {
        if (instancia == null) {
            instancia = new MediatorTituloDivida();
        }
        return instancia;
    }

    private String validar(TituloDivida titulo) {
        if (titulo.getIdentificador() <= 0 || titulo.getIdentificador() >= 100000) {
            return "Identificador deve estar entre 1 e 99999.";
        }
        if (titulo.getNome() == null || titulo.getNome().trim().isEmpty()) {
            return "Nome deve ser preenchido.";
        }
        if (titulo.getNome().length() < 10 || titulo.getNome().length() > 100) {
            return "Nome deve ter entre 10 e 100 caracteres.";
        }
        if (titulo.getDataDeValidade().isBefore(LocalDate.now().plusDays(180))) {
            return "Data de validade deve ter pelo menos 180 dias a partir da data atual.";
        }
        if (titulo.getTaxaJuros() < 0) {
            return "Taxa de juros deve ser maior ou igual a zero.";
        }
        return null; // Objeto é válido
    }

    public String incluir(TituloDivida titulo) {
        String validacao = validar(titulo);
        if (validacao != null) {
            return validacao; // Se a validação falhar, retorna a mensagem de erro
        }
        boolean sucesso = repositorioTituloDivida.adicionarTitulo(titulo);
        return sucesso ? null : "Título já existente";
    }

    public String alterar(TituloDivida titulo) {
        String validacao = validar(titulo);
        if (validacao != null) {
            return validacao; // Se a validação falhar, retorna a mensagem de erro
        }
        boolean sucesso = repositorioTituloDivida.atulizarTitulo(titulo);
        return sucesso ? null : "Título inexistente";
    }

    public String excluir(int identificador) {
        if (identificador <= 0 || identificador >= 100000) {
            return "Identificador deve estar entre 1 e 99999.";
        }
        boolean sucesso = repositorioTituloDivida.excluirTitulo(identificador);
        return sucesso ? null : "Título inexistente";
    }

    public TituloDivida buscar(int identificador) {
        if (identificador <= 0 || identificador >= 100000) {
            return null; // Identificador inválido
        }
        return repositorioTituloDivida.buscarTitulo(identificador);
    }
}
