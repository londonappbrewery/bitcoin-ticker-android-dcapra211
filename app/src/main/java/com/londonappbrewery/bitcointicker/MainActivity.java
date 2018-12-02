package com.londonappbrewery.bitcointicker;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Bitcoin", "Item Selected: " + parent.getItemAtPosition(position));
                String selectedItem = "BTC" + parent.getItemAtPosition(position);

                letsDoSomeNetworking(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
                Log.d("Bitcoin", "Nothing selected");
            }
        });


    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String selectedItem) {

        AsyncHttpClient client = new AsyncHttpClient();

        String apiURL = BASE_URL + selectedItem;
        Log.d("Bitcoin", apiURL);
        client.get(apiURL, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                Log.d("Bitcoin", "response: " + response.toString());
                try {
                    String price = response.getString("last");
                    Log.d("Bitcoin", price);
                    mPriceTextView.setText(price);
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                // called when response HTTP status is "200 OK"
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.d("Bitcoin", "Fail: " + e.toString());

            }
            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                Log.d("Bitcoin", "Retrying api call.");
            }
        });


    }


}
