package br.com.cesarschool.poo.titulos.repositorios;

import br.com.cesarschool.poo.titulos.entidades.Transacao;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/*
 * Deve gravar em e ler de um arquivo texto chamado Transacao.txt os dados dos objetos do tipo
 * Transacao. Seguem abaixo exemplos de linhas 
 * De entidadeCredito: identificador, nome, autorizadoAcao, saldoAcao, saldoTituloDivida.
 * De entidadeDebito: identificador, nome, autorizadoAcao, saldoAcao, saldoTituloDivida.
 * De acao: identificador, nome, dataValidade, valorUnitario OU null
 * De tituloDivida: identificador, nome, dataValidade, taxaJuros OU null. 
 * valorOperacao, dataHoraOperacao
 * 
 *   002192;BCB;true;0.00;1890220034.0;001112;BOFA;true;12900000210.00;3564234127.0;1;PETROBRAS;2024-12-12;30.33;null;100000.0;2024-01-01 12:22:21 
 *   002192;BCB;false;0.00;1890220034.0;001112;BOFA;true;12900000210.00;3564234127.0;null;3;FRANCA;2027-11-11;2.5;100000.0;2024-01-01 12:22:21
 *
 * A inclus�o deve adicionar uma nova linha ao arquivo. 
 * 
 * A busca deve retornar um array de transa��es cuja entidadeCredito tenha identificador igual ao
 * recebido como par�metro.  
 */
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

        /
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
			EntidadeOperadora entidadeCredito = new EntidadeOperadora(idCredito, nomeCredito, 0);
			EntidadeOperadora entidadeDebito = new EntidadeOperadora(idDebito, nomeDebito, 0);

			return new Transacao(entidadeCredito, entidadeDebito, null, null, valorOperacao, dataHoraOperacao);
		}
	}


}
