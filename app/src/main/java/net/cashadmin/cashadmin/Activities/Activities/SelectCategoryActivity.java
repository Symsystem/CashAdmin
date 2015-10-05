package net.cashadmin.cashadmin.Activities.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import net.cashadmin.cashadmin.Activities.Adapter.ButtonCategoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.UI.Popup;
import net.cashadmin.cashadmin.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SelectCategoryActivity extends ActionBarActivity {

    @InjectView(R.id.gridView)
    GridView mGridView;

    private int color;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        ButterKnife.inject(this);
        mDataManager = DataManager.getDataManager();

        try {
            final List<Entity> list = mDataManager.getAll(TypeEnum.CATEGORY);
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
                                                         View layout = getLayoutInflater().inflate(R.layout.new_category_popup, null);
                                                         final Dialog popu = Popup.popInfo(SelectCategoryActivity.this, layout);
                                                         popu.show();

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

                                                         Button cancelPopup = (Button) layout.findViewById(R.id.cancelPopup);
                                                         cancelPopup.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View view) {
                                                                 popu.dismiss();
                                                             }
                                                         });
                                                         Button addFinalCategoryButton = (Button) layout.findViewById(R.id.addFinalCategoryButton);
                                                         addFinalCategoryButton.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 String name = nameCat.getText().toString().trim();

                                                                 //TODO : Faire un popup si name est vide

                                                                 Category cat = new Category(mDataManager.getNextId(TypeEnum.CATEGORY), name, color);
                                                                 Intent intent = new Intent(SelectCategoryActivity.this, NewExpenseActivity.class);
                                                                 intent.putExtra("category", cat);
                                                                 intent.putExtra("newCategory", true);
                                                                 popu.dismiss();
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
