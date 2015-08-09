package net.cashadmin.cashadmin.Activities.Model;

import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

public abstract class Entity {

    protected TypeEnum mType;

    public TypeEnum getType(){
        return mType;
    }
}
