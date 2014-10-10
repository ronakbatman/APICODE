package com.example.android.navigationdrawerexample;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
 /* BG Fragment appears in the "content_frame", showing a specific Fragment     */
public class PlittoFragment extends Fragment {

    public static final String ARG_NAV_NUMBER = "nav_number";
    public static final String TAG = PlittoFragment.class.getSimpleName();
    private String url = "http://www.plitto.com/api/getSometest";
    TextView text;
    ListView listview;
    List<String> header;
    List<RowInfo> content;
    HashMap<Integer, Integer> map;

    public PlittoFragment() {

    }

    public static PlittoFragment newInstance(int position) {
        Log.d(TAG,"On new instance");
        PlittoFragment fragment = new PlittoFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_NAV_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO Change these to handle the correct Plitto Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"On create view");
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        text = (TextView)rootView.findViewById(R.id.itemListTitle);
        listview = (ListView)rootView.findViewById(R.id.userlist);
        header = new ArrayList<String>();
        content = new ArrayList<RowInfo>();
        map = new HashMap<Integer, Integer>();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"On activity created");

// This sets the view to be the fragment_item_list
        int i = getArguments().getInt(ARG_NAV_NUMBER);
        // BG This takes the nav item name from the string.
        String navItem = getResources().getStringArray(R.array.nav_array)[i];
        Log.d(TAG, "NavItem: " + i + " " + navItem);

        String getSomeUrl = " ";

        // TODO - Build up the proper URL to call.
        // http://plitto.com/api/getSometest
        if( new String("Ditto").equals(navItem) ){
            Log.d(TAG,"You chose 'Ditto'");
            getSomeUrl = "http://www.plitto.com/api/getSometest";
        }
        else if( new String("Friends").equals(navItem) ){
            Log.d(TAG,"You chose 'Friends'");
            getSomeUrl = "http://www.plitto.com/api/friends";
        } else if( new String("Search").equals(navItem) ){
            Log.d(TAG,"You chose 'Search'");
            getSomeUrl = "http://www.plitto.com/api/search";
        }
        else {
            Log.d(TAG,"You chose something other than 'Ditto'" + navItem);
            getSomeUrl = "http://www.plitto.com/api/getSometest";
        }
        Log.d(TAG,"URL To Call " + getSomeUrl);
        // TODO Make the API call
        // new HttpAsyncTask().execute(getSomeUrl);
        new HttpAsyncTask().execute(getSomeUrl);
        // TODO - Update the list contents based on the condition


        // TODO Select the active fragment instead of setting the image.
        // Log.d(TAG,"gridTitle: " + rootView.findViewById(R.id.gridTitle));
        // BG Update the text in the item list.
        // android:id="@+id/itemListTitle"
        // itemListTitle.setTitle(navItem);


/*
            // This creates the file name for the planet and makes it an image.
            int imageId = getResources().getIdentifier(navItem.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());

            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);

            // getActivity().setTitle(planet);
            */
        }


    public class HttpAsyncTask extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject jsonObj=null;
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return jsonObj;
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
            protected void onPostExecute(JSONObject result) {
            Log.v(TAG, "RESULT: " + result);
            //Toast.makeText(getActivity(), "Data Sent! "+result, Toast.LENGTH_LONG).show();
            try {
                JSONArray userArray = result.getJSONArray("results");
                Toast.makeText(getActivity(), "Number of users= "+userArray.length(), Toast.LENGTH_LONG).show();
                for(int i=0;i<userArray.length();i++)
                {
                    JSONObject user=(JSONObject)userArray.get(i);
                    String username = user.getString("username");
                    content.add(new RowInfo(1,username));
                    header.add(username);
                    JSONArray user_lists = (JSONArray)user.getJSONArray("lists");
                    for(int j=0;j<user_lists.length();j++)
                    {
                        JSONObject user_desc=(JSONObject)user_lists.get(j);
                        content.add(new RowInfo(2,user_desc.getString("listname")));
                        header.add(user_desc.getString("listname"));
                        JSONArray final_list = (JSONArray)user_desc.getJSONArray("items");
                        for(int k=0;k<final_list.length();k++)
                        {
                            JSONObject final_elem = (JSONObject)final_list.get(k);
                            header.add(final_elem.getString("thingname"));
                            content.add(new RowInfo(3,final_elem.getString("thingname"),final_elem.getString("added")));
                        }
                    }

                }
                SimpleAdapter s =new SimpleAdapter(content,getActivity().getApplicationContext());
                listview.setAdapter(s);
            }catch (JSONException e)
            {
                e.printStackTrace();
            }



        }
    }

}