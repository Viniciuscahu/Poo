package br.com.cesarschool.poo.titulos.repositorios;

import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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

	private static final String FILE_NAME = "src/br/com/cesarschool/poo/titulos/repositorios/TituloDivida.txt";

	// Adiciona um novo título
	public boolean adicionarTitulo(TituloDivida titulo) throws IOException {
		List<TituloDivida> titulos = listarTitulos();

		for (TituloDivida t : titulos) {
			if (t.getIdentificador() == titulo.getIdentificador()) {
				return false; // Título já existe
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
			writer.write(formatTitulo(titulo));
			writer.newLine();
		}
		return true;
	}

	// Atualiza um título existente
	public boolean atulizarTitulo(TituloDivida titulo) throws IOException {
		List<TituloDivida> titulos = listarTitulos();
		boolean encontrado = false;

		for (int i = 0; i < titulos.size(); i++) {
			if (titulos.get(i).getIdentificador() == titulo.getIdentificador()) {
				titulos.set(i, titulo);
				encontrado = true;
				break;
			}
		}

		if (!encontrado) {
			return false;
		}

		salvarTitulos(titulos);
		return true;
	}

	// Exclui um título pelo identificador
	public boolean excluirTitulo(int identificador) throws IOException {
		List<TituloDivida> titulos = listarTitulos();
		boolean encontrado = false;

		for (int i = 0; i < titulos.size(); i++) {
			if (titulos.get(i).getIdentificador() == identificador) {
				titulos.remove(i);
				encontrado = true;
				break;
			}
		}

		if (!encontrado) {
			return false;
		}

		salvarTitulos(titulos);
		return true;
	}

	// Busca um título pelo identificador
	public TituloDivida buscarTitulo(int identificador) throws IOException {
		List<TituloDivida> titulos = listarTitulos();

		for (TituloDivida titulo : titulos) {
			if (titulo.getIdentificador() == identificador) {
				return titulo;
			}
		}

		return null; // Se não encontrar, retorna null
	}

	// Lista todos os títulos do arquivo
	private List<TituloDivida> listarTitulos() throws IOException {
		List<TituloDivida> titulos = new ArrayList<>();
		File file = new File(FILE_NAME);

		if (!file.exists()) {
			return titulos;
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String linha;

			while ((linha = reader.readLine()) != null) {
				TituloDivida titulo = parseTitulo(linha);
				if (titulo != null) {
					titulos.add(titulo);
				}
			}
		}

		return titulos;
	}

	// Salva todos os títulos no arquivo
	private void salvarTitulos(List<TituloDivida> titulos) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
			for (TituloDivida titulo : titulos) {
				writer.write(formatTitulo(titulo));
				writer.newLine();
			}
		}
	}

	// Formata um TituloDivida como string para salvar no arquivo
	private String formatTitulo(TituloDivida titulo) {
		return titulo.getIdentificador() + ";" + titulo.getNome() + ";" + titulo.getDataDeValidade() + ";" + titulo.getTaxaJuros();
	}

	// Converte uma string lida do arquivo para um objeto TituloDivida
	private TituloDivida parseTitulo(String linha) {
		String[] partes = linha.split(";");
		if (partes.length != 4) {
			return null;
		}

		int identificador = Integer.parseInt(partes[0]);
		String nome = partes[1];
		LocalDate dataDeValidade = LocalDate.parse(partes[2]);
		double taxaJuros = Double.parseDouble(partes[3]);

		return new TituloDivida(identificador, nome, dataDeValidade, taxaJuros);
	}
}
