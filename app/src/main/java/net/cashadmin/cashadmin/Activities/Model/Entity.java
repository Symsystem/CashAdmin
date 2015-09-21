package net.cashadmin.cashadmin.Activities.Model;

import net.cashadmin.cashadmin.Activities.Model.Enum.TypeEnum;

import java.io.Serializable;

public abstract class Entity implements Serializable{

    protected TypeEnum mType;

    public TypeEnum getType(){
        return mType;
    }
}
