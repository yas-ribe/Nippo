package br.fecap.nippoprojeto.service;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConexaoServidor {

    public interface Callback {
        void onSuccess();
        void onError(String message);
    }

    public static void realizarLogin(Context context, String email, String senha, Callback callback) {
        String url = "https://ljtt7h-3000.csb.app/login";

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("Login bem-sucedido")) {
                                callback.onSuccess();
                            } else {
                                callback.onError(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Erro ao processar a resposta do servidor");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensagemErro;
                if (error instanceof AuthFailureError) {
                    mensagemErro = "Conta não encontrada ou senha incorreta";
                } else {
                    mensagemErro = "Erro ao fazer login. Por favor, tente novamente mais tarde. " + error.toString();
                }
                callback.onError(mensagemErro);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("senha", senha);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
