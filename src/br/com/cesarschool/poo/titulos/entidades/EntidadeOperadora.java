package br.com.cesarschool.poo.titulos.entidades;
/*
 * Esta classe deve ter os seguintes atributos:
 * identificador, do tipo long
 * nome, do tipo String
 * autorizadoAcao, do tipo double
 * saldoAcao, do tipo double
 * saldoTituloDivida, do tipo double
 *
 * Deve ter um construtor público que inicializa os atributos identificador, nome
 * e autorizadoAcao. Deve ter métodos set/get públicos para os atributos identificador, nome
 * e autorizadoAcao. O atributo identificador é read-only fora da classe.
 *
 * Os atributos saldoAcao e saldoTituloDivida devem ter apenas métodos get públicos.
 *
 * Outros métodos públicos:
 *
 *  void creditarSaldoAcao(double valor): deve adicionar valor ao saldoAcao
 *  void debitarSaldoAcao(double valor): deve diminuir valor de saldoAcao
 *  void creditarSaldoTituloDivida(double valor): deve adicionar valor ao saldoTituloDivida
 *  void debitarSaldoTituloDivida(double valor): deve diminuir valor de saldoTituloDivida
 */public class EntidadeOperadora {

    private long identificador;
    private String nome;
    private boolean autorizadoAcao;
    private double saldoAcao;
    private double saldoTituloDivida;

    public EntidadeOperadora(long identificador, String nome, double saldoAcao) {
        this.identificador = identificador;
        this.nome = nome;
        this.autorizadoAcao = false;
        this.saldoAcao = saldoAcao;
        this.saldoTituloDivida = 0;
    }

    public long getIdentificador() {
        return identificador;
    }

    public String getNome() {
        return nome;
    }

    public boolean getAutorizadoAcao() {
        return autorizadoAcao;
    }

    public void setAutorizadoAcao(boolean autorizadoAcao) {
        this.autorizadoAcao = autorizadoAcao;
    }

    public double getSaldoAcao() {
        return saldoAcao;
    }

    public double getSaldoTituloDivida() {
        return saldoTituloDivida;
    }

    public void creditarSaldoAcao(double valor) {
        if (valor > 0) {
            this.saldoAcao += valor;
        }
    }

    public void debitarSaldoAcao(double valor) {
        if (valor > 0 && valor <= this.saldoAcao) {
            this.saldoAcao -= valor;
        }
    }

    public void creditarSaldoTituloDivida(double valor) {
        if (valor > 0) {
            this.saldoTituloDivida += valor;
        }
    }

    public void debitarSaldoTituloDivida(double valor) {
        if (valor > 0 && valor <= this.saldoTituloDivida) {
            this.saldoTituloDivida -= valor;
        }
    }
}
