package com.bantu.lift.user.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantu.lift.user.R;
import com.bantu.lift.user.constant.CommonMeathod;
import com.bantu.lift.user.interFace.AdapterCallback;
import com.bantu.lift.user.modelclass.GetPollsModel.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PoolnearYouAdapter extends RecyclerView.Adapter<PoolnearYouAdapter.MyViewHolder> {
    Context context;
    List<Datum> data;
    AdapterCallback callback;

    public PoolnearYouAdapter(Context context, List<Datum> data, AdapterCallback callback) {
        this.context = context;
        this.data = data;
        this.callback = callback;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_poolnearyou, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (data.get(i).getStatus().equalsIgnoreCase("0")) {
            myViewHolder.requestBtn.setText("send request");
        } else {
            myViewHolder.requestBtn.setText("REQUESTED");

        }
        myViewHolder.carName.setText(data.get(i).getDriverName());
        myViewHolder.car_Name.setText(data.get(i).getCarName());
        myViewHolder.carNumber.setText(data.get(i).getCarNumber());
        myViewHolder.passerger.setText(data.get(i).getPassengers());
        myViewHolder.startdate.setText(CommonMeathod.parseDateToddMMyyyy(data.get(i).getStartDateTime()));
        myViewHolder.luggage.setText(data.get(i).getLuggage());
        myViewHolder.price.setText("R "+data.get(i).getAmount());
        //Smoking Status
        if(data.get(i).getSmoking().equalsIgnoreCase("1"))
        {
            myViewHolder.tvSmoking.setVisibility(View.VISIBLE);
            myViewHolder.tvSmokingDivider.setVisibility(View.VISIBLE);

        }else
        {
            myViewHolder.tvSmoking.setVisibility(View.GONE);
            myViewHolder.tvSmokingDivider.setVisibility(View.GONE);

        }


        if(TextUtils.isEmpty(data.get(i).getLuggage())||data.get(i).getLuggage().equalsIgnoreCase("Luggage"))
        {
            myViewHolder.luggage.setVisibility(View.GONE);
        }else
        {
            myViewHolder.luggage.setVisibility(View.VISIBLE);
        }


        myViewHolder.endLocation.setText(data.get(i).getDropAddress());
        myViewHolder.startLocation.setText(data.get(i).getPickupAddress());
        Picasso.get()
                .load(data.get(i).getCarPic())
                .placeholder(R.drawable.ic_carimg)
                .error(R.drawable.ic_carimg)
                .into(myViewHolder.carImage);

        Picasso.get()
                .load(data.get(i).getDriverPic())
                .placeholder(R.drawable.profile_round)
                .error(R.drawable.profile_round)
                .into(myViewHolder.profile);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView car_Name, luggage, passerger, carName, carNumber, endLocation, startLocation, startdate, price;
        ImageView carImage;
        CircleImageView profile;
        Button requestBtn;
        TextView tvSmoking;
        TextView tvExpectedAmount;
        TextView tvSmokingDivider;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            carName = itemView.findViewById(R.id.carName);
            carNumber = itemView.findViewById(R.id.carNumber);
            car_Name = itemView.findViewById(R.id.car_Name);
            passerger = itemView.findViewById(R.id.passerger);
            price = itemView.findViewById(R.id.price);
            startdate = itemView.findViewById(R.id.startdate);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            luggage = itemView.findViewById(R.id.luggage);
            carImage = itemView.findViewById(R.id.carImage);
            requestBtn = itemView.findViewById(R.id.requestBtn);
            profile = itemView.findViewById(R.id.profile);
            tvSmoking=itemView.findViewById(R.id.tv_smoking);
            tvSmokingDivider=itemView.findViewById(R.id.tv_smoking_divider);
            requestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        if (data.get(getLayoutPosition()).getStatus().equalsIgnoreCase("0"))
                            callback.onItemClicked(data.get(getLayoutPosition()).getPollId(), data.get(getLayoutPosition()).getDriverId(),getLayoutPosition());
                    }
                }
            });
        }
    }
}
