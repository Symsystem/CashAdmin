package net.cashadmin.cashadmin.Activities.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import net.cashadmin.cashadmin.Activities.Classes.Utils;
import net.cashadmin.cashadmin.Activities.Request.OkHttpStack;
import net.cashadmin.cashadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountConnexionActivity extends ActionBarActivity {

    private boolean checked;

    @InjectView(R.id.loginId) EditText mLoginId;
    @InjectView(R.id.password) EditText mPassword;
    @InjectView(R.id.rememberCheck) CheckBox mRememberCheck;
    @InjectView(R.id.connexionButton) Button mConnexionButton;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.rememberPassword) TextView mRememberPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_connexion);

        ButterKnife.inject(this);


        if(mProgressBar.getVisibility() == ProgressBar.VISIBLE){
            mProgressBar.setVisibility(ProgressBar.GONE);
        }
    }

    @OnClick(R.id.connexionButton)
    public void onClickConnexionButton(){
        final String login = mLoginId.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        if(login.isEmpty() || password.isEmpty()){
            Toast.makeText(this, R.string.emptyField, Toast.LENGTH_LONG).show();
        }
        else{
            String URL = Utils.BASE_URL + "api/logins/" + login + "/passwords/" + password + ".json";

            StringRequest requestConnexion = new StringRequest(URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if ((userJSON.getInt("response") == Utils.SUCCESS)) {
                                Intent intent = new Intent(AccountConnexionActivity.this, MainActivity.class);
                                mProgressBar.setVisibility(ProgressBar.GONE);
                                startActivity(intent); //A CHANGER \\
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AccountConnexionActivity.this);
                                builder.setTitle("Erreur");
                                builder.setMessage("Mauvais mot de passe ou nom d'utilisateur");
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                mProgressBar.setVisibility(ProgressBar.GONE);
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AccountConnexionActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès au serveur, veuillez patienter");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AccountConnexionActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès Internet, veuillez l'activer");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mProgressBar.setVisibility(ProgressBar.GONE);
                            Log.e("errorConnexion", volleyError.getMessage());
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(AccountConnexionActivity.this, new OkHttpStack());
            queue.add(requestConnexion);
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
    }

    public void checkBoxClicked(View view){
        checked = ((CheckBox) view).isChecked();
    }

    @OnClick(R.id.rememberPassword)
    public void onClickRememberPassword(){
        Intent intent = new Intent(AccountConnexionActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);
    }
}
