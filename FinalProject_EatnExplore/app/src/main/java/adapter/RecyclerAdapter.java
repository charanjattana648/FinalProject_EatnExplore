package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.guri.eatnexplorerestaurantfinder.R;

import java.util.List;

import bean.Item;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.BusinessViewHolder>{
    List<Item> businessList;
    Context ctx;
    RecyclerView recyclerView;


    public RecyclerAdapter(List<Item> businessList, Context ctx, RecyclerView recyclerView) {
        this.businessList = businessList;
        this.ctx = ctx;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);

       // view.setOnClickListener(onClickListener);
        return new BusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessViewHolder businessViewHolder, int i) {
        businessViewHolder.txtName.setText(businessList.get(i).getName());
        businessViewHolder.txtPhone.setText(businessList.get(i).getPhone());
        businessViewHolder.txtAvgRating.setText(businessList.get(i).getAvgRating());
        businessViewHolder.txtReviewCount.setText(businessList.get(i).getReviewsCount());
        businessViewHolder.txtAddress.setText(businessList.get(i).getAddress());

    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    public static class BusinessViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtAvgRating, txtReviewCount, txtAddress, txtPhone;
        public BusinessViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtAvgRating = (TextView) view.findViewById(R.id.txtAvgRating);
            txtReviewCount = (TextView) view.findViewById(R.id.txtReviewCount);
            txtPhone = (TextView) view.findViewById(R.id.txtPhone);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);


        }
    }
}
