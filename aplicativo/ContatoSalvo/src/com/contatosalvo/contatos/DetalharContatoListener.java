package com.contatosalvo.contatos;

import com.contatosalvo.MainActivity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DetalharContatoListener implements OnItemClickListener {

	private ListView mainListView;
	private MainActivity mainActivity;
	
	public DetalharContatoListener(final ListView mainListView, MainActivity mainActivity) {
		this.mainListView = mainListView;
		this.mainActivity = mainActivity;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Object item = mainListView.getItemAtPosition(position);
		String telefone = item.toString();
		System.out.println("Telefone escolhido:" + telefone);
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefone));
		mainActivity.startActivity(intent);
	}

}
