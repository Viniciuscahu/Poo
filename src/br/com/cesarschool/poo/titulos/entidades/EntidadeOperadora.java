package br.com.cesarschool.poo.titulos.entidades;

public class EntidadeOperadora {

    private long identificador;
    private String nome;
    private double autorizadoAcao;
    private double saldoAcao;
    private double saldoTituloDivida;

    public EntidadeOperadora(long identificador, String nome, double autorizadoAcao) {
        this.identificador = identificador;
        this.nome = nome;
        this.autorizadoAcao = autorizadoAcao;
        this.saldoAcao = 0;
        this.saldoTituloDivida = 0;
    }

    public long getIdentificador() {
        return identificador;
    }

    private void setIdentificador(long identificador) {
        this.identificador = identificador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getAutorizadoAcao() {
        return autorizadoAcao;
    }

    public void setAutorizadoAcao(double autorizadoAcao) {
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
