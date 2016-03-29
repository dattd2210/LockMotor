package com.lockmotor.Components.managers.DataManager;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by VietHoa on 25/01/16.
 */
public class RealmString extends RealmObject implements Serializable {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
