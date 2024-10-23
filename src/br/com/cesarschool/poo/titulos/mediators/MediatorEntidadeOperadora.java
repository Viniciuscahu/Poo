package br.com.cesarschool.poo.titulos.mediators;
import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;
import br.com.cesarschool.poo.titulos.repositorios.RepositorioEntidadeOperadora;
import java.io.IOException;
import java.time.LocalDate;

public class MediatorEntidadeOperadora {

    private static MediatorEntidadeOperadora instancia;
    private static final RepositorioEntidadeOperadora repositorioEntidadeOperadora = new RepositorioEntidadeOperadora();

    private MediatorEntidadeOperadora() { }

    public static MediatorEntidadeOperadora getInstancia() {
        if (instancia == null) {
            instancia = new MediatorEntidadeOperadora();
        }
        return instancia;
    }

    private String validar(EntidadeOperadora entidade) {
        if (entidade.getIdentificador() <= 0 || entidade.getIdentificador() >= 100000) {
            return "Identificador deve ser maior que zero e menor que 100000.";
        }
        if (entidade.getNome() == null || entidade.getNome().trim().isEmpty()) {
            return "Nome deve ser preenchido e não pode ser nulo ou vazio.";
        }
        if (entidade.getNome().length() < 5 || entidade.getNome().length() > 60) {
            return "Nome deve ter entre 5 e 60 caracteres.";
        }
        if (entidade.getDataValidade().isBefore(LocalDate.now().plusDays(180))) {
            return "Data de validade deve ser maior que a data atual mais 180 dias.";
        }
        if (entidade.getValorUnitario() <= 0) {
            return "Valor unitário deve ser maior que zero.";
        }
        return null;
    }

    public String incluir(EntidadeOperadora entidade) {
        String validacao = validar(entidade);
        if (validacao != null) {
            return validacao;
        }
        try {
            boolean incluido = repositorioEntidadeOperadora.incluir(entidade);
            if (incluido) {
                return null;
            } else {
                return "Entidade já existente.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro de I/O ao incluir a entidade.";
        }
    }

    public String alterar(EntidadeOperadora entidade) {
        String validacao = validar(entidade);
        if (validacao != null) {
            return validacao;
        }
        try {
            boolean alterado = repositorioEntidadeOperadora.alterar(entidade);
            if (alterado) {
                return null;
            } else {
                return "Entidade inexistente.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro de I/O ao alterar a entidade.";
        }
    }

    public String excluir(int identificador) {
        if (identificador <= 0 || identificador >= 100000) {
            return "Identificador deve ser maior que zero e menor que 100000.";
        }
        try {
            boolean excluido = repositorioEntidadeOperadora.excluir(identificador);
            if (excluido) {
                return null;
            } else {
                return "Entidade inexistente.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro de I/O ao excluir a entidade.";
        }
    }

    public EntidadeOperadora buscar(int identificador) {
        if (identificador <= 0 || identificador >= 100000) {
            return null;
        }
        try {
            return repositorioEntidadeOperadora.buscar(identificador);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
