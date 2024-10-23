package br.com.cesarschool.poo.titulos.repositorios;
import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;


public class RepositorioEntidadeOperadora {

    private static final String FILE_NAME = "src/br/com/cesarschool/poo/titulos/repositorios/EntidadeOperadora.txt";

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
        return entidade.getIdentificador() + ";" + entidade.getNome() + ";" +
                entidade.getDataValidade() + ";" + entidade.getValorUnitario();
    }

    private EntidadeOperadora parseEntidade(String linha) {
        String[] partes = linha.split(";");
        if (partes.length != 4) {
            return null;
        }
        long identificador = Long.parseLong(partes[0]);
        String nome = partes[1];
        LocalDate dataValidade = LocalDate.parse(partes[2]);
        double valorUnitario = Double.parseDouble(partes[3]);
        return new EntidadeOperadora(identificador, nome, false, dataValidade, valorUnitario);
    }
}
