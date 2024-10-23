package br.com.cesarschool.poo.titulos.repositorios;
import br.com.cesarschool.poo.titulos.entidades.Acao;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;


public class RepositorioAcao {

	private static final String FILE_NAME = "src/br/com/cesarschool/poo/titulos/repositorios/Acao.txt";



	public boolean incluir(Acao acao) throws IOException {
		List<Acao> acoes = listarAcoes();

		for (Acao a : acoes) {
			if (a.getIdentificador() == acao.getIdentificador()) {
				System.out.println("Ação com o mesmo identificador já existe.");
				return false;
			}
		}

		// Gravar no arquivo
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
			writer.write(formatAcao(acao));
			writer.newLine();
			System.out.println("Ação incluída com sucesso no arquivo.");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
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
			System.out.println("Arquivo não encontrado, lista vazia.");
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
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) { // Sobrescreve o arquivo
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
