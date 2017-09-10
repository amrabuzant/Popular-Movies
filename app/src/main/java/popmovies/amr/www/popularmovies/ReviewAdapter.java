package popmovies.amr.www.popularmovies;

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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private ReviewObj[] reviewArray;

    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ReviewAdapter.ReviewAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        ReviewObj Review = reviewArray[position];
        holder.reviewAuth.setText(Review.reviewAuther);
        holder.reviewContent.setText(Review.reviewContent);
    }

    @Override
    public int getItemCount() {
        if (null == reviewArray) return 0;
        return reviewArray.length;
    }

    public void setReviewArray(ReviewObj[] array){

        reviewArray = array;
        notifyDataSetChanged();

    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView reviewAuth;
        TextView reviewContent;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            reviewAuth = (TextView) itemView.findViewById(R.id.review_author);
            reviewContent = (TextView) itemView.findViewById(R.id.review_content);
        }


    }
}
