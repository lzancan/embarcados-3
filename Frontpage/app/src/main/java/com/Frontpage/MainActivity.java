package com.Frontpage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Frontpage.model.Item;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends Activity {

    private static final int RESULT_SETTINGS = 1;
    /**
     * Called when the activity is first created.
     */
    public Button botaoinformar;
    public Button botaosetarsites;
    public TextView textviewcabecalho;
    private String ImageURL;
    public ArrayList<Item> arrayitemlist = new ArrayList<Item>();
    //public SettingsActivity settings;
    //public String URL = "http://g1.globo.com/dynamo/brasil/rss2.xml";
    public String URL = "http://g1.globo.com/dynamo/rss2.xml";
    public int maxQuant = 40;
    private ListView listView=null;

    SharedPreferences sharedPrefs;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        //pega a instancia do textviewcabecalho
        textviewcabecalho = (TextView) findViewById(R.id.TextView01);
        textviewcabecalho.setText("Notícias");

        //click da listview
        listView = (ListView) findViewById(R.id.ListView01);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Item item = arrayitemlist.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(item.getUrl()));

                startActivity(intent);
            }
        });

        // abre a janela das notícias
        check_url_numeros();
        OpenNews(URL, listView, maxQuant);



    }

    // abre rss de notícias
    @TargetApi(9)
    public void OpenNews(String Url, ListView listView, int maxQuant) {

        try {

            // document builder (parser)
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(Url);

            NodeList Source = doc.getElementsByTagName("title"); // nome da fonte
            String SourceName = Source.item(0).getChildNodes().item(0).getNodeValue();

            NodeList listItem = doc.getElementsByTagName("item"); // notícias

            String[] arrayTitles = new String[maxQuant];
            String[] arrayDetails = new String[maxQuant];
            String[] arrayImages = new String[maxQuant];

            if (listItem.getLength() > 0)
                arrayitemlist.clear();

            listView.setBackgroundColor(Color.LTGRAY);
            // System.out.println(maxQuant);
            //System.out.println(listItem.getLength());
            String data_imagem="";
            String details="";
            String title="";
            String link="";
            for (int x = 0; x <maxQuant; x++) {
                //titulo
                title = listItem.item(x).getChildNodes().item(1).getChildNodes().item(0).getNodeValue(); // era item(0)
                //System.out.println("title: " + title);
                //link
                link = listItem.item(x).getChildNodes().item(3).getChildNodes().item(0).getNodeValue(); // era item(1)
                //System.out.println("link: " + link);



                data_imagem = listItem.item(x).getChildNodes().item(7).getFirstChild().getNodeValue();

                    if (listItem.item(x).getChildNodes().item(7).getChildNodes().getLength()>1) { // TEM FILHOS
                        //System.out.println ("tem filhos");
                        // imagem
                        ImageURL = listItem.item(x).getChildNodes().item(7).getChildNodes().item(1).getNodeValue();
                        // detalhes
                        details = listItem.item(x).getChildNodes().item(7).getChildNodes().item(2).getNodeValue(); // era listItem.item(x).getChildNodes().item(2).getChildNodes().item(0).getNodeValue()
                        //System.out.println("details: " + details);
                    } else { // NAO TEM FILHO
                        //System.out.println ("nao tem filhos");
                        ImageURL = "";
                        details = data_imagem;
                    }



                if (ImageURL!="") {
                    // trata para pegar só a description
                    // pega imagem também
                    ImageURL = ImageURL.substring(ImageURL.lastIndexOf("src=\"") + 5);
                    ImageURL = ImageURL.split("\"")[0];
                }
                //System.out.println("imagem: " + ImageURL);
                Item item = new Item();

                item.setTitle(title);
                item.setDetails(details);
                item.setUrl(link);
                item.setImages(ImageURL);

                arrayTitles[x] = item.getTitle();
                arrayDetails[x] = item.getDetails();
                arrayImages[x] = item.getImages();

                arrayitemlist.add(item);
            }



            listView.setAdapter(
                    new LazyAdapter(this, arrayTitles, arrayDetails, arrayImages)
            );

            textviewcabecalho.setText("Notícias de " + SourceName.replaceAll(">",""));
            if (!textviewcabecalho.getText().toString().contains("Notícias"))
                textviewcabecalho.setText("Link inválido! Mantendo do último válido");
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                showUserSettings();
                break;


        }

    }

    private void showUserSettings() {


        Editor editor = sharedPrefs.edit();

        StringBuilder builder = new StringBuilder();

        TextView settingsTextView = (TextView) findViewById(R.id.TextView01);

        settingsTextView.setText(builder.toString());


        check_url_numeros();
    }
        private void check_url_numeros (){

            if (sharedPrefs.getString("num", null) != null)
                maxQuant = Integer.parseInt(sharedPrefs.getString("num", null));
            if (maxQuant < 0)
                maxQuant = 0;
            if (maxQuant > 40)
                maxQuant = 40;
        // Assunto

        if(sharedPrefs.getBoolean("checkbox_preference_1",true))
            URL=getString(R.string.link1);
        if(sharedPrefs.getBoolean("checkbox_preference_2",true))
            URL=getString(R.string.link2);
        if(sharedPrefs.getBoolean("checkbox_preference_3",true))
            URL=getString(R.string.link3);
        if(sharedPrefs.getBoolean("checkbox_preference_4",true))
            URL=getString(R.string.link4);
        if(sharedPrefs.getBoolean("checkbox_preference_5",true))
            URL=getString(R.string.link5);
        if(sharedPrefs.getBoolean("checkbox_preference_6",true))
            URL=getString(R.string.link6);
        if(sharedPrefs.getBoolean("checkbox_preference_6",true))
            URL=getString(R.string.link6);
        if(sharedPrefs.getBoolean("checkbox_preference_7",true))
            URL=getString(R.string.link7);
        if(sharedPrefs.getBoolean("checkbox_preference_8",true))
            URL=getString(R.string.link8);
        if(sharedPrefs.getBoolean("checkbox_preference_9",true))
            URL=getString(R.string.link9);
        if(sharedPrefs.getBoolean("checkbox_preference_10",true))
            URL=getString(R.string.link10);
        if(sharedPrefs.getBoolean("checkbox_preference_11",true))
            URL=getString(R.string.link11);
        if(sharedPrefs.getBoolean("checkbox_preference_12",true))
            URL=getString(R.string.link12);
        if(sharedPrefs.getBoolean("checkbox_preference_13",true))
            URL=getString(R.string.link13);
        if(sharedPrefs.getBoolean("checkbox_preference_14",true))
            URL=getString(R.string.link14);
        if(sharedPrefs.getBoolean("checkbox_preference_15",true))
            URL=getString(R.string.link15);
        if(sharedPrefs.getBoolean("checkbox_preference_16",true))
            URL=getString(R.string.link16);

        // Região

        if(sharedPrefs.getBoolean("checkbox_preference_regiao_1",true))
            URL=getString(R.string.link_regiao1);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_2",true))
            URL=getString(R.string.link_regiao2);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_3",true))
            URL=getString(R.string.link_regiao3);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_4",true))
            URL=getString(R.string.link_regiao4);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_5",true))
            URL=getString(R.string.link_regiao5);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_6",true))
            URL=getString(R.string.link_regiao6);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_7",true))
            URL=getString(R.string.link_regiao7);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_8",true))
            URL=getString(R.string.link_regiao8);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_9",true))
            URL=getString(R.string.link_regiao9);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_10",true))
            URL=getString(R.string.link_regiao10);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_11",true))
            URL=getString(R.string.link_regiao11);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_12",true))
            URL=getString(R.string.link_regiao12);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_13",true))
            URL=getString(R.string.link_regiao13);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_14",true))
            URL=getString(R.string.link_regiao14);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_15",true))
            URL=getString(R.string.link_regiao15);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_16",true))
            URL=getString(R.string.link_regiao16);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_17",true))
            URL=getString(R.string.link_regiao17);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_18",true))
            URL=getString(R.string.link_regiao18);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_19",true))
            URL=getString(R.string.link_regiao19);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_20",true))
            URL=getString(R.string.link_regiao20);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_21",true))
            URL=getString(R.string.link_regiao21);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_22",true))
            URL=getString(R.string.link_regiao22);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_23",true))
            URL=getString(R.string.link_regiao23);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_24",true))
            URL=getString(R.string.link_regiao24);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_25",true))
            URL=getString(R.string.link_regiao25);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_26",true))
            URL=getString(R.string.link_regiao26);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_27",true))
            URL=getString(R.string.link_regiao27);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_28",true))
            URL=getString(R.string.link_regiao28);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_29",true))
            URL=getString(R.string.link_regiao29);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_30",true))
            URL=getString(R.string.link_regiao30);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_31",true))
            URL=getString(R.string.link_regiao31);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_32",true))
            URL=getString(R.string.link_regiao32);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_33",true))
            URL=getString(R.string.link_regiao33);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_34",true))
            URL=getString(R.string.link_regiao34);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_35",true))
            URL=getString(R.string.link_regiao35);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_36",true))
            URL=getString(R.string.link_regiao36);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_37",true))
            URL=getString(R.string.link_regiao37);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_38",true))
            URL=getString(R.string.link_regiao38);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_39",true))
            URL=getString(R.string.link_regiao39);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_40",true))
            URL=getString(R.string.link_regiao40);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_41",true))
            URL=getString(R.string.link_regiao41);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_42",true))
            URL=getString(R.string.link_regiao42);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_43",true))
            URL=getString(R.string.link_regiao43);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_44",true))
            URL=getString(R.string.link_regiao44);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_45",true))
            URL=getString(R.string.link_regiao45);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_46",true))
            URL=getString(R.string.link_regiao46);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_47",true))
            URL=getString(R.string.link_regiao47);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_48",true))
            URL=getString(R.string.link_regiao48);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_49",true))
            URL=getString(R.string.link_regiao49);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_50",true))
            URL=getString(R.string.link_regiao50);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_51",true))
            URL=getString(R.string.link_regiao51);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_52",true))
            URL=getString(R.string.link_regiao52);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_53",true))
            URL=getString(R.string.link_regiao53);
        if(sharedPrefs.getBoolean("checkbox_preference_regiao_54",true))
            URL=getString(R.string.link_regiao54);

        System.out.println(URL);

    }



    @Override
    public void onResume() {
        super.onResume();

        //click da listview
        final ListView listView = (ListView) findViewById(R.id.ListView01);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Item item = arrayitemlist.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(item.getUrl()));

                startActivity(intent);
            }
        });
        OpenNews(URL, listView, maxQuant);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                OpenConfig();
                return true;
            case R.id.action_share:
                OpenShare();
                return true;
            case R.id.action_help:
                OpenHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void OpenConfig () {
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(i, RESULT_SETTINGS);
    }
    private void OpenShare () {
        textviewcabecalho = (TextView) findViewById(R.id.TextView01);
        textviewcabecalho.setText("Selecione a Notícia a ser Compartilhada");
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Item item = arrayitemlist.get(position);
                String sendurl = item.getUrl();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sendurl);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }
    private void OpenHelp (){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Sobre");

        // set dialog message
        alertDialogBuilder
                .setMessage("CREATOR:\nLuciano Zancan\nE-MAIL:\nluciano-zancan@hotmail.com")
                .setCancelable(false)
                /*.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        MainActivity.this.finish();
                    }
                })*/
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}