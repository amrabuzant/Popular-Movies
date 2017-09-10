package popmovies.amr.www.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils extends AppCompatActivity {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String TOPRATED_API = "/movie/top_rated";
    private static final String REVIEWS_API = "/reviews";
    private static final String TRAILERS_API = "/videos";
    private static final String API_AUTH = "api_key";

    private static final String POPULAR_API =
            "/movie/popular";

    private static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3";

    private static final String AUTH_KEY = "http://api.the";



    public static URL buildUrl(Context context,String APItoCall) {
        Uri builtUri;
        String authKey = context.getString(R.string.AuthKey);
        switch (APItoCall){
            case "Top Rated":
                builtUri = Uri.parse(MOVIEDB_BASE_URL+TOPRATED_API).buildUpon()
                .appendQueryParameter(API_AUTH, authKey)
                        .build();
                break;

            case "Popular":
                builtUri = Uri.parse(MOVIEDB_BASE_URL+POPULAR_API).buildUpon()
                        .appendQueryParameter(API_AUTH, authKey)
                        .build();
                break;

            default:
                return null;
        }
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static URL buildUrl(Context context,String APItoCall,String movieID) {
        Uri builtUri;
        String authKey = context.getString(R.string.AuthKey);
        movieID = "/movie/" + movieID;
        switch (APItoCall){
            case "Reviews":
                builtUri = Uri.parse(MOVIEDB_BASE_URL+movieID+REVIEWS_API).buildUpon()
                        .appendQueryParameter(API_AUTH, authKey)
                        .build();
                break;

            case "Trailers":
                builtUri = Uri.parse(MOVIEDB_BASE_URL+movieID+TRAILERS_API).buildUpon()
                        .appendQueryParameter(API_AUTH, authKey)
                        .build();
                break;

            default:
                return null;
        }
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}