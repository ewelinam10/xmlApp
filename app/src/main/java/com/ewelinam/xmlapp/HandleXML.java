package com.ewelinam.xmlapp;

import android.app.ActionBar;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HandleXML {
    private String text_caption;
    private String text_key;
    private String text_defaultValue;
    private String checkbox_caption;
    private String checkbox_key;
    private String checkbox_defaultValue;
    private String list_caption;
    private String list_key;
    private String list_defaultValue;
    private String item_caption;
    private String item_value;
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    private List<Widget> widgetList;


    public volatile boolean parsingComplete = true;

    public HandleXML(String url){
        this.urlString = url;
    }

    public String getText_caption() {
        return text_caption;
    }

    public String getText_key() {
        return text_key;
    }

    public String getText_defaultValue() {
        return text_defaultValue;
    }

    public String getCheckbox_caption() {
        return checkbox_caption;
    }

    public String getCheckbox_key() {
        return checkbox_key;
    }

    public String getCheckbox_defaultValue() {
        return checkbox_defaultValue;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        int listWidgetId = 0;
        String text = null;
        widgetList = new ArrayList<Widget>();

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                //Log.d("getName()","nazwa znacznika: " + name);
                //Log.d("event","event:" +event);
               // Log.d("getAttributeCount","liczba atrybutów biezacego elementu poczatkowego:"+myParser.getAttributeCount());
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("list")){
                            list_caption = myParser.getAttributeValue(null, "caption");
                            list_key = myParser.getAttributeValue(null, "key");
                            list_defaultValue = myParser.getAttributeValue(null, "defaultValue");
                            HashMap<String,String> map = new HashMap<>();
                            Widget widget = new Widget("list",list_caption,list_key,list_defaultValue,map);
                            Log.d("widget: ","Rodzaj kontrolki:" +widget.getName() + "\n Etykieta kontrolki : " +widget.getCaption()
                                    + "\n Domyslna wartosc : " + widget.getDefaultValue() + "\n Klucz : "+ widget.getKey());
                            widgetList.add(widget);
                            listWidgetId = widgetList.indexOf(widget);
                            Log.d("HANDLE","ID LISTY : " + listWidgetId);
                        }
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if (name.equals("text")) {
                            text_caption = myParser.getAttributeValue(null, "caption");
                            text_key = myParser.getAttributeValue(null, "key");
                            text_defaultValue = myParser.getAttributeValue(null, "defaultValue");
                            Widget widget = new Widget("text",text_caption,text_key,text_defaultValue);
                            widgetList.add(widget);
                            Log.d("widget: ","Rodzaj kontrolki:" +widget.getName() + "\n Etykieta kontrolki : " +widget.getCaption()
                                   + "\n Domyslna wartosc : " + widget.getDefaultValue() + "\n Klucz : "+ widget.getKey());

                        } else if (name.equals("checkbox")) {
                            checkbox_caption = myParser.getAttributeValue(null, "caption");
                            checkbox_key = myParser.getAttributeValue(null, "key");
                            checkbox_defaultValue = myParser.getAttributeValue(null, "defaultValue");
                            Widget widget = new Widget("checkbox",checkbox_caption,checkbox_key,checkbox_defaultValue);
                          Log.d("widget: ","Rodzaj kontrolki:" +widget.getName() + "\n Etykieta kontrolki : " +widget.getCaption()
                                    + "\n Domyslna wartosc : " + widget.getDefaultValue() + "\n Klucz : "+ widget.getKey());
                            widgetList.add(widget);

                        }else if(name.equals("item")){
                            Widget widget =getWidgetList().get(listWidgetId);
                            HashMap<String,String> map = widget.getMap();
                            item_caption = myParser.getAttributeValue(null,"caprion");
                            item_value = myParser.getAttributeValue(null,"value");
                            Log.d("Handle"," \n ListWidget id : " + listWidgetId + item_caption + item_value);
                            map.put(item_caption,item_value);
                        }
                        else {
                        }
                        break;
                }
                event = myParser.next();
                //tu dopisywac do listy obiektow


            }
            parsingComplete = false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Widget> getWidgetList() {
        return widgetList;
    }


    public void fetchXML(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream stream = conn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}



