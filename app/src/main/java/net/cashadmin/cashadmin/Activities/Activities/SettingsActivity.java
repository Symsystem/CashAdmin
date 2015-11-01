package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import net.cashadmin.cashadmin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @InjectView(R.id.handleCategoriesButton)
    Button mHandleCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.handleCategoriesButton)
    public void onClickHandleCategories(){
        startActivity(new Intent(SettingsActivity.this, EditCategoryActivity.class));
    }
}
