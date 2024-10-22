package br.com.cesarschool.poo.titulos.repositorios;
import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/*
 * Deve gravar em e ler de um arquivo texto chamado Acao.txt os dados dos objetos do tipo
 * Acao. Seguem abaixo exemplos de linhas.
 *
    1;PETROBRAS;2024-12-12;30.33
    2;BANCO DO BRASIL;2026-01-01;21.21
    3;CORREIOS;2027-11-11;6.12 
 * 
 * A inclus�o deve adicionar uma nova linha ao arquivo. N�o � permitido incluir 
 * identificador repetido. Neste caso, o m�todo deve retornar false. Inclus�o com 
 * sucesso, retorno true.
 * 
 * A altera��o deve substituir a linha atual por uma nova linha. A linha deve ser 
 * localizada por identificador que, quando n�o encontrado, enseja retorno false. 
 * Altera��o com sucesso, retorno true.  
 *   
 * A exclus�o deve apagar a linha atual do arquivo. A linha deve ser 
 * localizada por identificador que, quando n�o encontrado, enseja retorno false. 
 * Exclus�o com sucesso, retorno true.
 * 
 * A busca deve localizar uma linha por identificador, materializar e retornar um 
 * objeto. Caso o identificador n�o seja encontrado no arquivo, retornar null.   
 */
public class RepositorioEntidadeOperadora {

    private static final String FILE_NAME = "EntidadeOperadora.txt";

    public boolean incluir(EntidadeOperadora entidade) throws IOException {
        List<EntidadeOperadora> entidades = listarEntidades();
        for (EntidadeOperadora e : entidades) {
            if (e.getIdentificador() == entidade.getIdentificador()) {
                return false;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(formatEntidade(entidade));
            writer.newLine();
        }
        return true;
    }

    public boolean alterar(EntidadeOperadora entidade) throws IOException {
        List<EntidadeOperadora> entidades = listarEntidades();
        boolean encontrado = false;
        for (int i = 0; i < entidades.size(); i++) {
            if (entidades.get(i).getIdentificador() == entidade.getIdentificador()) {
                entidades.set(i, entidade);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            return false;
        }

        salvarEntidades(entidades);
        return true;
    }

    public boolean excluir(long identificador) throws IOException {
        List<EntidadeOperadora> entidades = listarEntidades();
        boolean encontrado = false;
        Iterator<EntidadeOperadora> iterator = entidades.iterator();
        while (iterator.hasNext()) {
            EntidadeOperadora entidade = iterator.next();
            if (entidade.getIdentificador() == identificador) {
                iterator.remove();
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            return false;
        }

        salvarEntidades(entidades);
        return true;
    }

    public EntidadeOperadora buscar(long identificador) throws IOException {
        List<EntidadeOperadora> entidades = listarEntidades();
        for (EntidadeOperadora entidade : entidades) {
            if (entidade.getIdentificador() == identificador) {
                return entidade;
            }
        }
        return null;
    }

    private List<EntidadeOperadora> listarEntidades() throws IOException {
        List<EntidadeOperadora> entidades = new ArrayList<>();
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            return entidades;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                EntidadeOperadora entidade = parseEntidade(linha);
                if (entidade != null) {
                    entidades.add(entidade);
                }
            }
        }
        return entidades;
    }

    private void salvarEntidades(List<EntidadeOperadora> entidades) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (EntidadeOperadora entidade : entidades) {
                writer.write(formatEntidade(entidade));
                writer.newLine();
            }
        }
    }

    private String formatEntidade(EntidadeOperadora entidade) {
        return entidade.getIdentificador() + ";" + entidade.getNome() + ";" + entidade.getAutorizadoAcao() + ";" +
                entidade.getSaldoAcao() + ";" + entidade.getSaldoTituloDivida();
    }

    private EntidadeOperadora parseEntidade(String linha) {
        String[] partes = linha.split(";");
        if (partes.length != 5) {
            return null;
        }
        long identificador = Long.parseLong(partes[0]);
        String nome = partes[1];
        boolean autorizadoAcao = Boolean.parseBoolean(partes[2]);
        double saldoAcao = Double.parseDouble(partes[3]);
        double saldoTituloDivida = Double.parseDouble(partes[4]);
        return new EntidadeOperadora(identificador, nome, autorizadoAcao);
    }
}
