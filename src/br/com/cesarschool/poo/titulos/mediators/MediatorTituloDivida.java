package br.com.cesarschool.poo.titulos.mediators;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import br.com.cesarschool.poo.titulos.repositorios.RepositorioTituloDivida;
import java.time.LocalDate;

public class MediatorTituloDivida {

    private static MediatorTituloDivida instancia;
    private static final RepositorioTituloDivida repositorioTituloDivida = new RepositorioTituloDivida();

    private MediatorTituloDivida() { }

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
        LocalDate dataAtual = LocalDate.now();
        if (titulo.getDataDeValidade().isBefore(dataAtual.plusDays(180))) {
            return "Data de validade deve ser pelo menos 180 dias após a data atual.";
        }
        if (titulo.getTaxaJuros() < 0) {
            return "Taxa de juros deve ser maior ou igual a zero.";
        }
        return null;
    }

    public String incluir(TituloDivida titulo) {
        String validacao = validar(titulo);
        if (validacao != null) {
            return validacao;
        }
        try {
            boolean incluido = repositorioTituloDivida.adicionarTitulo(titulo);
            if (incluido) {
                return null;
            } else {
                return "Título já existente.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro de I/O ao incluir o título de dívida.";
        }
    }

    public String alterar(TituloDivida titulo) {
        String validacao = validar(titulo);
        if (validacao != null) {
            return validacao;
        }
        try {
            boolean alterado = repositorioTituloDivida.atulizarTitulo(titulo);
            if (alterado) {
                return null;
            } else {
                return "Título inexistente.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro de I/O ao alterar o título de dívida.";
        }
    }

    public String excluir(int identificador) {
        if (identificador <= 0 || identificador >= 100000) {
            return "Identificador deve estar entre 1 e 99999.";
        }
        try {
            boolean excluido = repositorioTituloDivida.excluirTitulo(identificador);
            if (excluido) {
                return null;
            } else {
                return "Título inexistente.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro de I/O ao excluir o título de dívida.";
        }
    }

    public TituloDivida buscar(int identificador) {
        if (identificador <= 0 || identificador >= 100000) {
            return null;
        }
        try {
            return repositorioTituloDivida.buscarTitulo(identificador);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
