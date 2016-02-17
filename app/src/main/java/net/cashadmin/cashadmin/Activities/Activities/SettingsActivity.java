package net.cashadmin.cashadmin.Activities.Activities;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Enum.FrequencyEnum;
import net.cashadmin.cashadmin.Activities.Model.Frequency;
import net.cashadmin.cashadmin.Activities.Model.Setting;
import net.cashadmin.cashadmin.Activities.UI.Popup;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener, AdapterView.OnItemSelectedListener {

    @InjectView(R.id.spinner)
    Spinner mSpinner;
    @InjectView(R.id.pickerDays)
    TextView mPickerDays;
    @InjectView(R.id.pickerDayInMonth)
    TextView mPickerDayInMonth;
    @InjectView(R.id.daysLayout)
    LinearLayout mDaysLayout;
    @InjectView(R.id.monthsLayout)
    LinearLayout mMonthsLayout;
    @InjectView(R.id.yearsLayout)
    LinearLayout mYearsLayout;
    @InjectView(R.id.pickerYear)
    TextView mPickerYear;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.inject(this);

        mDataManager = DataManager.getDataManager();

        List<String> list = new ArrayList<>();
        list.add(getString(R.string.Weeks));
        list.add(getString(R.string.Months));
        list.add(getString(R.string.Years));

        Map<String, Integer> cycleTypeMap = new HashMap<>();
        cycleTypeMap.put(FrequencyEnum.WEEKLY.toString(), 0);
        cycleTypeMap.put(FrequencyEnum.MONTHLY.toString(), 1);
        cycleTypeMap.put(FrequencyEnum.YEARLY.toString(), 2);

        mSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
        Setting baseConfigFreq = (Setting) mDataManager.getWhere(Setting.class, Setting.COLUMN_KEY + " = 'CYCLEFREQUENCY'").get(0);
        Setting baseConfigDate = (Setting) mDataManager.getWhere(Setting.class, Setting.COLUMN_KEY + " = 'CYCLEDATE'").get(0);
        switch (cycleTypeMap.get(baseConfigFreq.getValue())){
            case 0:
                mPickerDays.setText((baseConfigDate.getValue()));
                break;
            case 1:
                mPickerDayInMonth.setText(baseConfigDate.getValue());
                break;
            case 2:
                mPickerYear.setText(baseConfigDate.getValue());
                break;
        }

        mSpinner.setSelection(cycleTypeMap.get(baseConfigFreq.getValue()));

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Setting configFreq = (Setting) mDataManager.getWhere(Setting.class, Setting.COLUMN_KEY + " = 'CYCLEFREQUENCY'").get(0);
                Setting configDate = (Setting) mDataManager.getWhere(Setting.class, Setting.COLUMN_KEY + " = 'CYCLEDATE'").get(0);
                String cycleFrequency = FrequencyEnum.MONTHLY.toString();
                String cycleValue = mPickerDayInMonth.getText().toString();
                switch (i) {
                    case 0:
                        mDaysLayout.setVisibility(View.VISIBLE);
                        cycleFrequency = FrequencyEnum.WEEKLY.toString();
                        cycleValue = mPickerDays.getText().toString();
                        mMonthsLayout.setVisibility(View.GONE);
                        mYearsLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        cycleFrequency = FrequencyEnum.MONTHLY.toString();
                        cycleValue = mPickerDayInMonth.getText().toString();
                        mMonthsLayout.setVisibility(View.VISIBLE);
                        mDaysLayout.setVisibility(View.GONE);
                        mYearsLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        cycleFrequency = FrequencyEnum.YEARLY.toString();
                        cycleValue = mPickerYear.getText().toString();
                        mYearsLayout.setVisibility(View.VISIBLE);
                        mDaysLayout.setVisibility(View.GONE);
                        mMonthsLayout.setVisibility(View.GONE);
                }
                configFreq.setValue(cycleFrequency);
                configDate.setValue(cycleValue);
                mDataManager.update(configFreq);
                mDataManager.update(configDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.pickerYear)
    public void onClickPickerYear() {
        showYear();
    }

    @OnClick(R.id.pickerDays)
    public void onClickPickerMonth() {
        showDays();
    }

    @OnClick(R.id.pickerDayInMonth)
    public void onClickPickerDay() {
        showDayInMonth();
    }

    public void showDays() {
        final Dialog d = new Dialog(SettingsActivity.this);
        d.setTitle(R.string.chooseDay);
        d.setContentView(R.layout.dialog_number_picker);
        Button buttonOk = (Button) d.findViewById(R.id.buttonOk);
        Button buttonCancel = (Button) d.findViewById(R.id.buttonCancel);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(6);
        np.setMinValue(0);
        final String[] weekDays = new String[]{getString(R.string.monday), getString(R.string.tuesday), getString(R.string.wednesday), getString(R.string.thursday), getString(R.string.friday), getString(R.string.saterday), getString(R.string.sunday)};
        np.setValue(Arrays.asList(weekDays).indexOf(mPickerDays.getText().toString()));
        np.setDisplayedValues(weekDays);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickerDays.setText(weekDays[np.getValue()]);
                Setting configDate = (Setting) mDataManager.getWhere(Setting.class, Setting.COLUMN_KEY + " = 'CYCLEDATE'").get(0);
                String cycleValue = mPickerDays.getText().toString();
                configDate.setValue(cycleValue);
                mDataManager.update(configDate);
                d.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void showDayInMonth() {
        final Dialog d = new Dialog(SettingsActivity.this);
        d.setTitle(R.string.chooseDay);
        d.setContentView(R.layout.dialog_number_picker);
        Button buttonOk = (Button) d.findViewById(R.id.buttonOk);
        Button buttonCancel = (Button) d.findViewById(R.id.buttonCancel);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(31);
        np.setMinValue(1);
        np.setValue(Integer.parseInt(mPickerDayInMonth.getText().toString()));
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickerDayInMonth.setText(String.valueOf(np.getValue()));
                Setting configDate = (Setting) mDataManager.getWhere(Setting.class, Setting.COLUMN_KEY + " = 'CYCLEDATE'").get(0);
                String cycleValue = mPickerDayInMonth.getText().toString();
                configDate.setValue(cycleValue);
                mDataManager.update(configDate);
                d.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    public void showYear() {
        final Dialog d = new Dialog(SettingsActivity.this);
        d.setTitle(R.string.chooseDay);
        d.setContentView(R.layout.dialog_number_picker);
        Button buttonOk = (Button) d.findViewById(R.id.buttonOk);
        Button buttonCancel = (Button) d.findViewById(R.id.buttonCancel);
        final NumberPicker np1 = (NumberPicker) d.findViewById(R.id.numberPicker1);
        String[] parts = mPickerYear.getText().toString().split("  ");
        String part1 = parts[0];
        String part2 = parts[1];
        np1.setMaxValue(31);
        np1.setMinValue(1);
        np1.setValue(Integer.parseInt(part1));
        np1.setWrapSelectorWheel(false);
        np1.setOnValueChangedListener(this);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
        np2.setVisibility(View.VISIBLE);
        np2.setMaxValue(11);
        np2.setMinValue(0);
        final String[] weekDays = new String[]{getString(R.string.january), getString(R.string.february), getString(R.string.march), getString(R.string.april), getString(R.string.may), getString(R.string.june), getString(R.string.july), getString(R.string.august), getString(R.string.september), getString(R.string.october), getString(R.string.november), getString(R.string.december)};
        np2.setValue(Arrays.asList(weekDays).indexOf(part2));
        np2.setDisplayedValues(weekDays);
        np2.setWrapSelectorWheel(false);
        np2.setOnValueChangedListener(this);
        np2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((((np2.getValue() % 2) == 1) && (np1.getValue() == 31)) || ((np2.getValue() == 1) && (np1.getValue() > 28))) {
                    Popup.toast(SettingsActivity.this, getString(R.string.availableDate)).show();
                } else {
                    mPickerYear.setText(String.valueOf(np1.getValue()) + "  " + weekDays[np2.getValue()]);
                    Setting configDate = (Setting) mDataManager.getWhere(Setting.class, Setting.COLUMN_KEY + " = 'CYCLEDATE'").get(0);
                    String cycleValue = mPickerYear.getText().toString();
                    configDate.setValue(cycleValue);
                    mDataManager.update(configDate);
                    d.dismiss();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
    }
}
