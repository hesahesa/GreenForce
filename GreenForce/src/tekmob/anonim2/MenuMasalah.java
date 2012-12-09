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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MenuMasalah extends Activity {
	// All static variables
	public static final String ALL_MASALAH_URL = "http://gamis61.org/greenforce/pull-daftar.php";
	private ListView list;
    private LazyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		list=(ListView)findViewById(R.id.list);
		new DataLoader().execute();		
	}
	
	private class DataLoader extends AsyncTask<String, Void, ModelAllMasalah> {

		@Override
		protected ModelAllMasalah doInBackground(String... params) {			
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
				return null;
			}
			return tesGson;
		}
		
		protected void onPostExecute(ModelAllMasalah tesGson) {	
			if(tesGson==null) {
				Toast.makeText(getApplicationContext(), "Gagal Mengambil Informasi Masalah, Silakan Mencoba Lagi", Toast.LENGTH_LONG).show();
				return;
			}
			ArrayList<HashMap<String, String>> listMasalah = new ArrayList<HashMap<String, String>>();
			for(ModelMasalahMini mm : tesGson.getAllMasalah()) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", mm.getId());
				map.put("lokasi", mm.getLokasi());
				map.put("nama", mm.getNama());
				map.put("pelapor", mm.getPelapor());
				listMasalah.add(map);
			}
			
			// Getting adapter by passing xml data ArrayList
	        adapter=new LazyAdapter(MenuMasalah.this, listMasalah);        
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
}