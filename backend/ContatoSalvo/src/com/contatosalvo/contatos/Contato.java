package com.contatosalvo.contatos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;


public class Contato {

	private String id;
	private String idConta;
	private String nome;
	private String email;
	private List<Telefone> telefones = new ArrayList<Telefone>();

	public Contato() {
		super();
	}
	
	public Contato(String idConta, String nome, List<Telefone> telefones, String email) {
		super();
		this.idConta = idConta;
		this.nome = nome;
		this.telefones = telefones;
		this.email = email;
	}

	public List<Contato> obterContatosTelefone(Context ctx, ContentResolver contentResolver){
		List<Contato> contatos = new ArrayList<Contato>();
		Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);

		while (cursor.moveToNext()) { 
		   String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
		   String nome = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		   String possuiTelefones = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

		   Contato c = new Contato(); 
		   c.setNome(nome);

		   if (Boolean.parseBoolean(possuiTelefones)) { 
		      // You know it has a number so now query it like this
		      Cursor phones = contentResolver.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id, null, null); 
		      while (phones.moveToNext()) { 
		         String numero = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
		         c.getTelefones().add(new Telefone(numero));
		      } 
		      phones.close(); 
		   }

		   Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null); 
		   while (emails.moveToNext()) { 
		      String email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
		      c.setEmail(email);
		   } 
		   emails.close();
		   contatos.add(c);
		}
		cursor.close();
		return contatos;
	}
	
	public String gerarJson() {
		JSONObject json = new JSONObject();
		try{
			json.put("id_usuario", idConta);
			json.put("nome", nome);
			json.put("email", email);
			JSONArray jsonArrayTelefones = new JSONArray();
			for (Telefone t : telefones) {
				JSONObject jsonTelefone = new JSONObject();
				jsonTelefone.put("numero", t.getNumero());
				jsonTelefone.put("operadora", t.getOperadora());
				jsonArrayTelefones.put(jsonTelefone);
			}
			json.put("telefones", jsonArrayTelefones);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdConta() {
		return idConta;
	}

	public void setIdConta(String idConta) {
		this.idConta = idConta;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
