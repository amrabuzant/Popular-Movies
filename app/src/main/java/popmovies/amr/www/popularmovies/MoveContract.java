package popmovies.amr.www.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by amr on 9/11/17.
 */

public class MoveContract {

    public static final String AUTHORITY = "popmovies.amr.www.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_IMAGE_URL = "image_url";
    }
}
