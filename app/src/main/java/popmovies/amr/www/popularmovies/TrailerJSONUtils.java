package popmovies.amr.www.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amr on 9/11/17.
 */

public class TrailerJSONUtils {
    public static TrailerObj[] getTrailerOBJFromJson(String trailerJSONRsp)
            throws JSONException {

        TrailerObj[] parsedMovieData = null;

        JSONObject trailerJSON = new JSONObject(trailerJSONRsp);

        JSONArray trailerArray = trailerJSON.getJSONArray("results");

        parsedMovieData = new TrailerObj[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++){
            JSONObject trailerInfo = trailerArray.getJSONObject(i);

            String name = trailerInfo.getString("name");
            String url = trailerInfo.getString("key");

            parsedMovieData[i] = new TrailerObj(name,url);

        }

        return parsedMovieData;
    }
}
