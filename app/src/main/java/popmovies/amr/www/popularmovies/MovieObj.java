package popmovies.amr.www.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amr on 8/14/17.
 */

public class MovieObj implements Parcelable {

    String originalTitle;
    String imageThumpAddr;
    String plot;
    float userRating;
    String releaseDate;
    int id;

    public MovieObj(String name, String image, String story, float rate, String date,int id)
    {
        this.originalTitle = name;
        this.imageThumpAddr = image;
        this.plot = story;
        this.userRating = rate;
        this.releaseDate = date;
        this.id = id;
    }

    protected MovieObj(Parcel in) {
        originalTitle = in.readString();
        imageThumpAddr = in.readString();
        plot = in.readString();
        userRating = in.readFloat();
        releaseDate = in.readString();
        id = in.readInt();
    }

    public static final Creator<MovieObj> CREATOR = new Creator<MovieObj>() {
        @Override
        public MovieObj createFromParcel(Parcel in) {
            return new MovieObj(in);
        }

        @Override
        public MovieObj[] newArray(int size) {
            return new MovieObj[size];
        }
    };

    @Override
    public String toString() {
        return "Movie Name:" + originalTitle + ", ID = "+id ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(originalTitle);
        parcel.writeString(imageThumpAddr);
        parcel.writeString(plot);
        parcel.writeFloat(userRating);
        parcel.writeString(releaseDate);
        parcel.writeInt(id);
    }
}
