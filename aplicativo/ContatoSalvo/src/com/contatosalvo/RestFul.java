package com.contatosalvo;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

public class RestFul {

	public HttpClient createHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 5000;
		int timeoutSocket = 5000;

		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParameters, "utf-8");

		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpClient client = new DefaultHttpClient(httpParameters);
		return client;
	}
	
	public Boolean analisarResposta(String json, MainActivity activity) throws JSONException {
		if (json.contains("erro")) {
			JSONObject jsonObject = new JSONObject(json);
			Boolean sucesso = jsonObject.getBoolean("success");
			if (!sucesso) {
				String erro = jsonObject.getString("erro");
				System.err.println(erro);
				Toast.makeText(activity, erro, Toast.LENGTH_LONG).show();
				return false;
			}
			return true;
		}
		return false;
	}
	
}
