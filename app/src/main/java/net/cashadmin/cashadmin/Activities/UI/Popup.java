package net.cashadmin.cashadmin.Activities.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.cashadmin.cashadmin.R;

public class Popup {

    public static Dialog popInfo(Context context, View view) {
        Dialog dialog = new Dialog(context);
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View layout = inflater.inflate(view, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setWindowAnimations(R.style.DialogAnimationBottomUp);
        WindowManager.LayoutParams lp = window.getAttributes();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        dialog.getWindow().setDimAmount(0.5f);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }

    public static Toast toast(Context context, String text) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout,null);
        TextView textView = (TextView) layout.findViewById(R.id.textToast);
        textView.setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        return toast;
    }

    public static Toast toast(Context context, String text, int duration) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_layout,null);
        TextView textView = (TextView) layout.findViewById(R.id.textToast);
        textView.setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(duration);
        toast.setView(layout);

        return toast;
    }

    public static void infoSnackBar(String text) {

    }

    public static void infoSnackBar(String text, String button, Color backgroundColor, Color textColor, Color buttonColor) {

    }

    public static void infoAlert(String title, String text) {

    }

    public static void infoAlert(String title, String text, String button1, String button2) {

    }

    public static void warningSnackBar(String text) {

    }

    public static void warningSnackBar(String text, String button, Color backgroundColor, Color textColor, Color buttonColor) {

    }

    public static void warningAlert(String title, String text) {

    }

    public static void warningAlert(String title, String text, String button1, String button2) {

    }

    public static void errorSnackBar(String text) {

    }

    public static void errorSnackBar(String text, String button, Color backgroundColor, Color textColor, Color buttonColor) {

    }

    public static void errorAlert(String title, String text) {

    }

    public static void errorAlert(String title, String text, String button1, String button2) {

    }


}
