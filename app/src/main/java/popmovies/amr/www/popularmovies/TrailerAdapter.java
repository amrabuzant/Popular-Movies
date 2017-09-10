package popmovies.amr.www.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by amr on 9/10/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private TrailerObj[] trailerArray;

    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new TrailerAdapter.TrailerAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        TrailerObj trailer = trailerArray[position];
        holder.trailerName.setText(trailer.trailerName);
        holder.url = trailer.trailerUrl;
    }

    @Override
    public int getItemCount() {
        if (null == trailerArray) return 0;
        return trailerArray.length;
    }

    public void setTrailerArray(TrailerObj[] array){

        trailerArray = array;
        notifyDataSetChanged();

    }
    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView trailerName;
        ImageButton playButton;
        String url;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            trailerName = (TextView) itemView.findViewById(R.id.trailer_name_text_view);
            playButton = (ImageButton) itemView.findViewById(R.id.playButton);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + url));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + url));
            try {
                view.getContext().startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                view.getContext().startActivity(webIntent);
            }
        }
    }
}
