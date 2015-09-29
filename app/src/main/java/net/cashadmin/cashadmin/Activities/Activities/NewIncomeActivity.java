package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Income;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewIncomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.amount)
    EditText mAmount;
    @InjectView(R.id.label)
    EditText mLabel;
    @InjectView(R.id.mySwitch)
    Switch mSwitch;
    @InjectView(R.id.whicheRecurrenceLayout)
    LinearLayout mWhichRecurrenceLayout;
    @InjectView(R.id.spinner)
    Spinner mSpinner;
    @InjectView(R.id.addIncomeButton)
    Button mAddIncomeButton;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income);
        mDataManager = DataManager.getDataManager();

        ButterKnife.inject(this);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Animation animShow = AnimationUtils.loadAnimation(NewIncomeActivity.this, R.anim.popup_show);
                Animation animHide = AnimationUtils.loadAnimation(NewIncomeActivity.this, R.anim.popup_hide);
                if (b) {
                    //float startY = mAddIncomeButton.getTop();
                    mWhichRecurrenceLayout.startAnimation(animShow);
                    mWhichRecurrenceLayout.setVisibility(View.VISIBLE);
                    //float endY = mAddIncomeButton.getTop();
                    //Animation animDown = new TranslateAnimation(0 , 0, 0, 100);
                    //animDown.setDuration(2000);
                    //animDown.setFillAfter(true);
                    //animDown.setInterpolator(new LinearInterpolator());
                    //mAddIncomeButton.startAnimation(animDown);
                } else {
                    mWhichRecurrenceLayout.startAnimation(animHide);
                    mWhichRecurrenceLayout.setVisibility(View.GONE);
                }
            }
        });

        mSpinner.setOnItemSelectedListener(this);

        List<String> listSpinner = new ArrayList<>();
        for(FrequencyEnum frequency : FrequencyEnum.values()){
            listSpinner.add(frequency.toString());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
    }

    @OnClick(R.id.addIncomeButton)
    public void onClickIncomeButton(){
        float amount = Float.valueOf(mAmount.getText().toString());
        String label = mLabel.getText().toString().trim();
        String frequency = FrequencyEnum.JAMAIS.toString();
        if(mSwitch.isChecked()){
            frequency = (String) mSpinner.getSelectedItem();
        }

        //TODO : Alert si pas de montant entré

        Income income = new Income(mDataManager.getNextId(TypeEnum.INCOME), amount, label, new Date(), FrequencyEnum.valueOf(frequency));
        mDataManager.insert(income);
        startActivity(new Intent(NewIncomeActivity.this, MainActivity.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }


}