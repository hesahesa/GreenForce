package tekmob.anonim2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import tekmob.anonim2.tools.ImageLoader;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuDetailMasalah extends MapActivity {

	public static final String URL_DETAIL_MASALAH = "http://gamis61.org/greenforce/pull-detail.php?idMasalah=";
	TextView namamasalah;
	TextView lokasi;
	TextView detailmasalah;
	TextView pelapor;
	ImageView large_image;
	MapView mapView;
	Button btn;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		String tester = this.getIntent().getStringExtra("id_masalah");
		this.setContentView(R.layout.detail_problem);
		namamasalah = (TextView)findViewById(R.id.title);
		lokasi = (TextView)findViewById(R.id.lokasi);
		detailmasalah = (TextView)findViewById(R.id.detailMasalah_content);
		pelapor = (TextView)findViewById(R.id.pelapor);
		large_image = (ImageView)findViewById(R.id.greenForceImage);
		mapView = (MapView) findViewById(R.id.petaposisi2);
		btn = (Button)findViewById(R.id.sharebutton);
		btn.setEnabled(false);
		new DataLoader().execute(tester);
	}
	
	@Override
    protected void onPause() {
        overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
        super.onPause();
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	static class PetaOverlay extends ItemizedOverlay {
		
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		
		public PetaOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
			// TODO Auto-generated constructor stub
		}

		public void addOverlay(OverlayItem overlay) {
		    mOverlays.add(overlay);
		    populate();
		}
		
		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}
	}
	
	private class DataLoader extends AsyncTask<String, Void, ModelMasalah> {

		@Override
		protected ModelMasalah doInBackground(String... params) {
			Gson gson = new Gson();
			ModelMasalah tesGson = null;
			try {
				String res = "";
		        URL url = new URL(URL_DETAIL_MASALAH+params[0]);
		        URLConnection urlConnection = url.openConnection();
		        urlConnection.setReadTimeout(10000);
		        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		        String line;

		        while ((line = reader.readLine()) != null) {
		            res += line;
		        }
		        reader.close();
		        tesGson = gson.fromJson(res, ModelMasalah.class);
			} catch (Exception ex) {
				Log.i("coks", ex.toString());
				return null;
			}
			return tesGson;
		}
		
		protected void onPostExecute(ModelMasalah tesGson) {
			if(tesGson==null) {
				Toast.makeText(getApplicationContext(), "Gagal Mengambil Informasi Masalah, Silakan Mencoba Lagi", Toast.LENGTH_LONG).show();
				return;
			}
			namamasalah.setText(tesGson.getNama());	
			lokasi.setText(tesGson.getLokasi());
			detailmasalah.setText(tesGson.getDetail());
			pelapor.setText("Pelapor: "+tesGson.getPelapor());
			ImageLoader imageLoader=new ImageLoader(MenuDetailMasalah.this.getApplicationContext());
			imageLoader.DisplayImage("http://gamis61.org/greenforce/show-image.php?id="+tesGson.getId()+"&isLarge=1", large_image);	
			MapController mc = mapView.getController();
	        mc.setZoom(17);
            mc.animateTo(new GeoPoint(Integer.parseInt(tesGson.getLat()), Integer.parseInt(tesGson.getLng())));
            List<Overlay> mapOverlays = mapView.getOverlays();
            Drawable drawable = MenuDetailMasalah.this.getResources().getDrawable(R.drawable.reddot);
            PetaOverlay itemizedoverlay = new PetaOverlay(drawable);
            GeoPoint point = new GeoPoint(Integer.parseInt(tesGson.getLat()), Integer.parseInt(tesGson.getLng()));
            OverlayItem overlayitem = new OverlayItem(point, "Disini", "Lokasinya disini");
            itemizedoverlay.addOverlay(overlayitem);
            mapOverlays.add(itemizedoverlay);
            
            final String nama = namamasalah.getText().toString();
            final String detail = detailmasalah.getText().toString();
            final String lokasinya = lokasi.getText().toString();
            final String pelapornya = pelapor.getText().toString();
            
            btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent shareIntent = new Intent(Intent.ACTION_SEND);
		            shareIntent.setType("text/plain");
		            shareIntent.putExtra(Intent.EXTRA_TEXT, "GreenForceAndroid -- Masalah: "+nama+", Detail: "+detail+", Lokasi: "+lokasinya+", Pelapor: "+pelapornya);
		            startActivity(Intent.createChooser(shareIntent, "Share..."));
				}
			});
            btn.setEnabled(true);
		}
		
	}
}