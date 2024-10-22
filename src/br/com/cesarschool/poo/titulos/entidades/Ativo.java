package br.com.cesarschool.poo.titulos.entidades;
import java.time.LocalDateTime;
/*
 * Esta classe deve ter os seguintes atributos:
 * identificador, do tipo int
 * nome, do tipo String
 * data de validade, do tipo LocalDate
 * 
 * Deve ter um construtor p�blico que inicializa os atributos, 
 * e m�todos set/get p�blicos para os atributos. O atributo identificador
 * � read-only fora da classe.
 */
public class Ativo {
	private int identificador;
	private String nome;
	private LocalDateTime dataDeValidade;
	
	public Ativo(int identificador, String nome, LocalDateTime dataDeValidade) {
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

	public LocalDateTime getdataDeValidade() {
		return dataDeValidade;
	}

	public void setDataDeValidade(LocalDateTime dataDeValidade) {
		this.dataDeValidade = dataDeValidade;
	}
}
