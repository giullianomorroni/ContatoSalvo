package com.contatosalvo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.contatosalvo.contatos.Contato;
import com.contatosalvo.contatos.ListaTelefoneListener;
import com.contatosalvo.contatos.ListarContatoListener;
import com.contatosalvo.contatos.Telefone;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	private Boolean PRODUCAO = true;

	private String id = null;
	private String email = null;
	private String senha = null;

	private static List<Telefone> novosTelefones = new ArrayList<Telefone>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);
		EditText emailInput = (EditText) findViewById(R.id.editText_email);
		EditText pinInput = (EditText) findViewById(R.id.editText_pin);
		emailInput.setText("giullianomorroni@gmail.com");
		pinInput.setText("123456");
		//manter o teclado fechado...
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_main, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_sincronizar:
	            sincronizarContatos(item.getActionView());
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	

	public void sincronizarContatos(View view) {
		Contato contato = new Contato();
		List<Contato> contatos = contato.obterContatosTelefone(this.id , this.getContentResolver());
		for (Contato c : contatos) {
			registrarContato(c);
		}
	}

	public void registrarContato(View view) {
		EditText inputNome = (EditText) findViewById(R.id.editText_contato_nome);
		//EditText inputDDD = (EditText) findViewById(R.id.editText_contato_ddd);
		EditText inputTelefone = (EditText) findViewById(R.id.editText_contato_telefone);
		Spinner spinerOperadora = (Spinner) findViewById(R.id.spinner_contato_operadoras);

		String nome = String.valueOf(inputNome.getText());
		String operadora = String.valueOf(spinerOperadora.getSelectedItem());
		String numero = String.valueOf(inputTelefone.getText());

		if (numero != null && numero.length() > 0) {
			Telefone telefone = new Telefone(numero, operadora);
			novosTelefones.add(telefone);
		}
		Contato usuario = new Contato(this.id, nome, novosTelefones, "");
		registrarContato(usuario);
	}

	private void registrarContato(Contato contato) {
		try {
			HttpClient client = new RestFul().createHttpClient();
			HttpPost post = new HttpPost(URL.REGISTRAR_CONTATO.getValor(PRODUCAO));
			HttpParams httpParameters = new BasicHttpParams();
			HttpProtocolParams.setContentCharset(httpParameters, "utf-8");
			post.setParams(httpParameters);

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("json", contato.gerarJson()));
			post.setEntity(new UrlEncodedFormEntity(pairs));
			client.execute(post);
			setContentView(R.layout.activity_filtro);
		} catch (ClientProtocolException e) {
			Toast.makeText(MainActivity.this, "Telefone inválido",Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(MainActivity.this, "Telefone inválido",Toast.LENGTH_LONG).show();
		}
	}

	public void adicionarTelefoneCache(View view) {
		//EditText inputDDD = (EditText) findViewById(R.id.editText_contato_ddd);
		EditText inputTelefone = (EditText) findViewById(R.id.editText_contato_telefone);
		Spinner spinerOperadora = (Spinner) findViewById(R.id.spinner_contato_operadoras);

		String operadora = String.valueOf(spinerOperadora.getSelectedItem());
		String numero = String.valueOf(inputTelefone.getText());

		if (numero != null && numero.length() > 0) {
			Telefone telefone = new Telefone(numero, operadora);
			novosTelefones.add(telefone);
			Toast.makeText(MainActivity.this, "Telefone salvo, se terminou clique em registrar", Toast.LENGTH_LONG).show();
			inputTelefone.setText("");
			spinerOperadora.setSelection(0);
		} else {
			Toast.makeText(MainActivity.this, "Telefone inválido",Toast.LENGTH_LONG).show();
		}
	}

	public void registrar(View view) {
		EditText emailInput = (EditText) findViewById(R.id.editText_cadastrar_email);
		EditText pinInput = (EditText) findViewById(R.id.editText_cadastrar_pin);
		CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_cadastrar_sincronizar);

		HttpClient client = new RestFul().createHttpClient();
		HttpPost post = new HttpPost(URL.REGISTRAR_CONTA.getValor(PRODUCAO));

		this.email = emailInput.getText().toString();
		this.senha = pinInput.getText().toString();

		try {
			if (checkBox.isChecked()) {
				//ContentResolver contentResolver = getContentResolver();
				//List<Contato> contatos = new Contato().obterContatosTelefone(this, contentResolver);
			}

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", this.email));
			pairs.add(new BasicNameValuePair("senha", this.senha));
			post.setEntity(new UrlEncodedFormEntity(pairs));

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();

			if (entity == null) {
				Toast.makeText(MainActivity.this, "Serviço Indisponível no momento!!!", Toast.LENGTH_LONG).show();
				return;
			}

			InputStream content = entity.getContent();
			InputStreamReader streamReader = new InputStreamReader(content);
			BufferedReader reader = new BufferedReader(streamReader);
			String json = "";
			String linha = null;
			while ((linha = reader.readLine()) != null)
				json += linha;

			System.out.println(json);
			Boolean possuiErro = new RestFul().analisarResposta(json, this);
			if (possuiErro)
				return;

			if (json.contains("id")) {
				JSONObject jsonObject = new JSONObject(json);
				this.id = jsonObject.getString("id");
				setContentView(R.layout.activity_filtro);
			} else {
				Toast.makeText(MainActivity.this, "Desculpe, mas não te conheço !!!",Toast.LENGTH_LONG).show();
			}

		} catch (UnsupportedEncodingException e) {
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (JSONException e) {
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	public void autenticar(View view) {
		HttpClient client = new RestFul().createHttpClient();
		HttpPost post = new HttpPost(URL.AUTENTICAR.getValor(PRODUCAO));

		if (!NetWork.isNetworkAvailable(this))
			Toast.makeText(MainActivity.this, "Sua net é gato", Toast.LENGTH_LONG).show();

		EditText emailInput = (EditText) findViewById(R.id.editText_email);
		EditText pinInput = (EditText) findViewById(R.id.editText_pin);

		try {
			this.email = emailInput.getText().toString();
			this.senha = pinInput.getText().toString();

			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("email", this.email));
			pairs.add(new BasicNameValuePair("senha", this.senha));
			post.setEntity(new UrlEncodedFormEntity(pairs));

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream content = entity.getContent();
				InputStreamReader streamReader = new InputStreamReader(content);
				BufferedReader reader = new BufferedReader(streamReader);
				String json = "";
				String linha = null;
				while ((linha = reader.readLine()) != null)
					json += linha;

				Boolean possuiErro = new RestFul().analisarResposta(json, this);
				if (possuiErro)
					return;

				if (json.contains("id")) {
					JSONObject jsonObject = new JSONObject(json);
					this.id = jsonObject.getString("id");
					setContentView(R.layout.activity_filtro);
				} else {
					Toast.makeText(MainActivity.this, "Desculpe, mas não te conheço !!!",Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(MainActivity.this,"Serviço Indisponível no momento!!!", Toast.LENGTH_LONG).show();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void preencherContatosPorLetra(View view) {
		Button clickedButton = (Button) view;
		List<String> nomes = prenhcerListaPorLetra(clickedButton.getText().toString());

		setContentView(R.layout.activity_contatos);
		final ListView mainListView = (ListView) findViewById(R.id.listView_contatos);
		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);

		ListarContatoListener listener = new ListarContatoListener(mainListView, this);
		mainListView.setOnItemClickListener(listener);
		mainListView.setAdapter(listAdapter);
	}

	private List<String> prenhcerListaPorLetra(String letra) {
		List<String> nomes = new ArrayList<String>();
		HttpClient client = new RestFul().createHttpClient();
		String url = URL.CONTATOS_POR_LETRA.getValor(PRODUCAO);
		url = url.replace("<id>", this.id);
		url = url.replace("<letra>", letra);
		HttpGet get = new HttpGet(url);

		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream content = entity.getContent();
				InputStreamReader streamReader = new InputStreamReader(content);
				BufferedReader reader = new BufferedReader(streamReader);
				String json = "";
				String linha = null;
				while ((linha = reader.readLine()) != null)
					json += linha;

				JSONObject jsonObject = new JSONObject(json);
				JSONArray contatos = jsonObject.getJSONArray("contatos");
				for (int i = 0; i < contatos.length(); i++) {
					JSONObject c = contatos.getJSONObject(i);
					nomes.add(c.getString("nome"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(MainActivity.this, "Ocorreu um erro...",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Toast.makeText(MainActivity.this, "Ocorreu um erro...",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(MainActivity.this, "Ocorreu um erro...",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (JSONException e) {
			Toast.makeText(MainActivity.this, "Ocorreu um erro...",Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		Collections.sort(nomes);
		return nomes;
	}

	public void detalheContato(String nome) {
		HttpClient client = new RestFul().createHttpClient();
		String url = URL.CONTATO.getValor(PRODUCAO);
		url = url.replace("<id>", this.id);
		url = url.replace("<nome>", nome.replace(" ", "%20"));
		HttpGet get = new HttpGet(url);

		try {
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream content = entity.getContent();
				InputStreamReader streamReader = new InputStreamReader(content);
				BufferedReader reader = new BufferedReader(streamReader);
				String json = "";
				String linha = null;
				while ((linha = reader.readLine()) != null)
					json += linha;

				JSONObject jsonObject = new JSONObject(json);
				List<Telefone> telefones = new ArrayList<Telefone>();
				//List<String> emails = new ArrayList<String>();

				JSONArray resutaldo = jsonObject.getJSONArray("telefones");
				for (int i = 0; i < resutaldo.length(); i++) {
					JSONObject t = resutaldo.getJSONObject(i);
					String numero = t.optString("numero");
					String operadora = t.optString("operadora");
					telefones.add(new Telefone(numero, operadora));
				}

				setContentView(R.layout.activity_contato);

				final TextView textViewNome = (TextView) findViewById(R.id.textView_nome);
				textViewNome.setText(nome);

				ListaTelefoneAdapter adapter = new ListaTelefoneAdapter(this, R.id.listView_telefones, telefones);

				final ListView mainListViewTelefone = (ListView) findViewById(R.id.listView_telefones);
				ListaTelefoneListener detalharContatoListener = new ListaTelefoneListener(mainListViewTelefone, this);
				mainListViewTelefone.setOnItemClickListener(detalharContatoListener);
				mainListViewTelefone.setAdapter(adapter);

				//final ListView mainListViewEmails = (ListView) findViewById(R.id.listView_emails);
				//ArrayAdapter<String> listAdapterEmails = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emails);
				//mainListViewEmails.setAdapter(listAdapterEmails);
			}
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(MainActivity.this, "Telefone inválido",Toast.LENGTH_LONG).show();
		} catch (ClientProtocolException e) {
			Toast.makeText(MainActivity.this, "Telefone inválido",Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(MainActivity.this, "Telefone inválido",Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Toast.makeText(MainActivity.this, "Telefone inválido",Toast.LENGTH_LONG).show();
		}
	}

	public void paginaInicial(View view) {
		setContentView(R.layout.activity_main);
		EditText emailInput = (EditText) findViewById(R.id.editText_email);
		EditText pinInput = (EditText) findViewById(R.id.editText_pin);

		emailInput.setText(this.email);
		pinInput.setText(this.senha);
	}
	
	public void paginaFiltro(View view) {
		setContentView(R.layout.activity_filtro);
	}

	public void paginaContatos(View view) {
		setContentView(R.layout.activity_contatos);
	}

	public void paginaRegistraConta(View view) {
		setContentView(R.layout.activity_usuario);
	}

	public void paginaNovoContato(View view) {
		setContentView(R.layout.activity_novo_contato);
	}

	public void fechar(View view) {
		finish();
	}
}
