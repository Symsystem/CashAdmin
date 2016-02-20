package net.cashadmin.cashadmin.Activities.Alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import net.cashadmin.cashadmin.Activities.Activities.MainActivity;
import net.cashadmin.cashadmin.Activities.Database.DataManager;
import net.cashadmin.cashadmin.Activities.Model.Entity;
import net.cashadmin.cashadmin.Activities.Model.Expense;
import net.cashadmin.cashadmin.Activities.Model.Frequency;
import net.cashadmin.cashadmin.Activities.Model.Income;
import net.cashadmin.cashadmin.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SchedulingService extends IntentService {

    private DataManager mDataManager;

    public SchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mDataManager = DataManager.getDataManager(this);
        List<Entity> transactions;

        transactions = mDataManager.getAll(Frequency.class);
        for (Entity entity : transactions) {
            Frequency frequency = (Frequency) entity;
            Calendar currentDate = Calendar.getInstance();
            Calendar frequencyDate = Calendar.getInstance();
            Calendar endFrequencyDate = Calendar.getInstance();
            frequencyDate.setTime(frequency.getDateFrequency());
            endFrequencyDate.setTime(frequency.getEndDateFrequency());
            switch (frequency.getFrequency()) {
                case NEVER:
                    if (currentDate.get(Calendar.DAY_OF_YEAR) == frequencyDate.get(Calendar.DAY_OF_YEAR)){
                        handleFrequency(frequency);
                        mDataManager.delete(frequency);
                    }
                case DAILY:
                    handleFrequency(frequency);
                    if (currentDate.get(Calendar.DAY_OF_YEAR) == endFrequencyDate.get(Calendar.DAY_OF_YEAR)) {
                        mDataManager.delete(frequency);
                    }
                    break;
                case WEEKLY:
                    if (currentDate.get(Calendar.DAY_OF_WEEK) == frequencyDate.get(Calendar.DAY_OF_WEEK)) {
                        handleFrequency(frequency);
                        if (currentDate.get(Calendar.DAY_OF_YEAR) == endFrequencyDate.get(Calendar.DAY_OF_YEAR)) {
                            mDataManager.delete(frequency);
                        }
                    }
                case MONTHLY:
                    if (currentDate.get(Calendar.DAY_OF_MONTH) == frequencyDate.get(Calendar.DAY_OF_MONTH)) {
                        handleFrequency(frequency);
                        if (currentDate.get(Calendar.DAY_OF_YEAR) == endFrequencyDate.get(Calendar.DAY_OF_YEAR)) {
                            mDataManager.delete(frequency);
                        }
                    }
                    break;
                case YEARLY:
                    if (currentDate.get(Calendar.DAY_OF_YEAR) == frequencyDate.get(Calendar.DAY_OF_YEAR)) {
                        handleFrequency(frequency);
                        if (currentDate.get(Calendar.DAY_OF_YEAR) == endFrequencyDate.get(Calendar.DAY_OF_YEAR)) {
                            mDataManager.delete(frequency);
                        }
                    }
                    break;
            }
        }

        AlarmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Frequency frequency) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        String msg = null;
        switch (frequency.getTransactionType()) {
            case INCOME:
                msg = frequency.getTotal() + getString(R.string.addWallet) + frequency.getLabel() + ")";
                break;
            case EXPENSE:
                msg = frequency.getTotal() + getString(R.string.removeWallet) + frequency.getLabel() + ")";
                break;
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(getString(R.string.newTransaction))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void handleFrequency(Frequency frequency) {
        sendNotification(frequency);
        switch (frequency.getTransactionType()) {
            case INCOME:
                Income income = new Income(mDataManager.getNextId(Income.class), frequency.getTotal(), frequency.getLabel(), new Date());
                mDataManager.insert(income);
                break;
            case EXPENSE:
                Expense expense = new Expense(mDataManager.getNextId(Expense.class), frequency.getTotal(), frequency.getLabel(), new Date(), frequency.getCategory());
                mDataManager.insert(expense);
                break;
        }
    }

}
