package com.example.safafard.jsonhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//////////
import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    private static String url;

    //JSON Node Names
    private static final String json_name = "kelidestan";
    private static final String json_node_name_1 = "id";
    private static final String json_node_name_2 = "name";
    private static final String json_node_name_3 = "city";

    JSONArray kelidestan = null;
    public String app_id_string;

    public String[] json_string_1_all;
    public String[] json_string_2_all;
    public String[] json_string_3_all;

    public int json_length;
    public ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "http://www.kelidestan.com/fixed-url/kelidestan-json-1.html";

        new JSONParse().execute();
    }


    public class JSONParse extends AsyncTask<String, String, JSONObject> {
        public ProgressDialog pDialog;
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        public JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        public void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // kelidestan
                kelidestan = json.getJSONArray(json_name);
                // build String
                json_length = kelidestan.length();
                json_string_1_all = new String [kelidestan.length()];
                json_string_2_all = new String [kelidestan.length()];
                json_string_3_all = new String [kelidestan.length()];
                for(int i = 0; i < kelidestan.length(); i++){
                    JSONObject c = kelidestan.getJSONObject(i);
                    // Storing JSON item in a Variable --> with UTF-8 for persian words
                    json_string_1_all[i] = new String(c.getString(json_node_name_1).getBytes("ISO-8859-1"), "UTF-8");
                    json_string_2_all[i] = new String(c.getString(json_node_name_2).getBytes("ISO-8859-1"), "UTF-8");
                    json_string_3_all[i] = new String(c.getString(json_node_name_3).getBytes("ISO-8859-1"), "UTF-8");
                }

                // show data in ListView
                setListAdapter(new MyAdapter(MainActivity.this,
                        R.layout.list_layout,
                        R.id.textView1,
                        json_string_1_all));
                lv = getListView();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
    }


    private class MyAdapter extends ArrayAdapter<String>{

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         String[] strings) {
            super(context, resource, textViewResourceId, strings);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_item, parent, false);

            String id = json_string_1_all[position];
            TextView tv_1 = (TextView) row.findViewById(R.id.textView1);
            tv_1.setText(id);

            String name = json_string_2_all[position];
            TextView tv_2 = (TextView) row.findViewById(R.id.textView2);
            tv_2.setText(name);

            String city = json_string_3_all[position];
            TextView tv_3 = (TextView) row.findViewById(R.id.textView3);
            tv_3.setText(city);

            return row;
        }


    }


}
