package net.cashadmin.cashadmin.Activities.Functor;

import android.os.Parcelable;

import net.cashadmin.cashadmin.Activities.Exception.IllegalTypeException;
import net.cashadmin.cashadmin.Activities.Model.Transaction;

import java.util.ArrayList;

public abstract class TransactionFunctor implements Parcelable{

    public abstract ArrayList<Transaction> getList() throws IllegalTypeException;
}
