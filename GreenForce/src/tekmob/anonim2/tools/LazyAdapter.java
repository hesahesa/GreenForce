package tekmob.anonim2.tools;

import java.util.ArrayList;
import java.util.HashMap;
import tekmob.anonim2.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView namamasalah = (TextView)vi.findViewById(R.id.namamasalah); // nama masalah
        TextView lokasidanpelapor = (TextView)vi.findViewById(R.id.lokasidanpelapor); // artist name
        //TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        HashMap<String, String> entryMasalah = new HashMap<String, String>();
        entryMasalah = data.get(position);
        
        /**
		 * WORKAROUND!
		 * method buat nge-load gambar pake caching nih, jadi nanti
		 * bikin method di server buat link yg kalo di browse nge-return gambar
		 * biar caching nya nggak ilang
		 */
        
        // Setting all values in listview
        namamasalah.setText(entryMasalah.get("nama"));
        lokasidanpelapor.setText("Lokasi: "+entryMasalah.get("lokasi")+"\nPelapor: "+entryMasalah.get("pelapor"));
        //imageLoader.DisplayImage(entryMasalah.get(MenuMasalah.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}