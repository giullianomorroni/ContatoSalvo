package com.contatosalvo.contatos;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
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

	public Contato(String idConta, String nome, List<Telefone> telefones,
			String email) {
		super();
		this.idConta = idConta;
		this.nome = nome;
		this.telefones = telefones;
		this.email = email;
	}

	public Contato(String idConta, String nome, Telefone telefone) {
		super();
		this.idConta = idConta;
		this.nome = nome;
		this.telefones.add(telefone);
	}

	public List<Contato> obterContatosTelefone(String idConta, ContentResolver cr) {
		List<Contato> contatos = new ArrayList<Contato>();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				Contato contato = new Contato();
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					contato.setNome(name);
					// get the phone number
					Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						contato.getTelefones().add(new Telefone(phone));
					}
					pCur.close();

					// get email and type
					Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID+ " = ?", new String[] { id }, null);
					while (emailCur.moveToNext()) {
						String email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						contato.setEmail(email);
					}
					emailCur.close();
					contato.setIdConta(idConta);
					contatos.add(contato);
				}
			}
		}
		return contatos;
	}

	public String gerarJson() {
		JSONObject json = new JSONObject();
		try {
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

	@Override
	public String toString() {
		return "Contato [id=" + id + ", idConta=" + idConta + ", nome=" + nome
				+ ", email=" + email + ", telefones=" + telefones + "]";
	}

}
