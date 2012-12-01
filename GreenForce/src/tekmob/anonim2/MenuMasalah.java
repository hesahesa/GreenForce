package tekmob.anonim2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tekmob.anonim2.tools.LazyAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.google.gson.Gson;

public class MenuMasalah extends Activity {
	// All static variables
	public static final String ALL_MASALAH_URL = "http://gamis61.org/greenforce/pull-daftar.php";
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
		ModelAllMasalah tesGson = null;
		try {
			String res = "";
	        URL url = new URL(ALL_MASALAH_URL);
	        URLConnection urlConnection = url.openConnection();
	        urlConnection.setReadTimeout(10000);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	        String line;

	        while ((line = reader.readLine()) != null) {
	            res += line;
	        }
	        reader.close();
	        
	        tesGson = gson.fromJson(res, ModelAllMasalah.class);

		} catch (Exception ex) {
			Log.i("coks", ex.toString());
		}
		
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
				Intent itc = new Intent().setClass(view.getContext(), MenuDetailMasalah.class);
		    	itc.putExtra("id_masalah", list.getAdapter().getItem(position).toString());
				startActivity(itc);
			}
		});		
	}	
}