package com.example.alanflores.asynctaskapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

    TextView textREsultado;
    WebView webView;
    Button buttonIniciar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textREsultado = (TextView)findViewById(R.id.text_numero_lineas);

        buttonIniciar = (Button)findViewById(R.id.button_iniciar);
        webView = (WebView)findViewById(R.id.webView);
        buttonIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConexionAsincrona conexionAsincrona = new ConexionAsincrona();
                conexionAsincrona.execute("http://google.com");
            }
        });
    }

    class ConexionAsincrona extends AsyncTask<String, Integer, String>{

        //Antes de iniciar la tarea acceso a la interfas de usuario
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(),"Iniciando descarga",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            webView.loadData(s, "text/html","UTF-8");
            Toast.makeText(getApplicationContext(),"Descarga finalizada",Toast.LENGTH_SHORT).show();
        }

        //Comunicarme desde doingBack ... con la interfaz acceso a la ui hilo principal
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            textREsultado.setText("Lineas leidas : " + values[0]);
        }

        //
        @Override
        protected String doInBackground(String... strings) {
            Integer contadorLineas = 0;
            String resultado = "";
            try{
                URL url = null;
                url = new URL(strings[0]);
                URLConnection connection = url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line="";
                while((line = bufferedReader.readLine()) != null){
                    resultado += line;
                    contadorLineas ++;
                    publishProgress(contadorLineas);
                    Thread.sleep(200);
                }

                bufferedReader.close();

                return resultado;
            }catch (Exception e){
                e.printStackTrace();
            }
            return "";
        }
    }
}
