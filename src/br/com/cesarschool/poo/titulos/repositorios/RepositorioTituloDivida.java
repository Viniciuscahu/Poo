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


public class RepositorioTituloDivida {

	private static final String FILE_NAME = "src/br/com/cesarschool/poo/titulos/repositorios/TituloDivida.txt";


	public boolean adicionarTitulo(TituloDivida titulo) throws IOException {
		List<TituloDivida> titulos = listarTitulos();

		for (TituloDivida t : titulos) {
			if (t.getIdentificador() == titulo.getIdentificador()) {
				return false;
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
			writer.write(formatTitulo(titulo));
			writer.newLine();
		}
		return true;
	}


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


	public TituloDivida buscarTitulo(int identificador) throws IOException {
		List<TituloDivida> titulos = listarTitulos();

		for (TituloDivida titulo : titulos) {
			if (titulo.getIdentificador() == identificador) {
				return titulo;
			}
		}

		return null;
	}


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


	private void salvarTitulos(List<TituloDivida> titulos) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
			for (TituloDivida titulo : titulos) {
				writer.write(formatTitulo(titulo));
				writer.newLine();
			}
		}
	}


	private String formatTitulo(TituloDivida titulo) {
		return titulo.getIdentificador() + ";" + titulo.getNome() + ";" + titulo.getDataDeValidade() + ";" + titulo.getTaxaJuros();
	}


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
