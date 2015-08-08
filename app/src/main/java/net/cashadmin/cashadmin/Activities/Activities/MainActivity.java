package net.cashadmin.cashadmin.Activities.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.TextView;

import net.cashadmin.cashadmin.Activities.Database.CategoryHandler;
import net.cashadmin.cashadmin.Activities.Database.DBHandler;
import net.cashadmin.cashadmin.Activities.Database.GenericHandler;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.testEdit) EditText mTestEdit;
    @InjectView(R.id.loadTextView) TextView mLoadTextView;

    private DBHandler mDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mDBHandler = new DBHandler(this, null, null, 1);
    }

    @OnClick(R.id.button1)
    public void onClickButton1(){
        Category cat = new Category(1, mTestEdit.getText().toString(), "#FF0000FF");
        CategoryHandler catHandler = (CategoryHandler)mDBHandler.getHandler("category");

        if(catHandler.isIn(cat))
            mDBHandler.getHandler("category").update(cat);
        else
            mDBHandler.getHandler("category").insert(cat);
    }

    @OnClick(R.id.button2)
    public void onClickButton2(){
        GenericHandler catHandler = mDBHandler.getHandler("category");
        Category cat = (Category)catHandler.findById(1);

        mLoadTextView.setText(String.valueOf(cat.getColor()));
    }
}
