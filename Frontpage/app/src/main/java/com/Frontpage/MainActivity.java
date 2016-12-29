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
    public String URL = "http://g1.globo.com/dynamo/brasil/rss2.xml";
    public int maxQuant = 40;
    private ListView listView=null;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
            String data_imagem="";
            String details="";
            String title="";
            String link="";
            for (int x = 0; x <maxQuant; x++) {
                //titulo
                title = listItem.item(x).getChildNodes().item(1).getChildNodes().item(0).getNodeValue(); // era item(0)
                System.out.println("\ntitle: " + title);
                //link
                link = listItem.item(x).getChildNodes().item(3).getChildNodes().item(0).getNodeValue(); // era item(1)
                //System.out.println("link: " + link);
                System.out.println ("chegou aqui0 "+x);
                System.out.println(listItem.item(x).getChildNodes().item(7).getFirstChild().getNodeValue());
                data_imagem=listItem.item(x).getChildNodes().item(7).getFirstChild().getNodeValue();


                if(!data_imagem.contains(".")){
                    System.out.println ("chegou aqui00 "+x);
                    // imagem
                    ImageURL=listItem.item(x).getChildNodes().item(7).getChildNodes().item(1).getNodeValue();
                    // detalhes
                    details = listItem.item(x).getChildNodes().item(7).getChildNodes().item(2).getNodeValue(); // era listItem.item(x).getChildNodes().item(2).getChildNodes().item(0).getNodeValue()
                    //System.out.println("details: " + details);

                }
                else{
                    System.out.println ("chegou aqui000 "+x);
                    ImageURL="";
                    details = data_imagem;
                }
                System.out.println ("chegou aqui0000 "+x);


                if (ImageURL.contains("http")) {
                    // trata para pegar só a description
                    // pega imagem também
                    ImageURL = ImageURL.substring(ImageURL.lastIndexOf("src=\"") + 5);
                    ImageURL = ImageURL.split("\"")[0];
                }
                System.out.println("imagem: " + ImageURL);
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

            textviewcabecalho.setText("Notícias de " + SourceName);
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
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        Editor editor = sharedPrefs.edit();

        StringBuilder builder = new StringBuilder();

        TextView settingsTextView = (TextView) findViewById(R.id.TextView01);

        settingsTextView.setText(builder.toString());

        if (sharedPrefs.getString("pref", null) != null)
            URL = findURL(sharedPrefs.getString("pref", null));
        if (sharedPrefs.getString("num", null) != null)
            maxQuant = Integer.parseInt(sharedPrefs.getString("num", null));
        if (maxQuant < 0)
            maxQuant = 0;
        if (maxQuant > 40)
            maxQuant = 40;
        System.out.println(maxQuant);
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

    @TargetApi(8)
    private String findURL(String Url) {
        if (Url.toLowerCase().contains("http"))
            return Url;
        return "http://g1.globo.com/dynamo/".concat(Url).concat("/rss2.xml\"");
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