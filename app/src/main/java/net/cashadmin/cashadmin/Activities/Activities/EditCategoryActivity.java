package net.cashadmin.cashadmin.Activities.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import net.cashadmin.cashadmin.Activities.Adapter.ButtonCategoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.UI.Popup;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EditCategoryActivity extends AppCompatActivity {

    @InjectView(R.id.gridView)
    GridView mGridView;

    private int color = R.color.White;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        ButterKnife.inject(this);
        mDataManager = DataManager.getDataManager();

        final ArrayList<Category> list = (ArrayList<Category>) (ArrayList<?>) mDataManager.getAll(Category.class);
        list.add(0, new Category(0, "+", "#000000"));
        final ButtonCategoryAdapter adapter = new ButtonCategoryAdapter(
                this,
                android.R.layout.simple_list_item_1,
                list
        );
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView parent, View itemClicked, final int position, long id) {

                 if (position == 0) {
                     View layout = getLayoutInflater().inflate(R.layout.new_category_popup, null);
                     final Dialog pop = Popup.popInfo(EditCategoryActivity.this, layout);
                     Window window = pop.getWindow();
                     window.setLayout((6 * getResources().getDisplayMetrics().widthPixels) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
                     pop.show();

                     final TextView colorChoice = (TextView) layout.findViewById(R.id.colorChoice);
                     color = ((ColorDrawable) colorChoice.getBackground()).getColor();
                     colorChoice.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             ColorPickerDialogBuilder
                                     .with(EditCategoryActivity.this)
                                     .setTitle(getString(R.string.ChooseColor))
                                     .initialColor(ContextCompat.getColor(EditCategoryActivity.this, R.color.White))
                                     .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                                     .density(6)
                                     .showAlphaSlider(false)
                                     .setPositiveButton(getString(R.string.ok), new ColorPickerClickListener() {
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
                             pop.dismiss();
                         }
                     });
                     Button addFinalCategoryButton = (Button) layout.findViewById(R.id.addFinalCategoryButton);
                     addFinalCategoryButton.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             String name = nameCat.getText().toString().trim();
                             if (name.isEmpty()) {
                                 Toast toast = Popup.toast(EditCategoryActivity.this, getString(R.string.emptyName));
                                 toast.show();
                             } else {
                                 Category cat = new Category(mDataManager.getNextId(Category.class), name, color);
                                 mDataManager.insert(cat);
                                 list.add(cat);
                                 adapter.notifyDataSetChanged();
                                 pop.dismiss();
                             }
                         }
                     });
                 } else {

                     View layout = getLayoutInflater().inflate(R.layout.edit_category_popup, null);
                     final Dialog pop = Popup.popInfo(EditCategoryActivity.this, layout);
                     Window window = pop.getWindow();
                     window.setLayout((6 * getResources().getDisplayMetrics().widthPixels) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
                     pop.show();
                     final Category cat = list.get(position);
                     color = cat.getColor();
                     final TextView colorChoice = (TextView) layout.findViewById(R.id.colorChoice);
                     colorChoice.setBackgroundColor(cat.getColor());
                     colorChoice.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             ColorPickerDialogBuilder
                                     .with(EditCategoryActivity.this)
                                     .setTitle(getString(R.string.ChooseColor))
                                     .initialColor(color)
                                     .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                                     .density(6)
                                     .showAlphaSlider(false)
                                     .setPositiveButton(getString(R.string.ok), new ColorPickerClickListener() {
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
                     nameCat.setText(cat.getLabel());

                     Button cancelPopup = (Button) layout.findViewById(R.id.cancelPopup);
                     cancelPopup.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             pop.dismiss();
                         }
                     });

                     Button deleteCategoryButton = (Button) layout.findViewById(R.id.deleteCategory);
                     deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             if (position == 1) {
                                 Popup.toast(EditCategoryActivity.this, getString(R.string.cantDeleteCat)).show();
                             } else {
                                 mDataManager.delete(cat);
                                 adapter.remove(cat);
                                 adapter.notifyDataSetChanged();
                                 pop.dismiss();
                             }
                         }
                     });

                     Button editCategoryButton = (Button) layout.findViewById(R.id.editCategoryButton);
                     editCategoryButton.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             String name = nameCat.getText().toString().trim();
                             if (name.isEmpty()) {
                                 Toast toast = Popup.toast(EditCategoryActivity.this, getString(R.string.emptyName));
                                 toast.show();
                             } else {
                                 cat.setColor(color);
                                 cat.setLabel(name);
                                 mDataManager.update(cat);
                                 pop.dismiss();
                                 Category changeCat = (Category) mGridView.getItemAtPosition(position);
                                 changeCat.setLabel(name);
                                 changeCat.setColor(color);
                                 adapter.notifyDataSetChanged();
                             }
                         }
                     });
                 }
             }
        });
        Button validateButton = (Button) findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditCategoryActivity.this, MainActivity.class));
            }
        });
    }
}
