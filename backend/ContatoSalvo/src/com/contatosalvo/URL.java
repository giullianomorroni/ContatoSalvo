package com.contatosalvo;

public enum URL {

	AUTENTICAR("http://giullianomorroni.com/contato_salvo/usuario/autenticar/"),

	REGISTRAR_CONTA ("http://giullianomorroni.com/contato_salvo/usuario/registrar/"),

	CONTATO("http://giullianomorroni.com/contato_salvo/usuario/<id>/contato/<nome>"),
	
	CONTATOS("http://giullianomorroni.com/contato_salvo/usuario/<id>/contatos"),
	
	CONTATOS_POR_LETRA("http://giullianomorroni.com/contato_salvo/usuario/<id>/contatos/letra/<letra>"), 

	REGISTRAR_CONTATO ("http://giullianomorroni.com/contato_salvo/contato/registrar/");

	private String valor;

	private URL(String valor) {
		this.valor = valor;
	}

	public String getValor(Boolean producao) {
		if (producao)
			return valor;
		else
			return valor.replace("giullianomorroni.com", "192.168.0.103:8090");
	}

}
