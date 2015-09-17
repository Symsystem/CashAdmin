package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SelectCategoryActivity extends ActionBarActivity {

    @InjectView(R.id.gridView)
    GridView mGridView;
    @InjectView(R.id.addCategoryButton)
    ImageButton mAddCategoryButton;
    @InjectView(R.id.colorChoice)
    TextView mColorChoice;
    @InjectView(R.id.addFinalCategoryButton)
    Button mAddFinalCategoryButton;
    @InjectView(R.id.nameCat)
    EditText mNameCat;

    private int color;
    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        ButterKnife.inject(this);

        DataManager dataManager = new DataManager(this);
        List<Entity> list = null;
        try {
            list = dataManager.getAll(TypeEnum.CATEGORY);
        } catch (IllegalTypeException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new ButtonCategoryAdapter(
                this,
                android.R.layout.simple_list_item_1,
                list
        );
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView parent, View itemClicked, int position, long id) {

                                                 Button button = (Button) mGridView.getItemAtPosition(position);
                                                 String cat = (String) button.getText();
                                                 Intent intent = new Intent(SelectCategoryActivity.this, NewExpenseActivity.class);
                                                 intent.putExtra("catName", cat);
                                                 startActivity(intent);
                                             }
                                         }
        );
    }

    @OnClick(R.id.addCategoryButton)
    public void onClickAddCategoryButton() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        PopupWindow pop = new PopupWindow(getLayoutInflater().inflate(R.layout.new_category_popup,null));
        pop.setAnimationStyle(-1);
    }

    @OnClick(R.id.colorChoice)
    public void onClickColorChoice(){
        ColorPickerDialogBuilder
                .with(this)
                .setTitle(getString(R.string.ChooseColor))
                .initialColor(R.color.White)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setPositiveButton("Ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                        mColorChoice.setBackgroundColor(i);
                        color = i;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @OnClick(R.id.addFinalCategoryButton)
    public void onClickAddFinalCategoryButton(){
        String name = mNameCat.getText().toString().trim();

        //TODO : Faire un popup si name est vide

        Category cat = new Category(0, name, color);
        mDataManager.insert(cat);
        Intent intent = new Intent(SelectCategoryActivity.this, NewExpenseActivity.class);
        intent.putExtra("catName", name);
        startActivity(intent);
    }
}
