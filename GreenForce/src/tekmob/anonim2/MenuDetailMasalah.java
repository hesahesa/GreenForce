package tekmob.anonim2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuDetailMasalah extends MapActivity {

	public static final String URL_DETAIL_MASALAH = "http://gamis61.org/greenforce/pull-detail.php?idMasalah=";
	TextView namamasalah;
	TextView lokasi;
	TextView detailmasalah;
	TextView pelapor;
	TextView voteVal;
	ImageView large_image;
	MapView mapView;
	Button btn;
	String id_masalah;
	String vote_type;
	String nama_voter;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		String tester = this.getIntent().getStringExtra("id_masalah");
		this.setContentView(R.layout.detail_problem);
		namamasalah = (TextView) findViewById(R.id.title);
		lokasi = (TextView) findViewById(R.id.lokasi);
		detailmasalah = (TextView) findViewById(R.id.detailMasalah_content);
		pelapor = (TextView) findViewById(R.id.pelapor);
		voteVal = (TextView) findViewById(R.id.vote_value);
		large_image = (ImageView) findViewById(R.id.greenForceImage);
		mapView = (MapView) findViewById(R.id.petaposisi2);
		btn = (Button) findViewById(R.id.sharebutton);
		btn.setEnabled(false);
		new DataLoader().execute(tester);

		id_masalah = tester;

		// image button for handling voteUp
		((ImageButton) findViewById(R.id.voteUpButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						vote_type = "1";
						
						MenuDetailMasalah.AsynchUploader as = new MenuDetailMasalah.AsynchUploader();
						as.execute();
					}
				});

		// image button for handling voteDown
		((ImageButton) findViewById(R.id.voteDownButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						vote_type = "-1";

						MenuDetailMasalah.AsynchUploader as = new MenuDetailMasalah.AsynchUploader();
						as.execute();
					}
				});
	}

	/**
	 * Method showAlert merupakan method ketika terjadi perberitahuan terhadap
	 * user/alert
	 * 
	 * @param void
	 */
	public void alertSukses() {
		new AlertDialog.Builder(this).setTitle("Sukses")
		.setMessage("Vote berhasil ditambahkan")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {

			}
		}).show();

	}

	/**
	 * Method showAlert merupakan method ketika terjadi perberitahuan terhadap
	 * user/alert
	 * 
	 * @param void
	 */
	public void alertGagal() {
		new AlertDialog.Builder(this).setTitle("Gagal")
		.setMessage("Vote gagal ditambahkan")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {

			}
		}).show();
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
				URL url = new URL(URL_DETAIL_MASALAH + params[0]);
				URLConnection urlConnection = url.openConnection();
				urlConnection.setReadTimeout(10000);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlConnection.getInputStream()));
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
			if (tesGson == null) {
				Toast.makeText(
						getApplicationContext(),
						"Gagal Mengambil Informasi Masalah, Silakan Mencoba Lagi",
						Toast.LENGTH_LONG).show();
				return;
			}
			SharedPreferences mPrefs = getSharedPreferences("userdata", MODE_PRIVATE);
			nama_voter = mPrefs.getString("nama_user", "dummy");
			
			namamasalah.setText(tesGson.getNama());
			lokasi.setText(tesGson.getLokasi());
			detailmasalah.setText(tesGson.getDetail());
			pelapor.setText("Pelapor: " + tesGson.getPelapor());
			voteVal.setText("Jumlah voteUp: " +tesGson.getVoteVal());
			ImageLoader imageLoader = new ImageLoader(
					MenuDetailMasalah.this.getApplicationContext());
			imageLoader.DisplayImage(
					"http://gamis61.org/greenforce/show-image.php?id="
							+ tesGson.getId() + "&isLarge=1", large_image);
			MapController mc = mapView.getController();
			mc.setZoom(17);
			mc.animateTo(new GeoPoint(Integer.parseInt(tesGson.getLat()),
					Integer.parseInt(tesGson.getLng())));
			List<Overlay> mapOverlays = mapView.getOverlays();
			Drawable drawable = MenuDetailMasalah.this.getResources()
					.getDrawable(R.drawable.reddot);
			PetaOverlay itemizedoverlay = new PetaOverlay(drawable);
			GeoPoint point = new GeoPoint(Integer.parseInt(tesGson.getLat()),
					Integer.parseInt(tesGson.getLng()));
			OverlayItem overlayitem = new OverlayItem(point, "Disini",
					"Lokasinya disini");
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
					shareIntent.putExtra(Intent.EXTRA_TEXT,
							"GreenForceAndroid -- Masalah: " + nama
									+ ", Detail: " + detail + ", Lokasi: "
									+ lokasinya + ", Pelapor: " + pelapornya);
					startActivity(Intent.createChooser(shareIntent, "Share..."));
				}
			});
			btn.setEnabled(true);
		}

	}

	private class AsynchUploader extends AsyncTask<Void, Void, String> {

		ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(MenuDetailMasalah.this, "",
					"Sedang Mengirim Vote...", true, false);
		}

		@Override
		protected String doInBackground(Void... params) {
			// upload
			Log.e("upload", "masuk upload");
			Log.e("id", id_masalah);
			Log.e("pelapor", nama_voter);
			Log.e("voteType", vote_type);
			StringBuilder s = new StringBuilder();
			try {
				final HttpParams hpar = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(hpar, 30000);
				HttpConnectionParams.setSoTimeout(hpar, 60000);
				HttpClient httpClient = new DefaultHttpClient(hpar);
				HttpPost postRequest = new HttpPost(
						"http://gamis61.org/greenforce/push-vote.php");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				reqEntity.addPart("id", new StringBody(id_masalah));
				reqEntity.addPart("voteType", new StringBody(vote_type));
				reqEntity.addPart("pelapor", new StringBody(nama_voter));
				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}
				Log.e("upload", "end upload");
			} catch (Exception e) {
				Log.e("blablabla", e.getMessage());
				return null;
			}
			return s.toString();
		}
		
		protected void onPostExecute(String hasil) {
			Log.e("hasil return", hasil);
			dialog.cancel();
			if(hasil==null || hasil.equals("1")) {
				alertGagal();
				return;
			}
			alertSukses();
		}
	}
}