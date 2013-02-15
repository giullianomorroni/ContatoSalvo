package com.contatosalvo.contatos;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.contatosalvo.MainActivity;

public class ListarContatoListener implements OnItemClickListener {

	private ListView mainListView;
	private MainActivity mainActivity;

	public ListarContatoListener(final ListView mainListView, MainActivity mainActivity) {
		this.mainListView = mainListView;
		this.mainActivity = mainActivity;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Object item = mainListView.getItemAtPosition(position);
		String nome = item.toString();
		System.out.println("Contato escolhido:" + nome);
		mainActivity.detalheContato(nome);
	}

}
