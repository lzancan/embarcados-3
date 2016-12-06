package com.Frontpage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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

import com.Frontpage.SettingsActivity;

public class MainActivity extends Activity {
	private static final int RESULT_SETTINGS = 1;
    /** Called when the activity is first created. */
	public Button botaoinformar;
	public Button botaosetarsites;
	public TextView textviewcabecalho;
	public ArrayList<Item> arrayitemlist = new ArrayList<Item>();
	//public SettingsActivity settings;
    public String URL = "http://g1.globo.com/dynamo/rs/rio-grande-do-sul/rss2.xml";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //pega a instancia do textviewcabecalho
        textviewcabecalho = (TextView) findViewById(R.id.TextView01);
        textviewcabecalho.setText("Notícias");

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

        // abre a janela das notícias
     /*   OpenNews(URL, listView);
        URL = "http://g1.globo.com/dynamo/economia/rss2.xml";*/
        OpenNews(URL, listView);

        botaosetarsites = (Button) findViewById(R.id.Button02);
        botaosetarsites.setText("Configurar Site");
		botaosetarsites.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivityForResult(i, RESULT_SETTINGS);
			}
		});

        //pega id do botao
        botaoinformar = (Button) findViewById(R.id.Button01);
        //muda texto do botao
        botaoinformar.setText("Informar Contato");
        //clicklistener do botao
        botaoinformar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoinformar.setText("Selecione Link");
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
        });

    }
    @TargetApi(9)
    public void OpenNews(String Url,ListView listView) {
        try {
            // document builder (parser)
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(Url);

            NodeList Source = doc.getElementsByTagName("title"); // nome da fonte
            String SourceName = Source.item(0).getChildNodes().item(0).getNodeValue();

            NodeList listItem = doc.getElementsByTagName("item"); // notícias

            String[] arrayTitles = new String[listItem.getLength()];

            if (listItem.getLength() > 0)
                arrayitemlist.clear();

            for (int x = 0; x < listItem.getLength(); x++) {
                //titulo
                String title = "" + Integer.toString(x+1).concat(". ").concat(listItem.item(x).getChildNodes().item(0).getChildNodes().item(0).getNodeValue());

                //link
                String link = listItem.item(x).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();

                Item item = new Item();

                item.setTitle(title);
                item.setUrl(link);

                arrayTitles[x] = item.getTitle();

                arrayitemlist.add(item);
            }


            listView.setAdapter(
                    new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_list_item_1, arrayTitles)
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

	private void showUserSettings()
	{
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

        Editor editor = sharedPrefs.edit();

		StringBuilder builder = new StringBuilder();

		TextView settingsTextView = (TextView) findViewById(R.id.TextView01);

		settingsTextView.setText(builder.toString());

        if (sharedPrefs.getString("pref", null) != null)
            URL = findURL(sharedPrefs.getString("pref", null));

	}



	@Override
	public void onResume() {
		super.onResume();

		//click da listview
		final ListView listView = (ListView) findViewById(R.id.ListView01);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {

				Item item = arrayitemlist.get(position);

				Intent intent = new Intent(Intent.ACTION_VIEW);

				intent.setData(Uri.parse(item.getUrl()));

				startActivity(intent);
			}
		});
		botaoinformar.setText("Informar Contato");
        OpenNews(URL, listView);
	}
@TargetApi(8)
    private String findURL(String Url){
    if(!Url.toLowerCase().contains("rss")){
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(Url);

            NodeList Source = doc.getElementsByTagName("rss"); // nome da fonte
            return Source.item(0).getTextContent();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG);
        }}
        return Url;
    }
}