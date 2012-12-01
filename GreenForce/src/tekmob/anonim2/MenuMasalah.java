package tekmob.anonim2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tekmob.anonim2.tools.LazyAdapter;
import tekmob.anonim2.tools.XMLParser;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.google.gson.Gson;

public class MenuMasalah extends Activity {
	// All static variables
	public static final String ALL_MASALAH_URL = "http://api.androidhive.info/music/music.xml";
	/*// XML node keys
	public static final String KEY_SONG = "song"; // parent node
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_THUMB_URL = "thumb_url";*/
	
	ListView list;
    LazyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		

		ArrayList<HashMap<String, String>> listMasalah = new ArrayList<HashMap<String, String>>();

		/**
		 * WORKAROUND!
		 * ganti method XML pake method JSON
		 * seharusnya nggak jauh beda
		 */
		Gson gson = new Gson();
		
		//  driver
		ModelAllMasalah driverMasalah = new ModelAllMasalah();
		List<ModelMasalahMini> driverList = new ArrayList<ModelMasalahMini>();
		for(int a=0;a<=1;a++) {
			ModelMasalahMini mm = new ModelMasalahMini();
			mm.setId(""+a);
			mm.setNama("Masalah ke-"+a);
			mm.setLokasi("Lokasi masalah ke-"+a);
			mm.setPelapor("Pelapor dummy"+a);
			driverList.add(mm);			
		}
		driverMasalah.setAllMasalah(driverList);
		String buff = gson.toJson(driverMasalah);
		Log.e("tes json", buff);
		// end driver
		
		ModelAllMasalah tesGson = gson.fromJson(buff, ModelAllMasalah.class);
		
		for(ModelMasalahMini mm : tesGson.getAllMasalah()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", mm.getId());
			map.put("lokasi", mm.getLokasi());
			map.put("nama", mm.getNama());
			map.put("pelapor", mm.getPelapor());
			listMasalah.add(map);
		}
		

		list=(ListView)findViewById(R.id.list);
		
		// Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, listMasalah);        
        list.setAdapter(adapter);
        

        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
							

			}
		});		
	}	
}