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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Frequency;
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

public class NewExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.subtitle)
    TextView mSubtitle;
    @InjectView(R.id.amount)
    EditText mAmount;
    @InjectView(R.id.label)
    EditText mLabel;
    @InjectView(R.id.mySwitch)
    Switch mSwitch;
    @InjectView(R.id.whicheRecurrenceLayout)
    LinearLayout mWhichRecurrenceLayout;
    @InjectView(R.id.dateLayout)
    LinearLayout mDateLayout;
    @InjectView(R.id.spinner)
    Spinner mSpinner;
    @InjectView(R.id.dateChoice)
    TextView mDateChoice;

    private Category mCategory;
    private boolean newCategory;
    private DataManager mDataManager;
    private DateFormat date = new SimpleDateFormat("dd/MM/yy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);
        mDataManager = DataManager.getDataManager();

        ButterKnife.inject(this);

        String currentDate = date.format(new Date());
        mDateChoice.setText(currentDate);

        Intent intent = getIntent();
        newCategory = intent.getBooleanExtra("newCategory", false);
        mCategory = (Category) intent.getSerializableExtra("category");
        mSubtitle.setText(mCategory.getLabel());
        mSubtitle.setBackgroundColor(mCategory.getColor());

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Animation animShow = AnimationUtils.loadAnimation(NewExpenseActivity.this, R.anim.popup_show_down);
                Animation animHide = AnimationUtils.loadAnimation(NewExpenseActivity.this, R.anim.popup_hide_up);
                if (b) {
                    mWhichRecurrenceLayout.startAnimation(animShow);
                    mWhichRecurrenceLayout.setVisibility(View.VISIBLE);
                    mDateLayout.startAnimation(animShow);
                    mDateLayout.setVisibility(View.VISIBLE);
                } else {
                    mWhichRecurrenceLayout.startAnimation(animHide);
                    mWhichRecurrenceLayout.setVisibility(View.GONE);
                    mDateLayout.startAnimation(animHide);
                    mDateLayout.setVisibility(View.GONE);
                }
            }
        });

        mSpinner.setOnItemSelectedListener(this);

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add(0, getString(R.string.Never));
        listSpinner.add(1, getString(R.string.Days));
        listSpinner.add(2, getString(R.string.Weeks));
        listSpinner.add(3, getString(R.string.Months));
        listSpinner.add(4, getString(R.string.Years));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);

    }

    @OnClick(R.id.addExpenseButton)
    public void onClickAddExpenseButton() {
        String stringAmount = mAmount.getText().toString().trim();
        String label = mLabel.getText().toString().trim();
        String frequency = FrequencyEnum.values()[0].toString();
        Date dateFrequency = null;
        if (stringAmount.isEmpty()) {
            Toast toast = Popup.toast(NewExpenseActivity.this, getString(R.string.fieldEmpty));
            toast.show();
        } else {
            float amount = Float.valueOf(stringAmount);
            if (mSwitch.isChecked()) {
                int idEnum = (int) mSpinner.getSelectedItemId();
                frequency = FrequencyEnum.values()[idEnum].toString();
                String frequencyDate = mDateChoice.getText().toString().trim();
                try {
                    dateFrequency = date.parse(frequencyDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Expense expense = new Expense(mDataManager.getNextId(TypeEnum.EXPENSE), amount, label, new Date(), mCategory);
            if (newCategory)
                mDataManager.insert(mCategory);
            if (mSwitch.isChecked() && !(frequency.equals(FrequencyEnum.values()[0].toString()))) {
                Frequency freq = new Frequency(mDataManager.getNextId(TypeEnum.FREQUENCY), TypeEnum.EXPENSE, expense.getTotal(), expense.getLabel(), FrequencyEnum.valueOf(frequency), dateFrequency, expense.getCategory());
                mDataManager.insert(freq);
            }
            mDataManager.insert(expense);
            startActivity(new Intent(NewExpenseActivity.this, MainActivity.class));
        }

    }

    @OnClick(R.id.dateChoice)
    public void onClickDateChoice() {
        final View layout = getLayoutInflater().inflate(R.layout.date_choice_popup, null);
        final Dialog pop = Popup.popInfo(NewExpenseActivity.this, layout);
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
