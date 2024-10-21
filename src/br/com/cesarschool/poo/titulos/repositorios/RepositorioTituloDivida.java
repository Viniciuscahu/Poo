package br.com.cesarschool.poo.titulos.repositorios;

import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Deve gravar em e ler de um arquivo texto chamado TituloDivida.txt os dados dos objetos do tipo
 * TituloDivida. Seguem abaixo exemplos de linhas (identificador, nome, dataValidade, taxaJuros).
 *
    1;BRASIL;2024-12-12;10.5
    2;EUA;2026-01-01;1.5
    3;FRANCA;2027-11-11;2.5 
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
public class RepositorioTituloDivida {
	Path arquivo = Paths.get("TituloDivida.txt");
	public boolean adicionarTitulo(TituloDivida novoTitulo)
	{
		try(BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
			String linha;
			while((linha = reader.readLine())!= null) {
				String[] informacoesTitulo = linha.split(";");

		if(informacoesTitulo[0].equals((String.valueOf(novoTitulo.getIdentificador())))) {
			return false;
		}
			}
		} catch (IOException e ) {
			return false;
		}
		try(BufferedWriter writer = new BufferedWriter((new FileWriter(arquivo.toFile(), true)))) {
			writer.write(novoTitulo.getIdentificador() + ";" + novoTitulo.getNome() + ";" + novoTitulo.getdataDeValidade() + ";" + novoTitulo.getTaxaJuros());
			writer.newLine();
			return true;
		} catch (IOException e ) {
			return false;
		}
	}




	public boolean atulizarTitulo(TituloDivida tituloAtualizado) {
		List<String> informacoesAtualizadas = new ArrayList<>();
		boolean encontrouTitulo = false;

		try(BufferedReader reader  = new BufferedReader(new FileReader(arquivo.toFile()))) {
			String linha;
			while((linha = reader.readLine())!= null) {
				String[] informacoesTitulo = linha.split(";");
				if (informacoesTitulo[0].equals((String.valueOf(tituloAtualizado.getIdentificador())))) {
						informacoesAtualizadas.add(tituloAtualizado.getIdentificador() + ";" + tituloAtualizado.getNome() + ";" + tituloAtualizado.getdataDeValidade() + ";" + tituloAtualizado.getTaxaJuros());
						encontrouTitulo = true;
			} else {
					informacoesAtualizadas.add (linha);
				}
			}
		} catch (Exception e) {
			return false;
		}

		if(encontrouTitulo) {
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()))){
				for(String linha : informacoesAtualizadas) {
					writer.write(linha);
					writer.newLine();
				}
				return true;

			} catch (Exception e) {
				return false;
			}
		}else{
			return false;
		}
	}


	public boolean excluirTitulo(int identificador) {

		List<String> linhasRestantes = new ArrayList<>();
		boolean tituloRemovido = false;
		try(BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
			String linha;
			while((linha = reader.readLine())!= null) {
				String[] informacoesTitulo = linha.split(";");
				if(informacoesTitulo[0].equals((String.valueOf(identificador)))) {
					linhasRestantes.add(linha);
				} else{
					tituloRemovido = true;
				}
			}
		} catch (IOException e) {
			return false;
		}
		if(tituloRemovido) {
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()))) {
				for(String linha : linhasRestantes) {
					writer.write(linha);
					writer.newLine();
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		}else {
			return false;
		}
	}


	public Acao buscarTitulo(int identificador) {
		try(BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
			String linha;
			while((linha = reader.readLine())!= null) {
				String[] informacoesTitulo = linha.split(";");
				if(informacoesTitulo[0].equals((String.valueOf(identificador)))) {
					return new Acao(Integer.parseInt(informacoesTitulo[0]), String.valueOf(informacoesTitulo[1]), LocalDateTime.parse(informacoesTitulo[2]), Double.parseDouble(informacoesTitulo[3]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
