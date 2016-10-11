package net.dboyce.homework4randompics;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getpicsTask GPT = new getpicsTask();
        GPT.execute("95128,us");

        WebView disim = (WebView) findViewById(R.id.displayimage);
        disim.loadUrl("https://images.unsplash.com/profile-fb-1450265639-ad3a11fcc7df.jpg?ixlib=rb-0.3.5");
    }



    public class getpicsTask extends AsyncTask<String, Void, Void> {

        public getpicsTask(int pcode){
            super();
            this.postcode = pcode;
        }

        public getpicsTask(){
            super();
            this.postcode = 95128;
        }

        private int postcode;
        private final String LOG_TAG = getpicsTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String picsJsonStr = null;


            try {

                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&APPID=f200abdcf73ad775e75725459739c41f");
                final String FORECAST_BASE_URL ="https://api.unsplash.com/photos/?client_id=855704e235d2c19021e60ee0ef756e1bd5d911f9d7a8bb84a2f80ad0ae4d4f6a";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";
                //https://api.unsplash.com/photos/?client_id=YOUR_APPLICATION_ID
               /* Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
                        .build();
                   */
                //URL url = new URL(builtUri.toString());

                URL url = new URL("https://api.unsplash.com/photos/?client_id=855704e235d2c19021e60ee0ef756e1bd5d911f9d7a8bb84a2f80ad0ae4d4f6a&query=star");

                //Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                picsJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Returned JSON String: " + picsJsonStr );
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

    }




}
