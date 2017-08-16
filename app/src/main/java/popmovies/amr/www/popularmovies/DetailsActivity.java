package popmovies.amr.www.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private MovieObj movie;

    private TextView movieName;
    private TextView movieReleaseDate;
    private TextView movieRating;
    private TextView moviePlot;
    private ImageView moviePoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movieName = (TextView) findViewById(R.id.movieName);
        movieReleaseDate = (TextView) findViewById(R.id.MovieReleaseDate);
        movieRating = (TextView) findViewById(R.id.UserRating);
        moviePlot = (TextView) findViewById(R.id.PlotText);
        moviePoster = (ImageView) findViewById(R.id.MoviePoster);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra("MovieOBJ")) {
            movie = (MovieObj)intentThatStartedThisActivity.getParcelableExtra("MovieOBJ");

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
