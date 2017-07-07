package org.teste.memcached.entidades;

import java.sql.Blob;
import java.sql.SQLException;

public class ContatoTeste {

	private String contato;
	private Tipo tipo;
	private Blob blob;
	
	public ContatoTeste(){
		
	}
	
	public ContatoTeste(String contato, Tipo tipo) {
		this.contato = contato;
		this.tipo = tipo;
		this.blob = new BlobTeste();
		try {
			this.blob.setBytes(0, contato.getBytes());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}
	
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public static enum Tipo{
		EMAIL, TELEFONE;
	}

	public Blob getBlob() {
		return blob;
	}

	public void setBlob(Blob blob) {
		this.blob = blob;
	}

	@Override
	public String toString() {
		return "ContatoTeste [contato=" + contato + ", tipo=" + tipo + ", blob=" + blob + "]";
	}
	
}