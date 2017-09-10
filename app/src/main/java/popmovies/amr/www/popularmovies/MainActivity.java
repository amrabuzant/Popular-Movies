package popmovies.amr.www.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
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

    private GridLayoutManager gridLayoutManager;

    private ProgressBar loadingIndicator;

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private Context context;

    private Menu menu;
    Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        context = this;

        gridLayoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridLayoutManager);


        movieAdapter = new MovieAdapter(this);

        recyclerView.setAdapter(movieAdapter);



        loadMovies();

        if (savedInstanceState != null){
            mListState = savedInstanceState.getParcelable("position");
            if (mListState != null) {
                gridLayoutManager.onRestoreInstanceState(mListState);
            }
        }



    }

    private void loadMovies(){
        showJsonDataView();

        new FetchMovies().execute(SELECTED_TYPE);

    }

    private void loadFav(){
        showJsonDataView();

        new FavouriteMoviesFetchTask().execute();

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
//                    Log.i("Main_Activiy",movie.toString());
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
        }else if (id == R.id.favMovies){
            movieAdapter.setMovieArray(null);
            loadFav();
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

    public class FavouriteMoviesFetchTask extends AsyncTask<Void, Void, Void> {

        MovieObj[] movies;

        @Override
        protected Void doInBackground(Void... params) {
            Uri uri = MoveContract.MovieEntry.CONTENT_URI;
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            int count = cursor.getCount();
            movies = new MovieObj[cursor.getCount()];
            if (count == 0) {
                return null;
            }

            if (cursor.moveToFirst()) {
                do {
                    String originalTitle = cursor.getString(cursor.getColumnIndex(MoveContract.MovieEntry.COLUMN_NAME));
                    String imageThumpAddr = cursor.getString(cursor.getColumnIndex(MoveContract.MovieEntry.COLUMN_IMAGE_URL));
                    String plot = cursor.getString(cursor.getColumnIndex(MoveContract.MovieEntry.COLUMN_PLOT));
                    float userRating = cursor.getFloat(cursor.getColumnIndex(MoveContract.MovieEntry.COLUMN_RATING));
                    String releaseDate =cursor.getString(cursor.getColumnIndex(MoveContract.MovieEntry.COLUMN_RELEASE_DATE));
                    int movieID = cursor.getInt(cursor.getColumnIndex(MoveContract.MovieEntry.COLUMN_MOVIE_ID));
                    movies[cursor.getPosition()] = new MovieObj(originalTitle,imageThumpAddr,plot,userRating,releaseDate,movieID);;
                } while (cursor.moveToNext());
            }

            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            movieAdapter.setMovieArray(movies);
            movieAdapter.notifyDataSetChanged();
            showJsonDataView();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mListState = gridLayoutManager.onSaveInstanceState();
        outState.putParcelable("position", mListState);

        super.onSaveInstanceState(outState);
    }
}
