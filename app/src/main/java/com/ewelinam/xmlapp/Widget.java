package com.ewelinam.xmlapp;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wmysiak on 2017-06-14.
 */

public class Widget {
    private String name; //rodzaj kontrolki
    private String caption; //etykieta kontrolki
    private String key; //nazwa klucza pod jakas wartosc z kontrolki zostanie zpaisana do pliku wyjsciowego
    private String defaultValue; // domyslna wartosc kontrolki (jezeli zadna nie zostanie wpisana)
    private HashMap map; // jezeli name = list, to jest to zbior elemntow listy
    //<item caption , item value>

    public Widget(String name, String caption, String key, String defaultValue) {
        this.name = name;
        this.caption = caption;
        this.key = key;
        this.defaultValue = defaultValue;
    }
//konstruktor jezeli name = list
    public Widget(String name, String caption, String key, String defaultValue, HashMap mapa) {
        this.name = name;
        this.caption = caption;
        this.key = key;
        this.defaultValue = defaultValue;
        this.map = mapa;
    }

    public String getName() {
        return name;
    }

    public String getCaption() {
        return caption;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public HashMap getMap() {
        return map;
    }

    public String[] mapToArray(){
        int size = map.size();
        String[] tab = new String[size];
        int i = 0;

        for (Object values: map.values()) {
            tab[i]= (String)values;
            i++;
        }

        return tab;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}

