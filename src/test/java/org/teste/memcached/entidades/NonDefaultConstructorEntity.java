package org.teste.memcached.entidades;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NonDefaultConstructorEntity {
	private String name;

	public NonDefaultConstructorEntity(@JsonProperty(value="name") String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "NonDefaultConstructorEntity [name=" + name + "]";
	}
}
