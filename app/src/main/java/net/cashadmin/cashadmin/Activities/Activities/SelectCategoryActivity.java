package net.cashadmin.cashadmin.Activities.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListAdapter;

import net.cashadmin.cashadmin.Activities.Adapter.ButtonCategoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SelectCategoryActivity extends ActionBarActivity {

    @InjectView(R.id.gridView) GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        ButterKnife.inject(this);

        DataManager dataManager = new DataManager(this);
        ListAdapter adapter = new ButtonCategoryAdapter(
                this,
                android.R.layout.simple_list_item_1,
                dataManager.getAll(TypeEnum.CATEGORY)
        );
        mGridView.setAdapter(adapter);
    }
}
