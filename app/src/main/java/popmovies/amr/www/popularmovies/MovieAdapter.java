package popmovies.amr.www.popularmovies;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by amr on 8/14/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private MovieObj[] movieArray;

    private final MovieAdapterOnClickHandler clickHandler;

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        view.setMinimumWidth(parent.getWidth()/2);
        view.setMinimumHeight(parent.getHeight()/2);
        return new MovieAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        MovieObj movie = movieArray[position];
        String url = "http://image.tmdb.org/t/p/" +"w185/" + movie.imageThumpAddr;
        Picasso.with(holder.itemView.getContext())
                .load(url)
                .into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if (null == movieArray) return 0;
        return movieArray.length;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieObj movie);
    }


    public MovieAdapter(MovieAdapterOnClickHandler tttt) {
        clickHandler = tttt;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView moviePoster;
        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.imageButton);
            moviePoster.setMinimumHeight(itemView.getMinimumHeight());
            moviePoster.setMinimumWidth(itemView.getMinimumWidth());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieObj movie = movieArray[adapterPosition];
            clickHandler.onClick(movie);
        }
    }

    public void setMovieArray(MovieObj[] array){

        movieArray = array;
        notifyDataSetChanged();

    }

//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        MovieObj movie = getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item_list, parent, false);
//        }
//        ImageButton iconView = (ImageButton) convertView.findViewById(R.id.imageButton);
//        //set image link
//        String url = "http://image.tmdb.org/t/p/" +"w185/" + movie.imageThumpAddr;
//        Picasso.with(parent.getContext())
//                .load(url)
//                .resize(parent.getWidth()/2,parent.getHeight()/2)
//                .into(iconView);
//        return convertView;
//    }
}
