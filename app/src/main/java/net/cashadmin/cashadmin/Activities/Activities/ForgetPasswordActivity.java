package net.cashadmin.cashadmin.Activities.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.cashadmin.cashadmin.Activities.Classes.Utils;
import net.cashadmin.cashadmin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ForgetPasswordActivity extends ActionBarActivity {

    @InjectView(R.id.mail) EditText mMail;
    @InjectView(R.id.sendButton) Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.sendButton)
    public void onClickSendButton(){
        final String mail = mMail.getText().toString().trim();
        if(mail.isEmpty()){
            Toast.makeText(this, R.string.emptyMail, Toast.LENGTH_LONG).show();
        }
        else{
            String URL = Utils.BASE_URL + "/mail/" + mail + ".json";
        }
    }
}
