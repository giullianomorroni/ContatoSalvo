package com.contatosalvo.contatos;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;

public class RemoverTelefoneListener implements OnClickListener {

	private int position;
	private List<Telefone> telefones;
	
	public RemoverTelefoneListener() {
		super();
	}
	
	public RemoverTelefoneListener(int position, List<Telefone> telefones) {
		this.position = position;
		this.telefones = telefones;
	}

	@Override
	public void onClick(View v) {
		//Telefone telefone = this.telefones.get(position);
		//TODO remover telefone...
	}

}
