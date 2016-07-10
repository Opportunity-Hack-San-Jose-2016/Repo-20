package com.opportunityhack.teamhacks.helpbot_android;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;


public class RefugeeListFragment extends ListFragment implements OnItemClickListener {

    private List<RowItem> rowItems;
    JSONArray jsonArray1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ProgressBar v = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        v.getIndeterminateDrawable().setColorFilter(0xFFFF0000,
                android.graphics.PorterDuff.Mode.MULTIPLY);

        getListView().setOnItemClickListener(this);

        new RetrieveFeedTask().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        RowItem item = (RowItem) adapterView.getItemAtPosition(i);
        Bundle bundle = new Bundle();
        bundle.putString("refugeeId", item.getRefugeeID());
        bundle.putString("fragment", "fromList");
        try {
            bundle.putString("jsonObject", jsonArray1.getJSONObject(i).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, detailFragment);
        fragmentTransaction.commit();

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... urls) {
            //  String email = emailText.getText().toString();
            // Do some validation here

            try {
                //HttpClient http = new DefaultHttpClient();
                URL url = new URL("https://messengerbotservice.herokuapp.com/listrefugees");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");



                Log.i("url", urlConnection.toString());
                try {
                    Log.i("responsecode",urlConnection.getResponseMessage());
                    Log.i("code",Integer.toString(urlConnection.getResponseCode()));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {

            CustomAdapter adapter;
            rowItems = new ArrayList<RowItem>();
            try {
                JSONArray jsonArray = new JSONArray(response);
                jsonArray1 = jsonArray.getJSONArray(0);
                Log.i("jsonArray", jsonArray1.toString());
                for (int i=0;i<jsonArray1.length();i++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    Log.i("json", jsonObject.toString());
                    RowItem item1 = new RowItem(jsonObject.optString("ID"), jsonObject.optString("address"));
                    rowItems.add(item1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new CustomAdapter(getActivity(), rowItems);
            setListAdapter(adapter);
            getActivity().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }

    }
}
