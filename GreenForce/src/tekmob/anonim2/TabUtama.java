package tekmob.anonim2;
	
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

	/**
	 * Class Tab ini digunakan untuk menghandle tampilan tab
	 * 
	 * @author Alif, Fajri, Hein, Arya
	 */
public class TabUtama extends TabActivity {
		
		private TabHost mTabHost;

		private void setupTabHost() {
			mTabHost = (TabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup();
		}

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			// construct the tabhost
			setContentView(R.layout.custom_tab);

			setupTabHost();
			mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

			Intent masalahIntent,laporkanIntent; // Reusable Intent for each tab

			// Create an Intent to launch an Activity for the tab (to be reused)
			masalahIntent = new Intent().setClass(this, MenuMasalah.class);
			
			laporkanIntent = new Intent().setClass(this, MenuLaporkan.class);
			
			setupTab("Masalah",masalahIntent);
			setupTab("Laporkan",laporkanIntent);
		}

		private void setupTab(final String tag, Intent intent) {
			View tabview = createTabView(mTabHost.getContext(), tag);

			TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
			mTabHost.addTab(setContent);

		}

		private static View createTabView(final Context context, final String text) {
			View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
			TextView tv = (TextView) view.findViewById(R.id.tabsText);
			tv.setText(text);
			return view;
		}
}
