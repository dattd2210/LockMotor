package com.lockmotor.Components.network.Realm;

import com.lockmotor.Components.managers.DataManager.RealmString;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import io.realm.RealmList;

/**
 * Created by VietHoa on 25/01/16.
 */
public class RealmConverter implements JsonSerializer<RealmList<RealmString>>, JsonDeserializer<RealmList<RealmString>> {

    @Override
    public JsonElement serialize(RealmList<RealmString> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray ja = new JsonArray();
        for (RealmString tag : src) {
            ja.add(context.serialize(tag.getValue()));
        }
        return ja;
    }

    @Override
    public RealmList<RealmString> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RealmList<RealmString> tags = new RealmList<>();
        JsonArray ja = json.getAsJsonArray();
        for (JsonElement je : ja) {
            String value = context.deserialize(je, String.class);
            RealmString realmString = new RealmString();
            realmString.setValue(value);
            tags.add(realmString);
        }
        return tags;
    }

}