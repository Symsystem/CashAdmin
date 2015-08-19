package net.cashadmin.cashadmin.Activities.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;

import net.cashadmin.cashadmin.Activities.Adapter.ButtonCategoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.DataNotFoundException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SelectCategoryActivity extends ActionBarActivity {

    @InjectView(R.id.gridView)
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        ButterKnife.inject(this);

        DataManager dataManager = new DataManager(this);
        List<Entity> list = null;
        try{
            list = dataManager.getAll(TypeEnum.CATEGORY);
        } catch (DataNotFoundException e) {}

        ListAdapter adapter = new ButtonCategoryAdapter(
                this,
                android.R.layout.simple_list_item_1,
                list
        );
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                         @Override
                                         public void onItemClick(AdapterView parent, View itemClicked, int position, long id) {
                                             // TODO Auto-generated method stub

                                             Button button = (Button) mGridView.getItemAtPosition(position);
                                             String cat = (String)button.getText();
                                             Intent intent = new Intent(SelectCategoryActivity.this, NewExpenseActivity.class);
                                             intent.putExtra("catName", cat);
                                             startActivity(intent);
                                         }
                                     }
        );
    }
}
