package net.cashadmin.cashadmin.Activities.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import net.cashadmin.cashadmin.Activities.Adapter.ButtonCategoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.R;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SelectCategoryActivity extends ActionBarActivity {

    @InjectView(R.id.gridView)
    GridView mGridView;
    @InjectView(R.id.mainLayout)
    FrameLayout mMainLayout;

    private int color;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        ButterKnife.inject(this);
        mDataManager = new DataManager(this);

        mMainLayout.getForeground().setAlpha(0);

        DataManager dataManager = new DataManager(this);
        try {
            final List<Entity> list = dataManager.getAll(TypeEnum.CATEGORY);
            list.add(0, new Category(0, "+", "#000000"));
            ListAdapter adapter = new ButtonCategoryAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    list
            );
            mGridView.setAdapter(adapter);
            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView parent, View itemClicked, int position, long id) {

                                                     if (position == 0) {
                                                         final PopupWindow pop = new PopupWindow(SelectCategoryActivity.this);
                                                         View layout = getLayoutInflater().inflate(R.layout.new_category_popup, null);
                                                         pop.setContentView(layout);
                                                         pop.setFocusable(true);
                                                         pop.setAnimationStyle(R.style.DialogAnimation);
                                                         pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button));
                                                         pop.setElevation(10);
                                                         pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                                             @Override
                                                             public void onDismiss() {
                                                                 mMainLayout.getForeground().setAlpha(0);
                                                             }
                                                         });
                                                         pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                                                         pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                                                         pop.showAtLocation(layout, Gravity.TOP, 0, 250);
                                                         mMainLayout.getForeground().setAlpha(200);

                                                         final TextView colorChoice = (TextView) layout.findViewById(R.id.colorChoice);
                                                         colorChoice.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 ColorPickerDialogBuilder
                                                                         .with(SelectCategoryActivity.this)
                                                                         .setTitle(getString(R.string.ChooseColor))
                                                                         .initialColor(((ColorDrawable) colorChoice.getBackground()).getColor())
                                                                         .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                                                                         .density(6)
                                                                         .showAlphaSlider(false)
                                                                         .setPositiveButton("Ok", new ColorPickerClickListener() {
                                                                             @Override
                                                                             public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                                                                                 colorChoice.setBackgroundColor(i);
                                                                                 color = i;
                                                                             }
                                                                         })
                                                                         .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(DialogInterface dialog, int which) {
                                                                             }
                                                                         })
                                                                         .build()
                                                                         .show();
                                                             }
                                                         });
                                                         final EditText nameCat = (EditText) layout.findViewById(R.id.nameCat);
                                                         Button addFinalCategoryButton = (Button) layout.findViewById(R.id.addFinalCategoryButton);
                                                         addFinalCategoryButton.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 String name = nameCat.getText().toString().trim();

                                                                 //TODO : Faire un popup si name est vide

                                                                 Category cat = new Category(0, name, color);
                                                                 mDataManager.insert(cat);
                                                                 Intent intent = new Intent(SelectCategoryActivity.this, NewExpenseActivity.class);
                                                                 intent.putExtra("category", cat);
                                                                 mMainLayout.getForeground().setAlpha(0);
                                                                 pop.dismiss();
                                                                 startActivity(intent);
                                                             }
                                                         });
                                                     } else {
                                                         Category cat = (Category) list.get(position);
                                                         Intent intent = new Intent(SelectCategoryActivity.this, NewExpenseActivity.class);
                                                         intent.putExtra("category", cat);
                                                         startActivity(intent);
                                                     }
                                                 }
                                             }
            );
        } catch (IllegalTypeException e) {
            e.printStackTrace();
        }
    }
}
