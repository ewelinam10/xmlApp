package com.ewelinam.xmlapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.button;
import static android.R.attr.canRequestTouchExplorationMode;
import static android.R.attr.text;

public class SecondActivity extends AppCompatActivity {
    private String url ;//"https://erpdemo.assecobs.pl/ErpDemo/homework_html5/controlsTest2.xml";
    private HandleXML obj;
    private RelativeLayout rl ;
    private List<Widget> list;
    private HashMap<String,String> keysMap;
    private String value;
    int widgetId=0;
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        final Intent intent = getIntent();
        url=intent.getStringExtra("link");
        Log.d("tag","\n \n link : " + url);
        obj = new HandleXML(url);
        obj.fetchXML();
        rl = new RelativeLayout(this);

        while (obj.parsingComplete) ;
        list = obj.getWidgetList();
        size = list.size();

        keysMap = new HashMap<>();

        for (int i = 0; i <size; i++) {
            final Widget widget = list.get(i);
            String name = widget.getName();
            String caption = widget.getCaption();
            String key = widget.getKey();
            String defaultValue = widget.getDefaultValue();
            widgetId++;

            if (name.equals("text")) {
                TextView textView = new TextView(getApplicationContext()); //etykieta
                EditText editText = new EditText(getApplicationContext()); //pole txt

                editText.setId(widgetId);//1
                widgetId++;
                textView.setId(widgetId);//2

                textView.setText("" + widget.getCaption());
                editText.setText("" + widget.getDefaultValue());

                textView.setHeight(100);
                textView.setTextSize(15);
                textView.setTextColor(Color.MAGENTA);
                textView.setPadding(20,20,20,20);

                RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

                RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

                textViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                editTextParams.addRule(RelativeLayout.RIGHT_OF,widgetId);
                editTextParams.addRule(RelativeLayout.ALIGN_BOTTOM,widgetId);
                editTextParams.addRule(RelativeLayout.ALIGN_BASELINE,widgetId);

                if (widgetId > 3) {
                    textViewParams.addRule(RelativeLayout.BELOW, (widgetId - 2));
                }

                //dodanie widgetow do layoutu
                rl.addView(textView, textViewParams);
                rl.addView(editText, editTextParams);

                //ustawienie layoutu
                setContentView(rl);
                editText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        if(s.length() != 0) {
                            widget.setDefaultValue(s.toString());
                        }
                    }
                });
            } else if (name.equals("checkbox")) {
                CheckBox checkbox = new CheckBox(this);
                checkbox.setText(""+caption);
                checkbox.setId(widgetId);
                Log.d("widgetId","widgetId CHECKBOX : " + widgetId);
                RelativeLayout.LayoutParams checkBoxParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                checkBoxParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                checkBoxParams.addRule(RelativeLayout.BELOW,(widgetId-1));
                rl.addView(checkbox,checkBoxParams);
                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                  public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                   widget.setDefaultValue(String.valueOf(isChecked));
                   }
             });
            } else if (name.equals("list")) {
                final String [] exampleTab =widget.mapToArray();
                Spinner spinner = new Spinner(this);
                spinner.setId(widgetId);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,exampleTab);
                spinner.setAdapter(adapter);

                RelativeLayout.LayoutParams spinnerBoxParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );

                spinnerBoxParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                spinnerBoxParams.addRule(RelativeLayout.BELOW,(widgetId-1));
                rl.addView(spinner,spinnerBoxParams);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                  @Override
                  public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                      widget.setDefaultValue(exampleTab[position]);
                  }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }

        Button button = new Button(this);
        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        buttonParams.addRule(RelativeLayout.BELOW,(widgetId));
        button.setText("Wygeneruj plik XML");
        button.setBackgroundColor(Color.MAGENTA);
        button.setHintTextColor(Color.BLACK);
        rl.addView(button,buttonParams);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nowyEkran = new Intent(getApplicationContext(),CreateXMLActivity.class);
                HashMap<String,String> hashMap = new HashMap<String, String>();
                for ( int j=0; j < list.size(); j++){
                    Widget widget = list.get(j);
                    Log.d("onClick"," \n Widget parametrs: " + widget.getDefaultValue() + " Key : " + widget.getKey());
                    hashMap.put(widget.getKey(),widget.getDefaultValue());
                }

                nowyEkran.putExtra("mapa",hashMap);
                startActivity(nowyEkran);


                //stworzyc nowa klase ktora bedzie miala za zadanie zapis pliku xml
                //tutaj ja wywolamy
                //a nastepnie zrobimy przeskok do nowej aktywnosci ktora wyswietli napis ze operacja przebiegla pomyslnie
                //wyswietli sciezke do pliku i bedzie zaweirala przycisk powrotu do ekranu poczatkowego
            }
        });
    }


    //STWORZYC METODE KTORA ZWROCI KEYSMAP PPOBBIERZE LISTE STWORZONY WIDGETOW
    // W CIELE METODY IF EDITTEXT TO POBIERZ TRESC JESLI CHCECKBOX TO CZY ZAZNACZONY CZY NIE JESLI LISTA TO TEZ POBIERZ WARTOSC
}