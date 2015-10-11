package net.cashadmin.cashadmin.Activities.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ListView;

import net.cashadmin.cashadmin.Activities.Adapter.TransactionHistoryAdapter;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;
import net.cashadmin.cashadmin.Activities.Model.Transaction;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HistoricActivity extends AppCompatActivity {

    private static final int MOVE_DURATION = 250;

    private DataManager mDataManager;
    private TransactionHistoryAdapter mAdapter;
    private HashMap<Long, Integer> mItemIdTopMap = new HashMap<>();

    @InjectView(R.id.historyList)
    ListView mHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        ButterKnife.inject(this);
        mDataManager = DataManager.getDataManager();
        try {
            ArrayList<Transaction> transactions = mergeExpenseIncome(
                    mDataManager.getAll(TypeEnum.EXPENSE),
                    mDataManager.getAll(TypeEnum.INCOME));

            mAdapter = new TransactionHistoryAdapter(HistoricActivity.this, transactions, mOnClickDeleteListener);
            mHistoryList.setAdapter(mAdapter);
        } catch (IllegalTypeException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener mOnClickDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    animateRemoval(mHistoryList, view);
                }
            };
            r.run();
        }
    };

    /**
     * This method animates all other views in the ListView container (not including ignoreView)
     * into their final positions. It is called after ignoreView has been removed from the
     * adapter, but before layout has been run. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private void animateRemoval(final ListView listview, View viewToRemove) {
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = mAdapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        final int position = listview.getPositionForView(viewToRemove);
        Transaction t = mAdapter.getItem(position);
        mDataManager.delete(t);
        mAdapter.remove(t);

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = position; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = mAdapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

    private ArrayList<Transaction> mergeExpenseIncome(List<Entity> l1, List<Entity> l2){
        ArrayList<Transaction> transactions = new ArrayList<>();
        Transaction t1, t2;
        int i1 = 0, i2 = 0;

        while(i1 < l1.size() && i2 < l2.size()){
            t1 = (Transaction) l1.get(i1);
            t2 = (Transaction) l2.get(i2);

            if(t1.getDate().compareTo(t2.getDate()) >= 0){
                transactions.add(t1);
                i1++;
            }
            else {
                transactions.add(t2);
                i2++;
            }
        }

        for(; i1 < l1.size(); i1++)
            transactions.add((Transaction) l1.get(i1));

        for(;i2 < l2.size(); i2++)
            transactions.add((Transaction) l2.get(i2));

        return transactions;
    }
}
