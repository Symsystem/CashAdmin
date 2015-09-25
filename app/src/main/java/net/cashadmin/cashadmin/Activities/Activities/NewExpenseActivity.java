package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
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
    @InjectView(R.id.mySwitch)
    Switch mSwitch;
    @InjectView(R.id.whicheRecurrenceLayout)
    LinearLayout mWhichRecurrenceLayout;

    private Category mCategory;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        mDataManager = new DataManager(this);

        ButterKnife.inject(this);

        Intent intent = getIntent();
        mCategory = (Category) intent.getSerializableExtra("category");
        mSubtitle.setText(mCategory.getLabel());
        mSubtitle.setBackgroundColor(mCategory.getColor());

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Animation animShow = AnimationUtils.loadAnimation(NewExpenseActivity.this, R.anim.popup_show);
                Animation animHide = AnimationUtils.loadAnimation(NewExpenseActivity.this, R.anim.popup_hide);
                if (b) {
                    mWhichRecurrenceLayout.startAnimation(animShow);
                    mWhichRecurrenceLayout.setVisibility(View.VISIBLE);
                }
                else{
                    mWhichRecurrenceLayout.startAnimation(animHide);
                    mWhichRecurrenceLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    @OnClick(R.id.addExpenseButton)
    public void onClickAddExpenseButton() {
        float amount = Float.valueOf(mAmount.getText().toString());
        String label = mLabel.getText().toString().trim();

        //TODO : Alert si pas de montant entré

        Expense expense = new Expense(0, amount, label, new Date(), mCategory, FrequencyEnum.DAYLY);
        mDataManager.insert(expense);
        startActivity(new Intent(NewExpenseActivity.this, MainActivity.class));
    }
}
