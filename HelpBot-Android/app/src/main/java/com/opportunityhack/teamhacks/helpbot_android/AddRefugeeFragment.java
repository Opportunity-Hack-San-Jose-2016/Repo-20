package com.opportunityhack.teamhacks.helpbot_android;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRefugeeFragment extends Fragment {

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private TextView address;
    private Button help;
    private String refugeeID;
    private String refugeePhone;
    private String refugeeAddress;
    private String refugeeMessage;

    public AddRefugeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_refugee, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity().getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();


        address = (TextView) getActivity().findViewById(R.id.refugee_text_address);
        address.requestFocus();
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    //  builder.setLatLngBounds(new LatLngBounds(new LatLng(latitude,longitude),new LatLng(latitude,longitude)));
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (Exception e) {

                }

            }
        });

        help = (Button) getActivity().findViewById(R.id.btn_help);
        help.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText refugeeIDText = (EditText)getActivity().findViewById(R.id.refugee_text_id);
                refugeeID = refugeeIDText.getText().toString();

                EditText refugeePhoneText = (EditText) getActivity().findViewById(R.id.refugee_text_phone);
                refugeePhone = refugeePhoneText.getText().toString();

                EditText refugeeAddressText = (EditText) getActivity().findViewById(R.id.refugee_text_address);
                refugeeAddress = refugeeAddressText.getText().toString();

                new PostTask().execute();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, getActivity());
                // Do something with the place
                address.setText(selectedPlace.getAddress());
            }

        }
    }

    class PostTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... urls) {

            // Do some validation here

            try {
                URL url = new URL("https://messengerbotservice.herokuapp.com/refugee");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                String postData = "refugeeID=" + refugeeID
                        + "&refugeePhone=" + refugeePhone
                        + "&refugeeAddress=" + URLEncoder.encode(refugeeAddress.replace(","," "),"UTF-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(postData);
                wr.flush();
                wr.close();


                Log.i("url",urlConnection.toString());
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

        }

    }

}
