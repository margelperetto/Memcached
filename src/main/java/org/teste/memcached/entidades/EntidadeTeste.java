package org.teste.memcached.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EntidadeTeste implements Serializable{
	private static final long serialVersionUID = -7296536278559007191L;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

	private String nome;
	private String bigText;
	private BigDecimal peso;
	private Date dataNascimento;
	private List<ContatoTeste> contatos;

	public EntidadeTeste() {

	}

	public EntidadeTeste(String nome, BigDecimal peso, Date dataNascimento) {
		this.nome = nome;
		this.peso = peso;
		this.dataNascimento = dataNascimento;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public BigDecimal getPeso() {
		return peso;
	}
	public void setPeso(BigDecimal peso) {
		this.peso = peso;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public List<ContatoTeste> getContatos() {
		return contatos;
	}

	public void setContatos(List<ContatoTeste> contatos) {
		this.contatos = contatos;
	}

	@Override
	public String toString() {
		return "EntidadeTeste [nome=" + nome + ", peso=" + peso + ", dataNascimento=" + 
				(dataNascimento==null?null:SDF.format(dataNascimento)) + 
				", contatos="+ contatos + "]";
	}

	public String getBigText() {
		return bigText;
	}

	public void setBigText(String bigText) {
		this.bigText = bigText;
	}
	
}