package popmovies.amr.www.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amr on 9/10/17.
 */

public class ReviewObj implements Parcelable {
    String reviewAuther;
    String reviewContent;

    public ReviewObj(String name, String url)
    {
        this.reviewAuther = name;
        this.reviewContent = url;

    }

    protected ReviewObj(Parcel in) {
        reviewAuther = in.readString();
        reviewContent = in.readString();
    }

    public static final Creator<ReviewObj> CREATOR = new Creator<ReviewObj>() {
        @Override
        public ReviewObj createFromParcel(Parcel in) {
            return new ReviewObj(in);
        }

        @Override
        public ReviewObj[] newArray(int size) {
            return new ReviewObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reviewAuther);
        parcel.writeString(reviewContent);
    }
}
