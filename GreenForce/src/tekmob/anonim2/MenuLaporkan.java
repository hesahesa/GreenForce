package tekmob.anonim2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuLaporkan extends MapActivity {	
	private static final int TAKE_PHOTO_CODE = 2;
	private static final int BROWSE_PHOTO_CODE = 1;
	Bitmap bit;
	String fileName = "";
	String gambar = "";
	String namaMasalah = "";
	String detilMasalah = "";
	String lokasi = "";
	String lat = "";
	String lng = "";
	Uri imagePosition;
	private SharedPreferences mPrefs;
	String namauser;
	static private MapView mapView;
	static private MyLocationOverlay myLocOverlay;
	static MapController mc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_problem);

		mPrefs = getSharedPreferences("userdata", MODE_PRIVATE);
		namauser = mPrefs.getString("nama_user", "dummy");
		mapView = (MapView) findViewById(R.id.petaposisi);
		mc = mapView.getController();

		// button for handle submit
		((Button) findViewById(R.id.laporkanButton))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// get nama masalah from editText
				EditText masalahField = (EditText) findViewById(R.id.masalahField);
				namaMasalah = masalahField.getText().toString();

				// get lokasi from editText
				EditText lokasiField = (EditText) findViewById(R.id.lokasiField);
				lokasi = lokasiField.getText().toString();

				// get detil masalah from editText
				EditText detilMasalahField = (EditText) findViewById(R.id.detilMasalahField);
				detilMasalah = detilMasalahField.getText().toString();

				if (bit!=null && !namaMasalah.equals("") && !lokasi.equals("") && !detilMasalah.equals("")) {
					MenuLaporkan.AsynchUploader as = new MenuLaporkan.AsynchUploader();
					as.execute();
				} else
					alertGagal();

			}
		});

		// button for handle browse
		((Button) findViewById(R.id.browsePhoto))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// To open up a gallery browser
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						1);
			}
		});

		// button for handle take photo
		((Button) findViewById(R.id.takePhoto))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// To open up a gallery browser
				takePhoto();

			}
		});

		//image button for handle gps

		((ImageButton) findViewById(R.id.gpsButton))
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final TextView posisiField = (TextView) findViewById(R.id.posisi);
				posisiField.setText("Sedang mencari lokasi sekarang...");
				myLocOverlay = new MyLocationMod(v.getContext(), mapView, posisiField);
				myLocOverlay.enableMyLocation();
				mapView.getOverlays().add(myLocOverlay);
				myLocOverlay.runOnFirstFix(new Runnable() {
					@Override
					public void run() {
						lat = ""+myLocOverlay.getMyLocation().getLatitudeE6();
						lng = ""+myLocOverlay.getMyLocation().getLongitudeE6();
						MenuLaporkan.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								posisiField.setText(lat+"~"+lng);
							}
						});
						mc.setZoom(17);
						mc.animateTo(myLocOverlay.getMyLocation());
					}
				});
			}
		});

	}

	/* function for take a photo from camera */
	private void takePhoto() {		
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		startActivityForResult(intent, TAKE_PHOTO_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ImageView picture = (ImageView) findViewById(R.id.greenForceImage);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PHOTO_CODE:

				if(data != null)
				{
					bit = (Bitmap) data.getExtras().get("data");
					bit = Bitmap.createScaledBitmap(bit, 200, 200, true);
					picture.setImageBitmap(bit);
				}


				break;
			case BROWSE_PHOTO_CODE:
				imagePosition = data.getData();
				gambar = getRealPathFromURI(imagePosition);
				File tempFile = new File(gambar);
				bit = decodeFile(tempFile);

				//set gui
				picture.setImageBitmap(bit);

				break;
			}
		}
	}

	public String getRealPathFromURI(Uri contentUri) {

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	private Bitmap decodeFile(File f){
		try {
			//decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f),null,o);

			//Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE=200;
			int width_tmp=o.outWidth, height_tmp=o.outHeight;
			int scale=1;
			while(true){
				if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
					break;
				width_tmp/=2;
				height_tmp/=2;
				scale*=2;
			}

			//decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {}
		return null;
	}

	/**
	 * Method showAlert merupakan method ketika terjadi perberitahuan terhadap
	 * user/alert
	 * 
	 * @param void
	 */
	public void alertSukses() {
		new AlertDialog.Builder(this).setTitle("Sukses")
		.setMessage("Laporan Masalah Berhasil Ditambahkan")
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
		.setMessage("Laaporan Masalah Gagal Ditambahkan")
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				
			}
		}).show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class MyLocationMod extends MyLocationOverlay {

		private TextView posisiField;
		
		public MyLocationMod(Context context, MapView mapView, TextView tx) {
			super(context, mapView);
			this.posisiField = tx;
		}
		
		public void onLocationChanged(Location loc) {
			super.onLocationChanged(loc);
			lat = ""+this.getMyLocation().getLatitudeE6();
			lng = ""+this.getMyLocation().getLongitudeE6();
			MenuLaporkan.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					posisiField.setText(lat+"~"+lng);
				}
			});
			mc.setZoom(17);
			mc.animateTo(this.getMyLocation());
		}
	}
	
	private class AsynchUploader extends AsyncTask<Void, Void, String> {

		ProgressDialog dialog;
		
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MenuLaporkan.this, "", "Sedang Meng-upload Data...", true, false);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			//upload
			Log.e("upload", "masuk upload");
			StringBuilder s = new StringBuilder();
			try {
				Bitmap bm = bit;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bm.compress(CompressFormat.JPEG, 100, bos);
				byte[] data = bos.toByteArray();
				final HttpParams hpar = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(hpar, 30000);
				HttpConnectionParams.setSoTimeout(hpar, 60000);
				HttpClient httpClient = new DefaultHttpClient(hpar);
				HttpPost postRequest = new HttpPost(
						"http://gamis61.org/greenforce/push-masalah.php");
				ByteArrayBody bab = new ByteArrayBody(data, "foto.jpg");
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				reqEntity.addPart("foto_full", bab);
				reqEntity.addPart("nama", new StringBody(namaMasalah));
				reqEntity.addPart("detail", new StringBody(detilMasalah));
				reqEntity.addPart("lokasi", new StringBody(lokasi));
				reqEntity.addPart("pelapor", new StringBody(namauser));
				reqEntity.addPart("lat", new StringBody(lat));
				reqEntity.addPart("lng", new StringBody(lng));
				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);
				BufferedReader reader = new BufferedReader(new InputStreamReader(
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
			dialog.cancel();
			if(hasil==null) {
				alertGagal();
				return;
			}
			alertSukses();
			ImageView picture = (ImageView) findViewById(R.id.greenForceImage);
			picture.setImageResource(R.drawable.no_image_icon);
			EditText masalahField = (EditText) findViewById(R.id.masalahField);
			masalahField.setText("");
			EditText lokasiField = (EditText) findViewById(R.id.lokasiField);
			lokasiField.setText("");
			EditText detilMasalahField = (EditText) findViewById(R.id.detilMasalahField);
			detilMasalahField.setText("");
			bit = null;
			namaMasalah = "";
			lokasi = "";
			detilMasalah = "";			
		}
		
	}
}