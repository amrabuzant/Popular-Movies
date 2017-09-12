package popmovies.amr.www.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Parcelable;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailsActivity extends AppCompatActivity {

    private MovieObj movie;
    private ReviewObj[] reviews;
    private TrailerObj[] trailers;
    private Context context;
    @BindView(R.id.movieName)
     TextView movieName;
    @BindView(R.id.MovieReleaseDate)
     TextView movieReleaseDate;
    @BindView(R.id.UserRating)
     TextView movieRating;
    @BindView(R.id.PlotText)
     TextView moviePlot;
    @BindView(R.id.MoviePoster)
     ImageView moviePoster;
    @BindView(R.id.markFav)
    Button addToFav;
    @BindView(R.id.detailsScrollView)
    ScrollView scrollView;
    @BindView(R.id.reviews_list)
     RecyclerView reviewsList;
    private ReviewAdapter reviewsAdapter;

    @BindView(R.id.trailers_list)
     RecyclerView trailersList;
    private TrailerAdapter trailersAdapter;
    public final static String LIST_STATE_KEY1 = "recycler_list_state1";
    public final static String LIST_STATE_KEY2 = "recycler_list_state2";
    public final static String LIST_Object_KEY1 = "recycler_list_objects1";
    public final static String LIST_Object_KEY2 = "recycler_list_objects2";

    Parcelable listState1;
    Parcelable listState2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);


        context =this;
        reviewsList.setLayoutManager(new LinearLayoutManager(this));

        reviewsAdapter = new ReviewAdapter();
        reviewsAdapter.setReviewArray(reviews);

        reviewsList.setAdapter(reviewsAdapter);

        trailersList.setLayoutManager(new LinearLayoutManager(this));

        trailersAdapter = new TrailerAdapter();
        trailersAdapter.setTrailerArray(trailers);

        trailersList.setAdapter(trailersAdapter);

        if (savedInstanceState != null){
            listState1 = savedInstanceState.getParcelable(LIST_STATE_KEY1);
            if (listState1 != null) {
                reviews = (ReviewObj[])savedInstanceState.getParcelableArray(LIST_Object_KEY1);
                reviewsAdapter.setReviewArray(reviews);
                reviewsList.getLayoutManager().onRestoreInstanceState(listState1);
            }
            listState2 = savedInstanceState.getParcelable(LIST_STATE_KEY2);
            if (listState2 != null) {
                trailers = (TrailerObj[])savedInstanceState.getParcelableArray(LIST_Object_KEY2);
                trailersAdapter.setTrailerArray(trailers);
                trailersList.getLayoutManager().onRestoreInstanceState(listState2);
            }
        }
        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra("MovieOBJ")) {
            movie = (MovieObj)intentThatStartedThisActivity.getParcelableExtra("MovieOBJ");

            loadMovieData();

            movieName.setText(movie.originalTitle);
            movieReleaseDate.setText(movie.releaseDate);
            movieRating.setText(String.valueOf(movie.userRating));
            moviePlot.setText(movie.plot);


            String url = "http://image.tmdb.org/t/p/" +"w185/" + movie.imageThumpAddr;
            Picasso.with(this)
                    .load(url)
                    .into(moviePoster);

        }
        updateButtonText();

    }

    private void loadMovieData(){

        Log.i("Hello",movie.toString());
        String movieID = ""+movie.id;
        if (trailers == null & reviews == null) {
            new FetchReviews().execute("Reviews", movieID);
            new FetchTrailers().execute("Trailers", movieID);
        }

    }
    public class FetchTrailers extends AsyncTask<String,Void,TrailerObj[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(TrailerObj[] trailerObjs) {
            super.onPostExecute(trailerObjs);
            if (trailerObjs != null) {
                trailers=trailerObjs;
                trailersAdapter.setTrailerArray(trailerObjs);
            } else {
                Log.e("Details Activity","Error in Trailor Loading");
            }
        }

        @Override
        protected TrailerObj[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            URL request = NetworkUtils.buildUrl(context,strings[0],strings[1]);
            try {
                String jsonTrailerResponse = NetworkUtils
                        .getResponseFromHttpUrl(request);

                TrailerObj[] trailerObjs = TrailerJSONUtils.getTrailerOBJFromJson(jsonTrailerResponse);

                return trailerObjs;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


    public class FetchReviews extends AsyncTask<String,Void,ReviewObj[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ReviewObj[] reviewObjs) {
            super.onPostExecute(reviewObjs);
            if (reviewObjs != null) {
                reviews = reviewObjs;
                reviewsAdapter.setReviewArray(reviewObjs);
            } else {
                Log.e("Details Activity","Error in Reviews Loading");
            }
        }

        @Override
        protected ReviewObj[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            URL request = NetworkUtils.buildUrl(context,strings[0],strings[1]);
            try {
                String jsonReviewssResponse = NetworkUtils
                        .getResponseFromHttpUrl(request);

                ReviewObj[] reviewObjs = ReviewJSONUtils.getReviewOBJFromJson(jsonReviewssResponse);

                return reviewObjs;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void addMovieToFav(View view) {

        int id = (int) movie.id;

        // Build appropriate uri with String row id appended
        String stringId = Integer.toString(id);
        Uri uri = MoveContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        int count = cursor.getCount();getContentResolver().query(uri,null,null,null,null);

        if (count > 0) {
            getContentResolver().delete(uri,null,null);
            Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show();
            updateButtonText();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoveContract.MovieEntry.COLUMN_MOVIE_ID, movie.id);
        contentValues.put(MoveContract.MovieEntry.COLUMN_NAME, movie.originalTitle);
        contentValues.put(MoveContract.MovieEntry.COLUMN_RELEASE_DATE, movie.releaseDate);
        contentValues.put(MoveContract.MovieEntry.COLUMN_RATING, movie.userRating);
        contentValues.put(MoveContract.MovieEntry.COLUMN_PLOT,movie.plot);
        contentValues.put(MoveContract.MovieEntry.COLUMN_IMAGE_URL,movie.imageThumpAddr);

        Uri uri2 = getContentResolver().insert(MoveContract.MovieEntry.CONTENT_URI, contentValues);
        if (uri2 != null) {
            Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
        }else{
            Log.e("Details","Error in add");
        }
        updateButtonText();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        listState1 = reviewsList.getLayoutManager().onSaveInstanceState();
        listState2 = trailersList.getLayoutManager().onSaveInstanceState();

        outState.putParcelable(LIST_STATE_KEY1, listState1);
        outState.putParcelable(LIST_STATE_KEY2, listState2);
        outState.putParcelableArray(LIST_Object_KEY1,reviews);
        outState.putParcelableArray(LIST_Object_KEY2,trailers);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ scrollView.getScrollX(), scrollView.getScrollY()});
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        trailers = (TrailerObj [])savedInstanceState.getParcelableArray(LIST_Object_KEY2);
        listState2 = savedInstanceState.getParcelable(LIST_STATE_KEY2);
        reviews = (ReviewObj[])savedInstanceState.getParcelableArray(LIST_Object_KEY1);
        listState1 = savedInstanceState.getParcelable(LIST_STATE_KEY1);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if(position != null)
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState1 != null) {
            reviewsList.getLayoutManager().onRestoreInstanceState(listState1);
        }
        if (listState2 != null) {
            trailersList.getLayoutManager().onRestoreInstanceState(listState2);
        }

    }

    void updateButtonText(){
        int id = (int) movie.id;

        String stringId = Integer.toString(id);
        Uri uri = MoveContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        int count = cursor.getCount();getContentResolver().query(uri,null,null,null,null);

        if (count > 0) {
            addToFav.setText("Remove As Favorite");
        }else {
            addToFav.setText("Mark As Favorite");
        }
    }

}
