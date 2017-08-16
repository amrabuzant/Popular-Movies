package popmovies.amr.www.popularmovies;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amr on 8/14/17.
 */

public final class MovieDBJSONUtils {

    public static MovieObj[] getMovieOBJFromJson(String MovieJSONRsp)
            throws JSONException {

        MovieObj[] parsedMovieData = null;

        JSONObject movieJSON = new JSONObject(MovieJSONRsp);

        JSONArray movieArray = movieJSON.getJSONArray("results");

        parsedMovieData = new MovieObj[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++){
            JSONObject movieInfo = movieArray.getJSONObject(i);

            String originalTitle = movieInfo.getString("original_title");
            String imageThumpAddr = movieInfo.getString("poster_path");
            String plot = movieInfo.getString("overview");
            float userRating = (float) movieInfo.getDouble("vote_average");
            String releaseDate = movieInfo.getString("release_date");

            parsedMovieData[i] = new MovieObj(originalTitle,imageThumpAddr,plot,userRating,releaseDate);

        }

        return parsedMovieData;
    }
}
