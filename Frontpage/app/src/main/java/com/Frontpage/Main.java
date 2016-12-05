package com.Frontpage;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.Frontpage.model.Item;

public class Main extends Activity {
    /** Called when the activity is first created. */
	public Button lerConteudo;
	public TextView cabecalho;
	public ArrayList<Item> itemList = new ArrayList<Item>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //pegando instancia do cabecalho
        cabecalho = (TextView) findViewById(R.id.TextView01);
		cabecalho.setText("Leitor de Conteúdo");
        
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
        lerConteudo = (Button) findViewById(R.id.Button01);
		lerConteudo.setText("Informar Contato");
		lerConteudo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				lerConteudo.setText("Selecione Link");
				listView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
											long id) {

						Item item = itemList.get(position);
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
				
			//}
       // });
        
        
        
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

				Item item = itemList.get(position);

				Intent intent = new Intent(Intent.ACTION_VIEW);

				intent.setData(Uri.parse(item.getUrl()));

				startActivity(intent);
			}
		});
		lerConteudo.setText("Informar Contato");
	}
}