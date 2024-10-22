package br.com.cesarschool.poo.titulos.repositorios;

import br.com.cesarschool.poo.titulos.entidades.Acao;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/*
 * Deve gravar em e ler de um arquivo texto chamado Acao.txt os dados dos objetos do tipo
 * Acao. Seguem abaixo exemplos de linhas (identificador, nome, dataValidade, valorUnitario)
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
public class RepositorioAcao {
	Path arquivo = Paths.get("Acao.txt");

	public boolean incluir(Acao acao) {
		try(BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
			String linha;
			while((linha = reader.readLine()) != null) {
				String[] informacoes = linha.split(";");
				if(informacoes[0].equals(String.valueOf(acao.getNome()))) {
					return false;
				}
			}
		}catch (IOException e) {
			return false;
		}

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile(), true))){
			writer.write(acao.getIdentificador() + ";" + acao.getNome() + ";" + acao.getDataDeValidade() + ";" + acao.getValorUnitario());
			writer.newLine();
			return true;
		} catch (IOException e) {
			return false;
		}
	}




	public boolean alterar(Acao acao) {
		List<String> novasLinhas = new ArrayList<>();

		boolean troca = false;

		try(BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
			String linha;
			while((linha = reader.readLine()) != null) {
				String[] informacoes = linha.split(";");
				if(informacoes[0].equals(String.valueOf(acao.getIdentificador()))) {
					troca = true;
				}else{
					novasLinhas.add(linha);
				}
			}
			}catch (Exception e) {
			return false;
		}
		if(troca == true) {
			try(BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivo.toFile()))) {
				for(String linha : novasLinhas) {
					escritor.write(linha);
					escritor.newLine();


				}
				return true;
			} catch (Exception e) {
				return false;
			}
		}else {
			return false;
		}
	}



	public boolean excluir(int identificador) {
		List <String> novasLinhas = new ArrayList<>();

		boolean apagado = false;

		try(BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
			String linha;
			while((linha = reader.readLine())!= null) {
				String[] informacoes = linha.split(";");
				if(informacoes[0].equals(String.valueOf(identificador)) == false) {
					novasLinhas.add(linha);
				}else{
					apagado = true;
				}
			}
		} catch (IOException e) {
			return false;
		}
		if(apagado == true) {
			try(BufferedWriter escrever = new BufferedWriter(new FileWriter(arquivo.toFile()))) {
				for(String linha : novasLinhas) {
					escrever.write(linha);
					escrever.newLine();
				}
				return true;
			}catch (Exception e) {
				return false;
			}
		}
		else {
			return false;
		}
	}


	public Acao buscar(int identificador) {
		try(BufferedReader reader = new BufferedReader(new FileReader(arquivo.toFile()))) {
			String linha;
			while ((linha = reader.readLine())!= null) {
				String[] informacoes = linha.split(";");
				if(informacoes[0].equals(String.valueOf(identificador)) == true) {
					return new Acao(Integer.parseInt(informacoes[0]), informacoes[1], LocalDate.parse(informacoes[2]), Double.parseDouble(informacoes[3]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
