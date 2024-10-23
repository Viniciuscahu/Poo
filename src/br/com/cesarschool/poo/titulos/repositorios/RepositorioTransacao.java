package br.com.cesarschool.poo.titulos.repositorios;
import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;
import br.com.cesarschool.poo.titulos.entidades.Transacao;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTransacao {

	private static final String NOME_ARQUIVO = "Transacao.txt";
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public void incluir(Transacao transacao) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, true))) {
			writer.write(formatarTransacao(transacao));
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Transacao[] buscarPorEntidadeCredora(long identificadorEntidadeCredito) {
		List<Transacao> transacoes = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				Transacao transacao = parseTransacao(linha);
				if (transacao.getEntidadeCredito().getIdentificador() == identificadorEntidadeCredito) {
					transacoes.add(transacao);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transacoes.toArray(new Transacao[0]);
	}

	public Transacao[] buscarPorEntidadeDevedora(long identificadorEntidadeDebito) {
		List<Transacao> transacoes = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
			String linha;
			while ((linha = reader.readLine()) != null) {
				Transacao transacao = parseTransacao(linha);
				if (transacao.getEntidadeDebito().getIdentificador() == identificadorEntidadeDebito) {
					transacoes.add(transacao);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return transacoes.toArray(new Transacao[0]);
	}

	private String formatarTransacao(Transacao transacao) {
		return transacao.getEntidadeCredito().getIdentificador() + ";" +
				transacao.getEntidadeCredito().getNome() + ";" +
				transacao.getEntidadeDebito().getIdentificador() + ";" +
				transacao.getEntidadeDebito().getNome() + ";" +
				transacao.getValorOperacao() + ";" +
				transacao.getDataHoraOperacao().format(DATETIME_FORMATTER);
	}

	private Transacao parseTransacao(String linha) {
		String[] partes = linha.split(";");
		long idCredito = Long.parseLong(partes[0]);
		String nomeCredito = partes[1];
		long idDebito = Long.parseLong(partes[2]);
		String nomeDebito = partes[3];
		double valorOperacao = Double.parseDouble(partes[4]);
		LocalDateTime dataHoraOperacao = LocalDateTime.parse(partes[5], DATETIME_FORMATTER);

		EntidadeOperadora entidadeCredito = new EntidadeOperadora(idCredito, nomeCredito, false);
		EntidadeOperadora entidadeDebito = new EntidadeOperadora(idDebito, nomeDebito, false);

		return new Transacao(entidadeCredito, entidadeDebito, null, null, valorOperacao, dataHoraOperacao);
	}
}


