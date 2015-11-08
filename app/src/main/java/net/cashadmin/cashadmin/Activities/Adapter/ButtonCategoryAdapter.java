package net.cashadmin.cashadmin.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;

public class ButtonCategoryAdapter extends ArrayAdapter<Category> {

    private ArrayList<Category> mCategories;

    public ButtonCategoryAdapter(Context context, int textViewRessourceId, ArrayList<Category> categories) {
        super(context, textViewRessourceId, categories);
        mCategories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_category, null);
        }
            Category category = (Category) this.getItem(position);
            if (category != null) {
                Button buttonCategory = (Button) v.findViewById(R.id.buttonCat);
                buttonCategory.setText(category.getLabel());
                buttonCategory.setTextColor(category.getColor());
                buttonCategory.setFocusable(false);
                buttonCategory.setClickable(false);
            }
        return v;
    }

    /*public synchronized void refreshAdapter(ArrayList<Category> categs){
        mCategories.clear();
        mCategories.addAll(categs);
        notifyDataSetChanged();
    }*/
}
