package popmovies.amr.www.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amr on 9/11/17.
 */

public class ReviewJSONUtils {
    public static ReviewObj[] getReviewOBJFromJson(String reviewJSONRsp)
            throws JSONException {

        ReviewObj[] parsedMovieData = null;

        JSONObject reviewJSON = new JSONObject(reviewJSONRsp);

        JSONArray reviewArray = reviewJSON.getJSONArray("results");

        parsedMovieData = new ReviewObj[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++){
            JSONObject reviewInfo = reviewArray.getJSONObject(i);


            String name = reviewInfo.getString("author");
            String content = reviewInfo.getString("content");

            parsedMovieData[i] = new ReviewObj(name,content);

        }

        return parsedMovieData;
    }
}
