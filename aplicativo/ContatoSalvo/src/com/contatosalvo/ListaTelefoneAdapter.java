package com.contatosalvo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.contatosalvo.contatos.IniciarChamadaListener;
import com.contatosalvo.contatos.RemoverTelefoneListener;
import com.contatosalvo.contatos.Telefone;

public class ListaTelefoneAdapter extends ArrayAdapter<Telefone> {
	
	private List<Telefone> telefones = new ArrayList<Telefone>();
	private Context context;

	public ListaTelefoneAdapter(Context context, int resource, List<Telefone> telefones) {
		super(context, resource, telefones);
		this.context = context;
		this.telefones = telefones;
	}

	@Override
	public int getCount() {
		return telefones.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
             LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             convertView = vi.inflate(R.layout.contatos_linha, null);
         }

         TextView editText = (TextView) convertView.findViewById(R.id.rowEditTextTelefone);
         editText.setText(telefones.get(position).toString());

         TextView text = (TextView) convertView.findViewById(R.id.rowTextOperadora);
         text.setText(telefones.get(position).getOperadora());

         ImageButton buttonCall = (ImageButton) convertView.findViewById(R.id.buttonCallTelefone);
         buttonCall.setOnClickListener(new IniciarChamadaListener(position, telefones));

         ImageButton buttonRemove = (ImageButton) convertView.findViewById(R.id.buttonRemoveTelefone);
         buttonRemove.setOnClickListener(new RemoverTelefoneListener(position, telefones));

         return convertView;
	}

}
