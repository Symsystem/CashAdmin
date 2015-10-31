package net.cashadmin.cashadmin.Activities.Model.Enum;

import android.content.Intent;

public enum HistoricEntryEnum {
    All, ByCategory;

    private static final String name = HistoricEntryEnum.class.getName();

    public void attachTo(Intent intent) {
        intent.putExtra(name, ordinal());
    }

    public static HistoricEntryEnum detachFrom(Intent intent) {
        if(!intent.hasExtra(name)) throw new IllegalStateException();
        return values()[intent.getIntExtra(name, -1)];
    }
}