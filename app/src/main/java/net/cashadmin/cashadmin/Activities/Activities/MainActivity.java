package net.cashadmin.cashadmin.Activities.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

import net.cashadmin.cashadmin.Activities.Database.DBHandler;
import net.cashadmin.cashadmin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.testEdit) EditText mTestEdit;

    private DBHandler mDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

    }

    @OnClick(R.id.button1)
    public void onClickButton1(){

    }

    @OnClick(R.id.button2)
    public void onClickButton2(){

    }
}
