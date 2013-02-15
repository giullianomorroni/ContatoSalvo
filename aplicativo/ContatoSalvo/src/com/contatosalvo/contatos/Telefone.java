package com.contatosalvo.contatos;

public class Telefone {

	private String ddd;
	private String numero;
	private String operadora;
	
	public Telefone(String ddd, String numero, String operadora) {
		super();
		this.ddd = ddd;
		this.numero = numero;
		this.operadora = operadora;
	}

	public Telefone(String numero, String operadora) {
		super();
		this.numero = numero;
		if(this.numero != null) {
			this.numero = this.numero.replace("-", "").replace(" ", "");
			if(this.numero.length() > 10) {
				this.ddd = this.numero.substring(0, 2);
				this.numero = this.numero.substring(2);
			} else {
				this.ddd = "";
			}
		}
		this.operadora = operadora;
	}
	
	public Telefone(String numero) {
		this.numero = numero;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getOperadora() {
		return operadora;
	}

	public void setOperadora(String operadora) {
		this.operadora = operadora;
	}

	@Override
	public String toString() {
		return ddd + numero;
	}
	
}
