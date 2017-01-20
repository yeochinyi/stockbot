package com.example.myfirstapp;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static android.text.Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private TableLayout stockTable;
    private RequestQueue queue;
    private MultiAutoCompleteTextView editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stockTable = (TableLayout) findViewById(R.id.stock_table);
        editText = (MultiAutoCompleteTextView ) findViewById(R.id.edit_message);
        queue = Volley.newRequestQueue(this);

        AssetManager assetManager = getAssets();

        List<String> symbols = new ArrayList<String>();

        try {
            InputStream is = assetManager.open("companylist.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String line = "";
            StringTokenizer st = null;

                while ((line = reader.readLine()) != null) {
                    st = new StringTokenizer(line, ",");
                    String symbol = st.nextToken().replaceAll("\"","");
                    String desc = st.nextToken().replaceAll("\"","");
                    symbols.add(symbol);
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, symbols);
        editText.setAdapter(adapter);
        editText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        String message = editText.getText().toString();

        getInstrumentFromSource(message, new InstrumentCallback() {
            @Override
            public void callback(Instrument i) {
                addStockDisplay(i);
            }
        });

    }

    private void getInstrumentFromSource(String commentDelimitedSymbols, final InstrumentCallback cb){
        String url = "http://finance.google.com/finance/info?q=" + commentDelimitedSymbols;

//        {
//            "id":"22144",
//                "t":"AAPL",       // ticker
//                "e":"NASDAQ",     // exchange
//                "l":"119.04",     //last_price
//                "l_fix":"119.04",
//                "l_cur":"119.04",
//                "s":"0",
//                "ltt":"4:00PM EST",   //last trade time
//                "lt":"Jan 13, 4:00PM EST",
//                "lt_dts":"2017-01-13T16:00:01Z",
//                "c":"-0.21",      //change
//                "c_fix":"-0.21",
//                "cp":"-0.18",     //change percent
//                "cp_fix":"-0.18",
//                "ccol":"chr",
//                "pcls_fix":"119.25"
//        }

//        t	Ticker
//        e	Exchange
//        l	Last Price
//        ltt	Last Trade Time
//        l	Price
//        lt	Last Trade Time Formatted
//        lt_dts	Last Trade Date/Time
//        c	Change
//        cp	Change Percentage
//        el	After Hours Last Price
//        elt	After Hours Last Trade Time Formatted
//        div	Dividend
//        yld	Dividend Yield

        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        response = response.trim();
                        if(response.startsWith("//")){
                            response = response.substring(2);
                        }

                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String ticker = jsonObject.getString("t");
                                double lastPrice = jsonObject.getDouble("l");

                                Instrument instrument = new InstrumentBuilder().setSymbol(ticker).setLastPrice(lastPrice).createInstrument();
                                cb.callback(instrument);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError e) {
                        e.printStackTrace();

                    }
                });
        // Add the request to the RequestQueue.
        queue.add(request);

    }

    private void addStockDisplay(Instrument i){
        Spanned html = Html.fromHtml(String.format("<b>%s</b> %s", i.getSymbol(), i.getLastPrice()));
        //textView.setText(Html.fromHtml(html));

        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TextView tv = new TextView(this);
        tv.setText(html);

        //myView.setText(html);
        row.addView(tv);
        stockTable.addView(row);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
