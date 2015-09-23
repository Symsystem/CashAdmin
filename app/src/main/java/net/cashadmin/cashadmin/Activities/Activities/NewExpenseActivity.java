package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.R;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewExpenseActivity extends ActionBarActivity {

    @InjectView(R.id.subtitle)
    TextView mSubtitle;
    @InjectView(R.id.amount)
    EditText mAmount;
    @InjectView(R.id.label)
    EditText mLabel;
    @InjectView(R.id.addExpenseButton)
    Button mAddExpenseButton;

    private Category mCategory;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        mDataManager = new DataManager(this);

        ButterKnife.inject(this);

        Intent intent = getIntent();mCategory = (Category)intent.getSerializableExtra("category");
        mSubtitle.setText(mCategory.getLabel());
        mSubtitle.setBackgroundColor(mCategory.getColor());

    }

    @OnClick(R.id.addExpenseButton)
    public void onClickAddExpenseButton(){
        float amount = Float.valueOf(mAmount.getText().toString());

        //TODO : Alert si pas de montant entré

        Expense expense = new Expense(0,amount,new Date(),mCategory);
        mDataManager.insert(expense);
        startActivity(new Intent(NewExpenseActivity.this,MainActivity.class));
    }
}
