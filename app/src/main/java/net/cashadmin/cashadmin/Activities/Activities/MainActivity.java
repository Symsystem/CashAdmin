package net.cashadmin.cashadmin.Activities.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.R;

import java.util.Date;

import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mDataManager = new DataManager(this);

        Date currentDate = new Date();

    }

}
