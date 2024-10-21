package br.com.cesarschool.poo.titulos.repositorios;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

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

    private final Path arquivo = Paths.get("EntidadeOperacao.txt");

    public boolean incluir(EntidadeOperadora entidadeOperadora) {
        if (buscar(entidadeOperadora.getIdentificador()) != null) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile(), true))) {
            writer.write(entidadeOperadora.getIdentificador() + ";" + entidadeOperadora.getNome() + ";" + entidadeOperadora.getAutorizadoAcao());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(EntidadeOperadora entidadeOperadora) {
        List<String> linhasNovas = new ArrayList<>();
        boolean encontrada = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados[0].equals(String.valueOf(entidadeOperadora.getIdentificador()))) {
                    linhasNovas.add(entidadeOperadora.getIdentificador() + ";" + entidadeOperadora.getNome() + ";" + entidadeOperadora.getAutorizadoAcao());
                    encontrada = true;
                } else {
                    linhasNovas.add(linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (encontrada) {
            return reescreverArquivo(linhasNovas);
        }
        return false;
    }

    public boolean excluir(long identificador) {
        List<String> linhasNovas = new ArrayList<>();
        boolean encontrada = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (!dados[0].equals(String.valueOf(identificador))) {
                    linhasNovas.add(linha);
                } else {
                    encontrada = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (encontrada) {
            return reescreverArquivo(linhasNovas);
        }
        return false;
    }

    public EntidadeOperadora buscar(long identificador) {
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados[0].equals(String.valueOf(identificador))) {
                    return new EntidadeOperadora(Long.parseLong(dados[0]), dados[1], Double.parseDouble(dados[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean reescreverArquivo(List<String> linhasNovas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()))) {
            for (String linha : linhasNovas) {
                writer.write(linha);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
