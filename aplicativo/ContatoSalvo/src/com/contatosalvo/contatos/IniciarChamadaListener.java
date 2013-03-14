package com.contatosalvo.contatos;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

public class IniciarChamadaListener implements OnClickListener {

	private int position;
	private List<Telefone> telefones;
	
	public IniciarChamadaListener() {
		super();
	}

	public IniciarChamadaListener(int position, List<Telefone> telefones) {
		this.position = position;
		this.telefones = telefones;
	}

	@Override
	public void onClick(View v) {
		Telefone telefone = this.telefones.get(position);
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefone));
		v.getContext().startActivity(intent);
	}

}
