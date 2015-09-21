package net.cashadmin.cashadmin.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import net.cashadmin.cashadmin.Activities.Model.Category;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.R;

import java.util.List;

public class ButtonCategoryAdapter extends ArrayAdapter<Entity> {

    public ButtonCategoryAdapter(Context context, int textViewRessourceId, List<Entity> categories) {
        super(context, textViewRessourceId, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_category, null);
        }
        if (position == 0) {
            Button buttonAddActegory = (Button) v.findViewById(R.id.buttonCat);
            buttonAddActegory.setText("+");
            buttonAddActegory.setBackgroundResource(R.color.White);
            buttonAddActegory.setFocusable(false);
            buttonAddActegory.setClickable(false);
        } else {
            Category category = (Category) this.getItem(position - 1);
            if (category != null) {
                Button buttonCategory = (Button) v.findViewById(R.id.buttonCat);
                buttonCategory.setText(category.getLabel());
                buttonCategory.setBackgroundColor(category.getColor());
                buttonCategory.setFocusable(false);
                buttonCategory.setClickable(false);
            }
        }

        return v;
    }
}
