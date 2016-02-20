package net.cashadmin.cashadmin.Activities.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TransactionEntryEnum;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class IncomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    @InjectView(R.id.labelLayout)
    LinearLayout mLabelLayout;
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
    @InjectView(R.id.endDateSwitchLayout)
    LinearLayout mEndDateSwitchLayout;
    @InjectView(R.id.endDateSwitch)
    Switch mEndDateSwitch;
    @InjectView(R.id.endDateLayout)
    LinearLayout mEndDateLayout;
    @InjectView(R.id.endDateChoice)
    TextView mEndDateChoice;

    private DataManager mDataManager;
    private DateFormat date = new SimpleDateFormat("dd/MM/yy");
    private TransactionEntryEnum transactionEntry;
    private Income mIncome;
    private Frequency mFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        mDataManager = DataManager.getDataManager();

        ButterKnife.inject(this);

        String currentDate = date.format(new Date());
        mDateChoice.setText(currentDate);
        mEndDateChoice.setText(currentDate);

        final Animation animShow = AnimationUtils.loadAnimation(IncomeActivity.this, R.anim.popup_show_down);
        final Animation animHide = AnimationUtils.loadAnimation(IncomeActivity.this, R.anim.popup_hide_up);

        Intent intent = getIntent();
        transactionEntry = TransactionEntryEnum.detachFrom(intent);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                float startY = mAmountLayout.getTop();
                float endY = mRecurrenceLayout.getBottom();
                if (b) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAddIncomeButton.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.endDateSwitchLayout);
                    mAddIncomeButton.setLayoutParams(params);
                    mWhichRecurrenceLayout.startAnimation(animShow);
                    mWhichRecurrenceLayout.setVisibility(View.VISIBLE);
                    mDateLayout.startAnimation(animShow);
                    mDateLayout.setVisibility(View.VISIBLE);
                    mEndDateSwitchLayout.startAnimation(animShow);
                    mEndDateSwitchLayout.setVisibility(View.VISIBLE);
                    makeAnim(startY - endY);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAddIncomeButton.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.recurrenceLayout);
                    mAddIncomeButton.setLayoutParams(params);
                    makeAnim(endY - startY);
                    mEndDateSwitch.setChecked(false);
                    mWhichRecurrenceLayout.startAnimation(animHide);
                    mDateLayout.startAnimation(animHide);
                    mEndDateSwitchLayout.startAnimation(animHide);
                    if (mEndDateSwitch.isChecked()) {
                        mEndDateLayout.startAnimation(animHide);
                    }
                    animHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mWhichRecurrenceLayout.setVisibility(View.GONE);
                            mDateLayout.setVisibility(View.GONE);
                            mEndDateSwitchLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        final Animation sndAnimHide = AnimationUtils.loadAnimation(IncomeActivity.this, R.anim.popup_hide_up);
        mEndDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                float startY = mAmountLayout.getBottom();
                float endY = mLabelLayout.getBottom();
                if (b) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAddIncomeButton.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.endDateLayout);
                    mAddIncomeButton.setLayoutParams(params);
                    mEndDateLayout.startAnimation(animShow);
                    mEndDateLayout.setVisibility(View.VISIBLE);
                    makeAnim(startY - endY);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAddIncomeButton.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.endDateSwitchLayout);
                    mAddIncomeButton.setLayoutParams(params);
                    makeAnim(endY - startY);
                    mEndDateLayout.startAnimation(sndAnimHide);
                    sndAnimHide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mEndDateLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });

        mSpinner.setOnItemSelectedListener(this);

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add(getString(R.string.Never));
        listSpinner.add(getString(R.string.Days));
        listSpinner.add(getString(R.string.Weeks));
        listSpinner.add(getString(R.string.Months));
        listSpinner.add(getString(R.string.Years));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);

        if (transactionEntry == TransactionEntryEnum.New) {
        } else {
            try {
                if (transactionEntry == TransactionEntryEnum.FrequencyEdit) {

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mAddIncomeButton.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.endDateSwitchLayout);
                    mAddIncomeButton.setLayoutParams(params);

                    Map<FrequencyEnum, Integer> recurrenceMap = new HashMap<>();
                    recurrenceMap.put(FrequencyEnum.NEVER, 0);
                    recurrenceMap.put(FrequencyEnum.DAILY, 1);
                    recurrenceMap.put(FrequencyEnum.WEEKLY, 2);
                    recurrenceMap.put(FrequencyEnum.MONTHLY, 3);
                    recurrenceMap.put(FrequencyEnum.YEARLY, 4);

                    mFrequency = (Frequency) mDataManager.getById(Frequency.class, intent.getIntExtra("frequencyId", 0));
                    mAmount.setText(String.valueOf(mFrequency.getTotal()));
                    mLabel.setText(mFrequency.getLabel());
                    mEndDateChoice.setText(date.format(mFrequency.getEndDateFrequency()));
                    if (!date.format(mFrequency.getEndDateFrequency()).equals("31/12/69")) {
                        mEndDateSwitch.setChecked(true);
                        mEndDateLayout.setVisibility(View.VISIBLE);
                    } else {
                        mEndDateChoice.setText(currentDate);
                    }
                    mEndDateSwitchLayout.setVisibility(View.VISIBLE);
                    mDateLayout.setVisibility(View.VISIBLE);
                    mDateChoice.setText(date.format(mFrequency.getDateFrequency()));
                    mSpinner.setSelection(recurrenceMap.get(mFrequency.getFrequency()));
                    mWhichRecurrenceLayout.setVisibility(View.VISIBLE);
                    mAddIncomeButton.setText(R.string.saveChanges);
                    mRecurrenceLayout.setVisibility(View.GONE);
                }else{
                        mIncome = (Income) mDataManager.getById(Income.class, intent.getIntExtra("incomeId", 0));
                        mRecurrenceLayout.setVisibility(View.GONE);
                        mAddIncomeButton.setText(getString(R.string.saveChanges));
                        mAmount.setText(String.valueOf(mIncome.getTotal()));
                        mLabel.setText(mIncome.getLabel());
                    }
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick(R.id.addIncomeButton)
    public void onClickIncomeButton() {
        String stringAmount = mAmount.getText().toString().trim();
        String label = mLabel.getText().toString().trim();
        FrequencyEnum frequency = FrequencyEnum.NEVER;
        Date dateFrequency = null;
        Date endDateFrequency = null;
        if (!(stringAmount.matches("-?\\d+(\\.\\d+)?"))) {
            Toast toast = Popup.toast(IncomeActivity.this, getString(R.string.validAmount));
            toast.show();
        } else {
            if (label.isEmpty() || stringAmount.isEmpty()) {
                Toast toast = Popup.toast(IncomeActivity.this, getString(R.string.fieldEmpty));
                toast.show();
            } else {
                float amount = Float.valueOf(stringAmount);
                if (mSwitch.isChecked()) {
                    int idEnum = (int) mSpinner.getSelectedItemId();
                    frequency = FrequencyEnum.values()[idEnum];
                    String frequencyDate = mDateChoice.getText().toString().trim();
                    try {
                        dateFrequency = date.parse(frequencyDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (mEndDateSwitch.isChecked()) {
                    String frequencyEndDate = mEndDateChoice.getText().toString().trim();
                    try {
                        endDateFrequency = date.parse(frequencyEndDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if ((!(dateFrequency == null)) && (!(endDateFrequency == null)) && (dateFrequency.after(endDateFrequency))) {
                    Popup.toast(IncomeActivity.this, getString(R.string.failDate)).show();
                } else if (transactionEntry == TransactionEntryEnum.New) {
                    mIncome = new Income(mDataManager.getNextId(Income.class), amount, label, new Date());
                    if (mSwitch.isChecked()) {
                        Category cat = new Category(0, getString(R.string.income), R.color.green);
                        Frequency freq = new Frequency(mDataManager.getNextId(Frequency.class), TypeEnum.INCOME, mIncome.getTotal(), mIncome.getLabel(), frequency, dateFrequency, endDateFrequency, cat);
                        mDataManager.insert(freq);
                    } else {
                        mDataManager.insert(mIncome);
                    }
                    startActivity(new Intent(IncomeActivity.this, MainActivity.class));
                } else if (transactionEntry == TransactionEntryEnum.FrequencyEdit) {
                    int idEnum = (int) mSpinner.getSelectedItemId();
                    frequency = FrequencyEnum.values()[idEnum];
                    try {
                        dateFrequency = date.parse(mDateChoice.getText().toString().trim());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mFrequency.setTotal(Float.valueOf(stringAmount));
                    mFrequency.setLabel(label);
                    mFrequency.setDateFrequency(dateFrequency);
                    mFrequency.setFrenquency(frequency);
                    if (mEndDateSwitch.isChecked()) {
                        mFrequency.setEndDateFrequency(endDateFrequency);
                    }
                    mDataManager.update(mFrequency);
                    startActivity(new Intent(IncomeActivity.this, MainActivity.class));
                }
                else {
                    mIncome.setLabel(label);
                    mIncome.setTotal(amount);
                    mDataManager.update(mIncome);
                    startActivity(new Intent(IncomeActivity.this, MainActivity.class));
                }
            }
        }
    }

    @OnClick(R.id.dateChoice)
    public void onClickDateChoice() {
        final View layout = getLayoutInflater().inflate(R.layout.date_choice_popup, null);
        final Dialog pop = Popup.popInfo(IncomeActivity.this, layout);
        pop.show();
        Button cancelButton = (Button) layout.findViewById(R.id.cancelButton);
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
                String choosenDate = date.format(new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
                mDateChoice.setText(choosenDate);
                pop.dismiss();
            }
        });
    }

    @OnClick(R.id.endDateChoice)
    public void onClickEndDateChoice() {
        final View layout = getLayoutInflater().inflate(R.layout.date_choice_popup, null);
        final Dialog pop = Popup.popInfo(IncomeActivity.this, layout);
        pop.show();
        Button cancelButton = (Button) layout.findViewById(R.id.cancelButton);
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
                String choosenDate = date.format(new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth()));
                mEndDateChoice.setText(choosenDate);
                pop.dismiss();
            }
        });
    }

    private void makeAnim(float moove) {
        Animation animSample = new TranslateAnimation(0, 0, moove, 0);
        animSample.setDuration(200);
        animSample.setFillAfter(true);
        animSample.setInterpolator(new LinearInterpolator());
        mAddIncomeButton.startAnimation(animSample);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
