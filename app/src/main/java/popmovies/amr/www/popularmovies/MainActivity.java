package popmovies.amr.www.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static String SELECTED_TYPE = "Popular";

    private TextView errorMessageDisplay;

    private ProgressBar loadingIndicator;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private Context context;

    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        context = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this);

        recyclerView.setAdapter(movieAdapter);




        loadMovies();

    }

    private void loadMovies(){
        showJsonDataView();

        new FetchMovies().execute(SELECTED_TYPE);

    }

    private void showJsonDataView() {
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
        recyclerView.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieObj movie) {
        Context context = this;
//        Toast.makeText(context, movie.toString(), Toast.LENGTH_SHORT)
//                .show();
        Intent intent = new Intent(context,DetailsActivity.class);
        intent.putExtra("MovieOBJ",movie);
        startActivity(intent);
    }


    public class FetchMovies extends AsyncTask<String,Void,MovieObj[]>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(MovieObj[] movieObjs) {
            super.onPostExecute(movieObjs);
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (movieObjs != null) {
                for (MovieObj movie : movieObjs){
                    Log.i("Main_Activiy",movie.toString());
                }
                showJsonDataView();
                movieAdapter.setMovieArray(movieObjs);
            } else {
                showErrorMessage();
            }
        }

        @Override
        protected MovieObj[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            URL request = NetworkUtils.buildUrl(context,strings[0]);
            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(request);

                MovieObj[] MovieArray = MovieDBJSONUtils
                        .getMovieOBJFromJson(jsonMoviesResponse);

                return MovieArray;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.SortingType) {
            SELECTED_TYPE = String.valueOf(item.getTitle());
            movieAdapter.setMovieArray(null);
            loadMovies();
            updateMenuTitles();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMenuTitles() {
        MenuItem menuItem = menu.findItem(R.id.SortingType);
        switch (SELECTED_TYPE){
            case "Popular":
                menuItem.setTitle("Top Rated");
                break;
            case "Top Rated":
                menuItem.setTitle("Popular");
                break;
        }
    }
}
