package com.example.guri.eatnexplorerestaurantfinder;




        import android.content.Context;
        import android.support.annotation.LayoutRes;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.RatingBar;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;


public class ReviewListAdapter extends ArrayAdapter<Review> {

    private Context mContext;
    private List<Review> reviewList = new ArrayList<>();

    public ReviewListAdapter(@NonNull Context context, ArrayList<Review> list) {
        super(context, 0 , list);
        mContext = context;
        reviewList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        TextView review,noReview,nametxt,dateTxt;
        RatingBar ratingBar;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.review_list, parent, false);
        }
        review = (TextView) listItem.findViewById(R.id.textReview);
        ratingBar = (RatingBar) listItem.findViewById(R.id.ratingBarList);
        nametxt=(TextView)listItem.findViewById(R.id.name_txt);
        dateTxt=(TextView) listItem.findViewById(R.id.dateTxt);

        if(reviewList.size()>0) {

            Review currentReview = reviewList.get(position);

            Log.d("", "getView: "+currentReview.toString()+" -- "+currentReview.getStars()+"--"+currentReview.getReview());

            review.setText(currentReview.getReview());

            if(currentReview.getUserName()!=null && !currentReview.getUserName().isEmpty())
            {
                nametxt.setText(currentReview.getUserName());

            }else {
                nametxt.setText("anonymous user");
            }

            dateTxt.setText(currentReview.getReviewDate());
            ratingBar.setRating(currentReview.getStars());
        }

        return listItem;
    }
}
