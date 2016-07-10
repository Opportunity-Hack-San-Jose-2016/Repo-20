package com.opportunityhack.teamhacks.helpbot_android;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class DetailFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        Bundle data = getArguments();
        TextView refugeeID = (TextView) getActivity().findViewById(R.id.refugee_uin);
        TextView refugeeZip = (TextView) getActivity().findViewById(R.id.refugee_zip);
        TextView refugeePhone = (TextView) getActivity().findViewById(R.id.refugee_phone);
        TextView refugeeMsg = (TextView) getActivity().findViewById(R.id.refugee_msg);
        getActivity().setTitle("Detail");
        try {
            if (data.getString("fragment") != null) {
               JSONObject jsonObject = new JSONObject(data.getString("jsonObject"));
                refugeeID.setText(jsonObject.optString("ID"));
                refugeeZip.setText(jsonObject.optString("address"));
                refugeePhone.setText(jsonObject.optString("phone"));
                refugeeMsg.setText("Help me out");
            } else {
                refugeeID.setText(data.getString("refugeeID"));
                refugeeZip.setText(data.getString("refugeeZipCode"));
                refugeePhone.setText(data.getString("refugeePhone"));
                refugeeMsg.setText(data.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... urls) {
            //  String email = emailText.getText().toString();
            // Do some validation here

            try {
                //HttpClient http = new DefaultHttpClient();
                URL url = new URL("");

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
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            //  progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            CustomAdapter adapter;
            try {
                JSONObject jsonObject = new JSONObject(response);
                TextView refugeeID = (TextView) getActivity().findViewById(R.id.refugee_uin);
                TextView refugeeZip = (TextView) getActivity().findViewById(R.id.refugee_zip);
                TextView refugeePhone = (TextView) getActivity().findViewById(R.id.refugee_phone);
                TextView refugeeMsg = (TextView) getActivity().findViewById(R.id.refugee_msg);

                refugeeID.setText(jsonObject.optString("refugeeID"));
                refugeeZip.setText(jsonObject.optString("refugeeZipCode"));
                refugeePhone.setText(jsonObject.optString("refugeePhone"));
                refugeeMsg.setText(jsonObject.optString("message"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
