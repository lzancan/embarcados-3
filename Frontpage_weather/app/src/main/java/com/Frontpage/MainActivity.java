package com.Frontpage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.Frontpage.Item;

public class MainActivity extends AppCompatActivity {

	/** Called when the activity is first created. */
	public Button lerConteudo;
	public TextView cabecalho;
	public ArrayList<Item> itemList = new ArrayList<Item>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
        //setContentView(R.layout.rss_layout);


        // inicio rss **************************************
        /*
        //pegando instancia do cabecalho
        //cabecalho = (TextView) findViewById(R.id.TextView01);

        //click da listview
        final ListView listView = (ListView) findViewById(R.id.ListView01);
        listView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {

                Item item = itemList.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse(item.getUrl()));

                startActivity(intent);
            }
        });

        //Button Click
        //lerConteudo = (Button) findViewById(R.id.Button01);
        //lerConteudo.setOnClickListener(new OnClickListener(){

        //@Override
        //public void onClick(View v) {
        try {

            String url = "http://g1.globo.com/dynamo/brasil/rss2.xml";
            //http://www.ufrgs.br/relinter/portugues/menugeral/noticias/RSS
            //http://g1.globo.com/dynamo/brasil/rss2.xml

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url);
            //lerConteudo.setText("botaoloco");
            NodeList listItem = doc.getElementsByTagName("item");

            String[] arrayTitles = new String[listItem.getLength()];

            for(int x = 0; x < listItem.getLength(); x++){
                //titulo
                String title = listItem.item(x).getChildNodes().item(0).getChildNodes().item(0).getNodeValue();

                //link
                String link = listItem.item(x).getChildNodes().item(1).getChildNodes().item(0).getNodeValue();

                Item item = new Item();

                item.setTitle(title);
                item.setUrl(link);

                arrayTitles[x] = item.getTitle();

                itemList.add(item);
            }


            listView.setAdapter(
                    new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_list_item_1, arrayTitles)
            );

            //cabecalho.setText("Leitor de conteudo RSS - Finalizado");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG);
        }

        */
        // fim rss **************************************

        // inicio weather ***************************************
	    ///*
	    if (savedInstanceState == null) {
	        getSupportFragmentManager().beginTransaction()
	                .add(R.id.container, new WeatherFragment())
	                .commit();
	    }
        //*/
        // fim weather ***************************************

		
		
	}
///*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();
		//if (id == R.id.action_settings) {
		//	return true;
		//}
		//return super.onOptionsItemSelected(item);
	    if(item.getItemId() == R.id.change_city){
	        showInputDialog();
	    }
	    return false;
		
	}

	
	private void showInputDialog(){
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Trocar Cidade");
	    final EditText input = new EditText(this);
	    input.setInputType(InputType.TYPE_CLASS_TEXT);
	    builder.setView(input);
	    builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            changeCity(input.getText().toString());
	        }
	    });
	    builder.show();
	}
	 
	public void changeCity(String city){
	    WeatherFragment wf = (WeatherFragment)getSupportFragmentManager()
	                            .findFragmentById(R.id.container);
	    wf.changeCity(city);
	    new CityPreference(this).setCity(city);
	}
    //*/
	
}
