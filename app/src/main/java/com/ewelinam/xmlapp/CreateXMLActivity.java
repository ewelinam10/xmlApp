package com.ewelinam.xmlapp;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class CreateXMLActivity extends AppCompatActivity {
    private TextView tv_tresc;
    private Button bt_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_xml);
            Intent intent = getIntent();
            HashMap<String, String> intentHashMap = (HashMap<String, String>) intent.getSerializableExtra("mapa");
       Log.d("map","" + intentHashMap.size());


        tv_tresc = (TextView) findViewById(R.id.tresc);
        bt_next = (Button) findViewById(R.id.next);


        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();


            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("output");
            doc.appendChild(rootElement);

            for (String key : intentHashMap.keySet()) {
                Log.d("Klucz", "\n Klucz " + key);

                Element keyValue = doc.createElement(key);
                keyValue.appendChild(doc.createTextNode(intentHashMap.get(key)));
                rootElement.appendChild(keyValue);

            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result1 = new StreamResult(writer);
            transformer.transform(source, result1);

            // do tego momentu na pewno wszystko dobrze, dom zostaje stworzony poprawnie mozna sprawdzic writer to string i wyswietlic logiem
            /*String state;
            state = Environment.getExternalStorageState();
            if(Environment.MEDIA_MOUNTED.equals(state)){
                File Root = Environment.getExternalStorageDirectory();
                File Dir = new File(Root.getAbsolutePath()+"/XMLAppFile");
                     if(!Dir.exists()) {
                         Dir.mkdir();
                }
                File file = new File(Dir,"newXMLFile.txt");
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
            }
            else{
                Toast.makeText(getApplicationContext(),"External storage not found",Toast.LENGTH_LONG).show();
            }*/

            // return XML string
            String tekst = writer.toString();
            Log.d("tag"," \n \n XML FILE : " + tekst);

            tv_tresc.setText(tekst);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToMain(View view) {
        Intent nowyEkran = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(nowyEkran);
    }
}