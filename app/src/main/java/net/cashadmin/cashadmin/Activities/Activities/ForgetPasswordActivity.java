package net.cashadmin.cashadmin.Activities.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.cashadmin.cashadmin.Activities.Config.Utils;
import net.cashadmin.cashadmin.Activities.Request.OkHttpStack;
import net.cashadmin.cashadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ForgetPasswordActivity extends AppCompatActivity {

    @InjectView(R.id.mail) EditText mMail;
    @InjectView(R.id.sendButton) Button mSendButton;
    @InjectView(R.id.relative1) RelativeLayout mRelative1;
    @InjectView(R.id.relative2) RelativeLayout mRelative2;
    @InjectView(R.id.validationButton) Button mValidationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.validationButton)
    public void onClickValidationButton(){
        Intent intent = new Intent(ForgetPasswordActivity.this, AccountConnexionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.sendButton)
    public void onClickSendButton(){
        final String mail = mMail.getText().toString().trim();
        if(mail.isEmpty()){
            Toast.makeText(this, R.string.emptyMail, Toast.LENGTH_LONG).show();
        }
        else{
            String URL = Utils.BASE_URL + "/mail/" + mail + ".json";

            StringRequest requestForgetPassword = new StringRequest(URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String s){
                    try {
                        JSONObject userJSON = new JSONObject(s);
                        if (userJSON.has("response")) {
                            if ((userJSON.getInt("response") == Utils.SUCCESS)) {
                                mRelative1.setVisibility(mRelative1.GONE);
                                mRelative2.setVisibility(mRelative2.VISIBLE);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                builder.setTitle("Erreur");
                                builder.setMessage("Cet email n'existe pas dans nos bases de données");
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès au serveur, veuillez patienter");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                            builder.setTitle("Erreur");
                            builder.setMessage("Pas d'accès Internet, veuillez l'activer");
                            builder.setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            Log.e("errorConnexion", volleyError.getMessage());
                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(ForgetPasswordActivity.this, new OkHttpStack());
            queue.add(requestForgetPassword);
        }
    }
}

