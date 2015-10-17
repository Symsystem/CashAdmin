package net.cashadmin.cashadmin.Activities.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Frequency;
import net.cashadmin.cashadmin.Activities.Model.Income;
import net.cashadmin.cashadmin.Activities.UI.Popup;
import net.cashadmin.cashadmin.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @InjectView(R.id.amountLayout)
    LinearLayout mAmountLayout;
    @InjectView(R.id.recurrenceLayout)
    LinearLayout mRecurrenceLayout;
    @InjectView(R.id.whicheRecurrenceLayout)
    LinearLayout mWhichRecurrenceLayout;
    @InjectView(R.id.dateLayout)
    LinearLayout mDateLayout;
    @InjectView(R.id.spinner)
    Spinner mSpinner;
    @InjectView(R.id.addIncomeButton)
    Button mAddIncomeButton;
    @InjectView(R.id.dateChoice)
    TextView mDateChoice;

    private DataManager mDataManager;
    private DateFormat date = new SimpleDateFormat("dd/MM/yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income);
        mDataManager = DataManager.getDataManager();

        ButterKnife.inject(this);

        String currentDate = date.format(new Date());
        mDateChoice.setText(currentDate);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Animation animShow = AnimationUtils.loadAnimation(NewIncomeActivity.this, R.anim.popup_show_down);
                Animation animHide = AnimationUtils.loadAnimation(NewIncomeActivity.this, R.anim.popup_hide_up);
                float startY = mAmountLayout.getBottom();
                float endY = mRecurrenceLayout.getBottom();
                if (b) {
                    mWhichRecurrenceLayout.startAnimation(animShow);
                    mWhichRecurrenceLayout.setVisibility(View.VISIBLE);
                    mDateLayout.startAnimation(animShow);
                    mDateLayout.setVisibility(View.VISIBLE);
                    Animation animDown = new TranslateAnimation(0 , 0, startY - endY, 0);
                    animDown.setDuration(200);
                    animDown.setFillAfter(true);
                    animDown.setInterpolator(new LinearInterpolator());
                    mAddIncomeButton.startAnimation(animDown);
                } else {
                    mWhichRecurrenceLayout.startAnimation(animHide);
                    mWhichRecurrenceLayout.setVisibility(View.GONE);
                    mDateLayout.startAnimation(animHide);
                    mDateLayout.setVisibility(View.GONE);
                    Animation animUp = new TranslateAnimation(0, 0, endY - startY, 0);
                    animUp.setDuration(200);
                    animUp.setFillAfter(true);
                    animUp.setInterpolator(new LinearInterpolator());
                    mAddIncomeButton.startAnimation(animUp);
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
        String stringAmount = mAmount.getText().toString().trim();
        String label = mLabel.getText().toString().trim();
        String frequency = FrequencyEnum.values()[0].toString();
        Date dateFrequency = null;
        if(!(stringAmount.toString().matches("-?\\d+(\\.\\d+)?"))) {
            Toast toast = Popup.toast(NewIncomeActivity.this, getString(R.string.validAmount));
            toast.show();
        }else {
            if (label.isEmpty() || stringAmount.isEmpty()) {
                Toast toast = Popup.toast(NewIncomeActivity.this, getString(R.string.fieldEmpty));
                toast.show();
            } else {
                float amount = Float.valueOf(stringAmount);
                if (mSwitch.isChecked()) {
                    frequency = (String) mSpinner.getSelectedItem();
                    String frequencyDate = mDateChoice.getText().toString().trim();
                    try {
                        dateFrequency = date.parse(frequencyDate);
                    }catch (ParseException e){
                        e.printStackTrace();
                    }
                }
                Income income = new Income(mDataManager.getNextId(TypeEnum.INCOME), amount, label, new Date());
                if (mSwitch.isChecked() && !(frequency.equals(FrequencyEnum.values()[0].toString()))){
                    Category cat = null;
                    Frequency freq = new Frequency(mDataManager.getNextId(TypeEnum.FREQUENCY), TypeEnum.INCOME, income.getTotal(), income.getLabel(), FrequencyEnum.valueOf(frequency), dateFrequency, cat);
                    mDataManager.insert(freq);
                }
                mDataManager.insert(income);
                startActivity(new Intent(NewIncomeActivity.this, MainActivity.class));
            }
        }
    }

    @OnClick(R.id.dateChoice)
    public void onClickDateChoice(){
        final View layout = getLayoutInflater().inflate(R.layout.date_choice_popup, null);
        final Dialog pop = Popup.popInfo(NewIncomeActivity.this, layout);
        pop.show();
        Button cancelButton = (Button)layout.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });

        Button okButton = (Button) layout.findViewById(R.id.dateButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker datePicker = (DatePicker) layout.findViewById(R.id.dateSelection);
                String choosenDate = date.format(new Date(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                mDateChoice.setText(choosenDate);
                pop.dismiss();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }


}
