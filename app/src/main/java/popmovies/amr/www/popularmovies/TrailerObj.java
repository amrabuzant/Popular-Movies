package popmovies.amr.www.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amr on 9/10/17.
 */

public class TrailerObj implements Parcelable {

    String trailerName;
    String trailerUrl;

    public TrailerObj(String name, String url)
    {
        this.trailerName = name;
        this.trailerUrl = url;

    }

    protected TrailerObj(Parcel in) {
        trailerName = in.readString();
        trailerUrl = in.readString();
    }

    public static final Creator<TrailerObj> CREATOR = new Creator<TrailerObj>() {
        @Override
        public TrailerObj createFromParcel(Parcel in) {
            return new TrailerObj(in);
        }

        @Override
        public TrailerObj[] newArray(int size) {
            return new TrailerObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trailerName);
        parcel.writeString(trailerUrl);
    }
}
