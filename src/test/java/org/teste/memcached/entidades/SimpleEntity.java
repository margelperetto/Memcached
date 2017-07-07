package org.teste.memcached.entidades;

public class SimpleEntity {
	private String nome;
	private int idade;

	public int getIdade() {
		return idade;
	}
	public void setIdade(int idade) {
		this.idade = idade;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public String toString() {
		return "EntidadeSimples [nome=" + nome + ", idade=" + idade + "]";
	}
}
