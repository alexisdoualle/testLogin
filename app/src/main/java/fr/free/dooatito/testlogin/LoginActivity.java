package fr.free.dooatito.testlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Variables
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);

    }

    // Quand on click sur le boutton Valider:
    public void checkLogin(View arg0) {

        // obtient les valeurs
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        // initialise AsyncLogin() avec email et password:
        new AsyncLogin().execute(email,password);

    }

    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //ecran de chargement:
            pdLoading.setMessage("\tChargement...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // adresse en fonction de l'ip de l'ordinateur:
                url = new URL("http://10.0.2.129:8888/testLogin/login.inc.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Classe HttpURLConnection pour recevoir et envoyer les requetes sql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput et setDoOutput pour gerer ce qu'on reçoit et ce qu'on envoie
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // ajoute les paramètre à l'url:
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1]);
                String query = builder.build().getEncodedQuery();

                // Ouvre la connexion:
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // vérifie si la connexion a réussi
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // lie les données du serveur:
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                        System.out.println(line);
                    }

                    // envoie les données à la méthode onPostExecute
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //methode fonctionnant sur l'UI

            pdLoading.dismiss();

            if(result.equalsIgnoreCase("true"))
            {

                /* 0n lance une autre activité en cas d'identification réussie. Voir
                sharedPreferences sur Android, et bouton logout
                 */

                Intent intent = new Intent(LoginActivity.this,SecondActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();

            }else if (result.equalsIgnoreCase("false")){

                // Message d'erreur en cas de mauvais identifiants:
                Toast.makeText(LoginActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_LONG).show();

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {
                System.out.println("on est dans: if result.equalsIgnoreCase(exception)");
                Toast.makeText(LoginActivity.this, "Problème de connexion.", Toast.LENGTH_LONG).show();

            }
        }

    }
}