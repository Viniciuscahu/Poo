package br.com.cesarschool.poo.titulos.repositorios;
import br.com.cesarschool.poo.titulos.entidades.Acao;
import br.com.cesarschool.poo.titulos.entidades.EntidadeOperadora;
import br.com.cesarschool.poo.titulos.entidades.TituloDivida;
import br.com.cesarschool.poo.titulos.entidades.Transacao;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTransacao {

	private static final String NOME_ARQUIVO = "src/br/com/cesarschool/poo/titulos/repositorios/Transacao.txt";
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
				transacao.getEntidadeCredito().getAutorizadoAcao() + ";" +
				transacao.getEntidadeCredito().getSaldoAcao() + ";" +
				transacao.getEntidadeCredito().getSaldoTituloDivida() + ";" +
				transacao.getEntidadeDebito().getIdentificador() + ";" +
				transacao.getEntidadeDebito().getNome() + ";" +
				transacao.getEntidadeDebito().getAutorizadoAcao() + ";" +
				transacao.getEntidadeDebito().getSaldoAcao() + ";" +
				transacao.getEntidadeDebito().getSaldoTituloDivida() + ";" +
				(transacao.getAcao() != null ? transacao.getAcao().getIdentificador() : "null") + ";" +
				(transacao.getAcao() != null ? transacao.getAcao().getNome() : transacao.getTituloDivida().getNome()) + ";" +
				(transacao.getAcao() != null ? transacao.getAcao().getDataDeValidade() : transacao.getTituloDivida().getDataDeValidade()) + ";" +
				(transacao.getAcao() != null ? transacao.getAcao().getValorUnitario() : transacao.getTituloDivida().getTaxaJuros()) + ";" +
				(transacao.getAcao() != null ? "null" : transacao.getTituloDivida().getTaxaJuros()) + ";" +
				transacao.getValorOperacao() + ";" +
				transacao.getDataHoraOperacao().format(DATETIME_FORMATTER);
	}


	private Transacao parseTransacao(String linha) {
		String[] partes = linha.split(";");

		// Entidade Crédito
		long idCredito = Long.parseLong(partes[0]);
		String nomeCredito = partes[1];
		boolean autorizadoAcaoCredito = Boolean.parseBoolean(partes[2]);
		double saldoAcaoCredito = Double.parseDouble(partes[3]);
		double saldoTituloCredito = Double.parseDouble(partes[4]);

		long idDebito = Long.parseLong(partes[5]);
		String nomeDebito = partes[6];
		boolean autorizadoAcaoDebito = Boolean.parseBoolean(partes[7]);
		double saldoAcaoDebito = Double.parseDouble(partes[8]);
		double saldoTituloDebito = Double.parseDouble(partes[9]);

		Integer idAcaoOuTitulo = partes[10].equals("null") ? null : Integer.parseInt(partes[10]);
		String nomeAtivo = partes[11];
		LocalDate dataValidade = LocalDate.parse(partes[12]);
		double valorUnitario = Double.parseDouble(partes[13]);
		Double valorOperacao = partes[14].equals("null") ? null : Double.parseDouble(partes[14]);
		double valorAtivo = Double.parseDouble(partes[15]);

		LocalDateTime dataHoraOperacao = LocalDateTime.parse(partes[16], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		EntidadeOperadora entidadeCredito = new EntidadeOperadora(idCredito, nomeCredito, autorizadoAcaoCredito, LocalDate.now(), valorUnitario);
		entidadeCredito.creditarSaldoAcao(saldoAcaoCredito);
		entidadeCredito.creditarSaldoTituloDivida(saldoTituloCredito);

		EntidadeOperadora entidadeDebito = new EntidadeOperadora(idDebito, nomeDebito, autorizadoAcaoDebito, LocalDate.now(), valorUnitario);
		entidadeDebito.creditarSaldoAcao(saldoAcaoDebito);
		entidadeDebito.creditarSaldoTituloDivida(saldoTituloDebito);
		if (idAcaoOuTitulo != null) {
			Acao acao = new Acao(idAcaoOuTitulo, nomeAtivo, dataValidade, valorUnitario);
			return new Transacao(entidadeCredito, entidadeDebito, acao, null, valorOperacao, dataHoraOperacao);
		} else {
			TituloDivida tituloDivida = new TituloDivida(idAcaoOuTitulo, nomeAtivo, dataValidade, valorAtivo);
			return new Transacao(entidadeCredito, entidadeDebito, null, tituloDivida, valorOperacao, dataHoraOperacao);
		}
	}



}


