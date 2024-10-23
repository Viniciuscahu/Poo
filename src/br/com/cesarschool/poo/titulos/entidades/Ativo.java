package br.com.cesarschool.poo.titulos.entidades;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Ativo {
	private final int identificador;
	private String nome;
	private LocalDate dataDeValidade;

	public Ativo(int identificador, String nome, LocalDate dataDeValidade) {
		this.identificador = identificador;
		this.nome = nome;
		this.dataDeValidade = dataDeValidade;
	}

	public int getIdentificador() {
		return identificador;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataDeValidade() {
		return dataDeValidade;
	}

	public void setDataDeValidade(LocalDate dataDeValidade) {
		this.dataDeValidade = dataDeValidade;
	}
}

