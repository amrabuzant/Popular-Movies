package popmovies.amr.www.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    @BindView(R.id.reviews_list)
     RecyclerView reviewsList;
    private ReviewAdapter reviewsAdapter;

    @BindView(R.id.trailers_list)
     RecyclerView trailersList;
    private TrailerAdapter trailersAdapter;

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
    }

    private void loadMovieData(){

        Log.i("Hello",movie.toString());
        String movieID = ""+movie.id;
        new FetchReviews().execute("Reviews",movieID);
        new FetchTrailers().execute("Trailers",movieID);

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
                for (TrailerObj trailer : trailerObjs){
                    Log.i("Details_Activiy",movie.toString());
                }
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
                for (ReviewObj review : reviewObjs){
                    Log.i("Details Activiy",movie.toString());
                }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
