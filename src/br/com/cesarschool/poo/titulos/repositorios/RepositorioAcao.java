package br.com.cesarschool.poo.titulos.repositorios;
import br.com.cesarschool.poo.titulos.entidades.Acao;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

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

	private static final String FILE_NAME = "Acao.txt";


	public boolean incluir(Acao acao) throws IOException {
		List<Acao> acoes = listarAcoes();

		for (Acao a : acoes) {
			if (a.getIdentificador() == acao.getIdentificador()) {
				return false;
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
			writer.write(formatAcao(acao));
			writer.newLine();
		}
		return true;
	}


	public boolean alterar(Acao acao) throws IOException {
		List<Acao> acoes = listarAcoes();
		boolean encontrado = false;
		for (int i = 0; i < acoes.size(); i++) {
			if (acoes.get(i).getIdentificador() == acao.getIdentificador()) {
				acoes.set(i, acao);
				encontrado = true;
				break;
			}
		}
		if (!encontrado) {
			return false;
		}
		salvarAcoes(acoes);
		return true;
	}

	public boolean excluir(int identificador) throws IOException {
		List<Acao> acoes = listarAcoes();
		boolean encontrado = false;
		Iterator<Acao> iterator = acoes.iterator();
		while (iterator.hasNext()) {
			Acao acao = iterator.next();
			if (acao.getIdentificador() == identificador) {
				iterator.remove();
				encontrado = true;
				break;
			}
		}
		if (!encontrado) {
			return false;
		}
		salvarAcoes(acoes);
		return true;
	}

	public Acao buscar(int identificador) throws IOException {
		List<Acao> acoes = listarAcoes();
		for (Acao acao : acoes) {
			if (acao.getIdentificador() == identificador) {
				return acao;
			}
		}
		return null;
	}

	private List<Acao> listarAcoes() throws IOException {
		List<Acao> acoes = new ArrayList<>();
		Path path = Paths.get(FILE_NAME);
		if (!Files.exists(path)) {
			return acoes;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				Acao acao = parseAcao(linha);
				if (acao != null) {
					acoes.add(acao);
				}
			}
		}
		return acoes;
	}

	private void salvarAcoes(List<Acao> acoes) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
			for (Acao acao : acoes) {
				writer.write(formatAcao(acao));
				writer.newLine();
			}
		}
	}

	private String formatAcao(Acao acao) {
		return acao.getIdentificador() + ";" + acao.getNome() + ";" + acao.getDataDeValidade() + ";" + acao.getValorUnitario();
	}

	private Acao parseAcao(String linha) {
		String[] partes = linha.split(";");
		if (partes.length != 4) {
			return null;
		}
		int identificador = Integer.parseInt(partes[0]);
		String nome = partes[1];
		LocalDate dataDeValidade = LocalDate.parse(partes[2]);
		double valorUnitario = Double.parseDouble(partes[3]);
		return new Acao(identificador, nome, dataDeValidade, valorUnitario);
	}
}
