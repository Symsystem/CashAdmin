package net.cashadmin.cashadmin.Activities.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import net.cashadmin.cashadmin.Activities.Storage.ManageDB;
import net.cashadmin.cashadmin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.testEdit) EditText mTestEdit;

    private ManageDB mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

    }

    @OnClick(R.id.button1)
    public void onClickButton1(){

        mDB.createData(mTestEdit.getText().toString().trim());

    }

    @OnClick(R.id.button2)
    public void onClickButton2(){

    }
}
