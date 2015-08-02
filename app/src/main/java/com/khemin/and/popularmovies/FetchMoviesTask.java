package com.khemin.and.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khemin on 2/8/15.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
    private static final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    MoviesAdapter mMoviesAdapter;
    public FetchMoviesTask(MoviesAdapter moviesAdapter) {
        this.mMoviesAdapter = moviesAdapter;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJsonStr = null;


        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String MOVIE_DB_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";



            Uri builtUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, ApiKeys.MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

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
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
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
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            Log.wtf(LOG_TAG, moviesJsonStr);
            return parseMoviesData(moviesJsonStr);
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    private List<Movie> parseMoviesData(String moviesJsonStr) throws JSONException {
        List<Movie> movieList = new ArrayList<>();
        JSONObject root = new JSONObject(moviesJsonStr);
        JSONArray results = root.getJSONArray("results");
        String[] resultStringArray = new String[results.length()];
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < results.length(); i++){
            JSONObject movieJson = results.getJSONObject(i);
            Movie movieObj = new Movie();
            movieObj.setId(movieJson.getLong("id"));
            movieObj.setTitle(movieJson.getString("original_title"));
            movieObj.setPosterUrl("http://image.tmdb.org/t/p/w342/" + movieJson.getString("poster_path"));
            movieObj.setSynopsis(movieJson.getString("overview"));
            movieObj.setRating(movieJson.getDouble("vote_average"));
            String releaseDateStr = movieJson.getString("release_date");
            try {
                movieObj.setReleaseDate(dateFormat.parse(releaseDateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movieList.add(movieObj);
        }
        return movieList;
    }

    @Override
    protected void onPostExecute(List<Movie> result) {
        if (result != null) {
            mMoviesAdapter.setData(result);
            // New data is back from the server.  Hooray!
        }
    }
}
