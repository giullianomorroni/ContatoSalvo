package com.contatosalvo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.contatosalvo.contatos.Telefone;

public class ListaContatoAdapter extends BaseAdapter {

	private List<Telefone> telefones = new ArrayList<Telefone>();
	private Context context;

	public ListaContatoAdapter(Context context, List<Telefone> telefones) {
		this.context = context;
		 //this.inflater = LayoutInflater.from(context);
		 this.telefones = telefones;
	}

	@Override
	public int getCount() {
		return telefones.size();
	}

	@Override
	public Object getItem(int position) {
		return telefones.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
             LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView = vi.inflate(R.layout.contatos_linha, null);
         }

         EditText editText = (EditText) convertView.findViewById(R.id.rowEditTextTelefone);
         editText.setText(telefones.get(position).toString());

         TextView text = (TextView) convertView.findViewById(R.id.rowTextOperadora);
         text.setText(telefones.get(position).getOperadora());

         return convertView;
	}

}
