package net.cashadmin.cashadmin.Activities.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TransactionEntryEnum;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Frequency;
import net.cashadmin.cashadmin.Activities.UI.Popup;
import net.cashadmin.cashadmin.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ExpenseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.subtitle)
    TextView mSubtitle;
    @InjectView(R.id.amount)
    EditText mAmount;
    @InjectView(R.id.addExpenseButton)
    Button mAddExpenseButton;
    @InjectView(R.id.label)
    AutoCompleteTextView mLabel;
    @InjectView(R.id.recurrenceLayout)
    LinearLayout mRecurrenceLayout;
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
    @InjectView(R.id.endDateSwitchLayout)
    LinearLayout mEndDateSwitchLayout;
    @InjectView(R.id.endDateSwitch)
    Switch mEndDateSwitch;
    @InjectView(R.id.endDateLayout)
    LinearLayout mEndDateLayout;
    @InjectView(R.id.endDateChoice)
    TextView mEndDateChoice;
    @InjectView(R.id.categoryLayout)
    LinearLayout mCategoryLayout;
    @InjectView(R.id.categorySpinner)
    Spinner mCategorySpinner;

    private Category mCategory;
    private boolean newCategory;
    private DataManager mDataManager;
    private DateFormat date = new SimpleDateFormat("dd/MM/yy");
    private TransactionEntryEnum transactionEntry;
    private Expense mExpense;
    private Frequency mFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        mDataManager = DataManager.getDataManager();

        ButterKnife.inject(this);

        String currentDate = date.format(new Date());
        mDateChoice.setText(currentDate);
        mEndDateChoice.setText(currentDate);

        final Animation animHide = AnimationUtils.loadAnimation(ExpenseActivity.this, R.anim.popup_hide_up);
        final Animation animShow = AnimationUtils.loadAnimation(ExpenseActivity.this, R.anim.popup_show_down);

        Intent intent = getIntent();
        transactionEntry = TransactionEntryEnum.detachFrom(intent);

        mSpinner.setOnItemSelectedListener(this);

        List<String> listSpinner = new ArrayList<>();
        listSpinner.add(getString(R.string.Never));
        listSpinner.add(getString(R.string.Days));
        listSpinner.add(getString(R.string.Weeks));
        listSpinner.add(getString(R.string.Months));
        listSpinner.add(getString(R.string.Years));

        Map<FrequencyEnum, Integer> recurrenceMap = new HashMap<>();
        recurrenceMap.put(FrequencyEnum.NEVER, 0);
        recurrenceMap.put(FrequencyEnum.DAILY, 1);
        recurrenceMap.put(FrequencyEnum.WEEKLY, 2);
        recurrenceMap.put(FrequencyEnum.MONTHLY, 3);
        recurrenceMap.put(FrequencyEnum.YEARLY, 4);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSpinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);

        if (transactionEntry == TransactionEntryEnum.New) {
            newCategory = intent.getBooleanExtra("newCategory", false);
            mCategory = (Category) intent.getSerializableExtra("category");
        } else {
            try {
                if (transactionEntry == TransactionEntryEnum.FrequencyEdit) {
                    mFrequency = (Frequency) mDataManager.getById(Frequency.class, intent.getIntExtra("frequencyId", 0));
                    mAmount.setText(String.valueOf(mFrequency.getTotal()));
                    mLabel.setText(mFrequency.getLabel());
                    mCategory = mFrequency.getCategory();
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
                } else {
                    mExpense = (Expense) mDataManager.getById(Expense.class, intent.getIntExtra("expenseId", 0));
                    mAmount.setText(String.valueOf(mExpense.getTotal()));
                    mLabel.setText(mExpense.getLabel());
                    mCategory = mExpense.getCategory();
                }
                mAddExpenseButton.setText(R.string.saveChanges);
                mRecurrenceLayout.setVisibility(View.GONE);
                mCategoryLayout.setVisibility(View.VISIBLE);
                List<Category> categories = (ArrayList<Category>) (ArrayList<?>) mDataManager.getAll(Category.class);
                Map<String, Integer> catLabels = new HashMap<>();
                int i = 0;
                for (Category cat : categories) {
                    catLabels.put(cat.getLabel(), i);
                    i++;
                }
                List<String> listCatLabels = new ArrayList<>();
                listCatLabels.addAll(catLabels.keySet());
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCatLabels);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mCategorySpinner.setAdapter(dataAdapter);
                mCategorySpinner.setSelection(catLabels.get(mCategory.getLabel()));
            } catch (DataNotFoundException e) {
                e.printStackTrace();
            }
        }
        mSubtitle.setText(mCategory.getLabel());
        mSubtitle.setBackgroundColor(mCategory.getColor());

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mWhichRecurrenceLayout.startAnimation(animShow);
                    mWhichRecurrenceLayout.setVisibility(View.VISIBLE);
                    mDateLayout.startAnimation(animShow);
                    mDateLayout.setVisibility(View.VISIBLE);
                    mEndDateSwitchLayout.startAnimation(animShow);
                    mEndDateSwitchLayout.setVisibility(View.VISIBLE);
                } else {
                    mWhichRecurrenceLayout.startAnimation(animHide);
                    mDateLayout.startAnimation(animHide);
                    mEndDateSwitchLayout.startAnimation(animHide);
                    if (mEndDateSwitch.isChecked()) {
                        mEndDateLayout.startAnimation(animHide);
                    }
                    mEndDateSwitch.setChecked(false);
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

        final Animation sndAnimHide = AnimationUtils.loadAnimation(ExpenseActivity.this, R.anim.popup_hide_up);
        mEndDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mEndDateLayout.startAnimation(animShow);
                    mEndDateLayout.setVisibility(View.VISIBLE);
                } else {
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

        List<Expense> listExpense = (ArrayList<Expense>) (ArrayList<?>)mDataManager.getAll(Expense.class);
        List<String> listLibelle = new ArrayList<>();
        for (Expense e : listExpense) {
            listLibelle.add(e.getLabel());
        }
        String[] libelleArray = new String[listLibelle.size()];
        listLibelle.toArray(libelleArray);

        ArrayAdapter<String> libelleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, removeDuplicates(libelleArray));
        mLabel.setAdapter(libelleAdapter);

    }

    @OnClick(R.id.addExpenseButton)
    public void onClickAddExpenseButton() {
        String stringAmount = mAmount.getText().toString().trim();
        String label = mLabel.getText().toString().trim();
        FrequencyEnum frequency = FrequencyEnum.NEVER;
        Date dateFrequency = null;
        Date endDateFrequency = null;
        if (stringAmount.isEmpty()) {
            Toast toast = Popup.toast(ExpenseActivity.this, getString(R.string.fieldEmpty));
            toast.show();
        } else {
            float amount = Float.valueOf(stringAmount);
            if (mSwitch.isChecked()) {
                int idEnum = (int) mSpinner.getSelectedItemId();
                frequency = FrequencyEnum.values()[idEnum];
                try {
                    dateFrequency = date.parse(mDateChoice.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (mEndDateSwitch.isChecked()) {
                try {
                    endDateFrequency = date.parse(mEndDateChoice.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if ((!(dateFrequency == null)) && (!(endDateFrequency == null)) && (dateFrequency.after(endDateFrequency))) {
                Popup.toast(ExpenseActivity.this, getString(R.string.failDate)).show();
            } else if (transactionEntry == TransactionEntryEnum.Edit) {
                Category categorySelected = (Category) mDataManager.getWhere(Category.class, Category.COLUMN_LABEL + " = '" + mCategorySpinner.getSelectedItem().toString() + "'").get(0);
                mExpense.setLabel(label);
                mExpense.setTotal(Float.valueOf(stringAmount));
                mExpense.setCategory(categorySelected);
                mDataManager.update(mExpense);
                startActivity(new Intent(ExpenseActivity.this, MainActivity.class));
            } else if (transactionEntry == TransactionEntryEnum.FrequencyEdit) {
                Category categorySelected = (Category) mDataManager.getWhere(Category.class, Category.COLUMN_LABEL + " = '" + mCategorySpinner.getSelectedItem().toString() + "'").get(0);
                int idEnum = (int) mSpinner.getSelectedItemId();
                frequency = FrequencyEnum.values()[idEnum];
                try {
                    dateFrequency = date.parse(mDateChoice.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mFrequency.setTotal(Float.valueOf(stringAmount));
                mFrequency.setLabel(label);
                mFrequency.setCategory(categorySelected);
                mFrequency.setDateFrequency(dateFrequency);
                mFrequency.setFrenquency(frequency);
                if (mEndDateSwitch.isChecked()) {
                    mFrequency.setEndDateFrequency(endDateFrequency);
                }
                mDataManager.update(mFrequency);
                startActivity(new Intent(ExpenseActivity.this, MainActivity.class));
            } else {
                mExpense = new Expense(mDataManager.getNextId(Expense.class), amount, label, new Date(), mCategory);
                if (newCategory)
                    mDataManager.insert(mCategory);

                if (mSwitch.isChecked()) {
                    Frequency freq = new Frequency(mDataManager.getNextId(Frequency.class),
                            TypeEnum.EXPENSE, mExpense.getTotal(), mExpense.getLabel(),
                            frequency, dateFrequency, endDateFrequency, mExpense.getCategory());
                    mDataManager.insert(freq);
                } else {
                    mDataManager.insert(mExpense);
                }
                startActivity(new Intent(ExpenseActivity.this, MainActivity.class));
            }
        }

    }

    @OnClick(R.id.dateChoice)
    public void onClickDateChoice() {
        final View layout = getLayoutInflater().inflate(R.layout.date_choice_popup, null);
        final Dialog pop = Popup.popInfo(ExpenseActivity.this, layout);
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
        final Dialog pop = Popup.popInfo(ExpenseActivity.this, layout);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }

    public static String[] removeDuplicates(String[] s){
        return new HashSet<String>(Arrays.asList(s)).toArray(new String[0]);
    }
}
