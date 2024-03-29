package tekmob.anonim2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.android.*;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.*;
import com.facebook.android.AsyncFacebookRunner;

public class HalamanLogin extends Activity {

    Facebook facebook = new Facebook("131694736985416");
    AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(facebook);
    Button buttonLogin;
    Button buttonLogout;

    private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halamanlogin);
        
        /**hack
        mPrefs = getSharedPreferences("userdata", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString("nama_user", "Prahesa Kusuma Setia");
		editor.commit();
        Intent itc = new Intent().setClass(this, TabUtama.class);
		startActivity(itc);
		finish();
		end hack**/
        
        
        buttonLogin = (Button)findViewById(R.id.loginfb);
        buttonLogout = (Button)findViewById(R.id.logoutfb);
        /*
         * Get existing access_token if any
         */
        mPrefs = getSharedPreferences("userdata", MODE_PRIVATE);
        String access_token = mPrefs.getString("access_token", null);
        long expires = mPrefs.getLong("access_expires", 0);
        if(access_token != null) {
            facebook.setAccessToken(access_token);
        }
        if(expires != 0) {
            facebook.setAccessExpires(expires);
        }
        
        buttonLogout.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View v) {
        		final ProgressDialog dialog = ProgressDialog.show(HalamanLogin.this, "", "Logging out...", true, false);
        		mAsyncRunner.logout(HalamanLogin.this, new RequestListener() {
					
					@Override
					public void onMalformedURLException(MalformedURLException e, Object state) {
						dialog.cancel();
						Toast.makeText(HalamanLogin.this, "Logout gagal, silakan coba beberapa saat lagi", Toast.LENGTH_SHORT);
					}
					
					@Override
					public void onIOException(IOException e, Object state) {
						dialog.cancel();
						Toast.makeText(HalamanLogin.this, "Logout gagal, silakan coba beberapa saat lagi", Toast.LENGTH_SHORT);
					}
					
					@Override
					public void onFileNotFoundException(FileNotFoundException e, Object state) {
						dialog.cancel();
						Toast.makeText(HalamanLogin.this, "Logout gagal, silakan coba beberapa saat lagi", Toast.LENGTH_SHORT);
					}
					
					@Override
					public void onFacebookError(FacebookError e, Object state) {
						dialog.cancel();
						Toast.makeText(HalamanLogin.this, "Logout gagal, silakan coba beberapa saat lagi", Toast.LENGTH_SHORT);
					}
					
					@Override
					public void onComplete(String response, Object state) {
						SharedPreferences.Editor editor = mPrefs.edit();
						editor.putString("access_token", null);
						editor.putLong("access_expires", 0);
						editor.putString("nama_user", null);
						editor.commit();
						dialog.cancel();
						HalamanLogin.this.finish();
					}
				});
        	}
        });

        /*
         * Only call authorize if the access_token has expired.
         */
        if(!facebook.isSessionValid()) {
        	buttonLogin.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			facebook.authorize(HalamanLogin.this, new String[] {"email"}, new DialogListener() {
        				@Override
        				public void onComplete(Bundle values) {
        					SharedPreferences.Editor editor = mPrefs.edit();
        					editor.putString("access_token", facebook.getAccessToken());
        					editor.putLong("access_expires", facebook.getAccessExpires());
        					editor.commit();
        					mAsyncRunner.request("me", new RequestListener() {
								
								@Override
								public void onMalformedURLException(MalformedURLException e, Object state) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onIOException(IOException e, Object state) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onFileNotFoundException(FileNotFoundException e, Object state) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onFacebookError(FacebookError e, Object state) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onComplete(String response, Object state) {
									try {
										JSONObject json = Util.parseJson(response);
										SharedPreferences.Editor editor = mPrefs.edit();
			        					editor.putString("nama_user", json.getString("name"));
			        					editor.commit();
			        					Log.d("result", ""+facebook.isSessionValid());
			        			        HalamanLogin.this.runOnUiThread(new Runnable() {
											
											@Override
											public void run() {
												if(facebook.isSessionValid()) {
					        			        	String namauser = mPrefs.getString("nama_user", null);
					        			        	buttonLogin.setText(namauser+"'s GreenForce");
					        			        	buttonLogin.setOnClickListener(new OnClickListener() {
					        							
					        							@Override
					        							public void onClick(View v) {
					        								Intent itc = new Intent().setClass(v.getContext(), TabUtama.class);
					        				    			startActivity(itc);
					        				    			finish();
					        							}
					        						});
					        			        	buttonLogout.setVisibility(View.VISIBLE);
					        			        }
											}
										});
									} catch (FacebookError e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
        				}

        				@Override
        				public void onFacebookError(FacebookError error) {}

        				@Override
        				public void onError(DialogError e) {}

        				@Override
        				public void onCancel() {}
        			});	
        		}
        	});
        }
        else {
        	String namauser = mPrefs.getString("nama_user", null);
        	buttonLogin.setText(namauser+"'s GreenForce");
        	buttonLogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent itc = new Intent().setClass(v.getContext(), TabUtama.class);
	    			startActivity(itc);
	    			finish();
				}
			});
        	buttonLogout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);        
    }
}