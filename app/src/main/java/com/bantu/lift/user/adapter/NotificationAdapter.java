package com.bantu.lift.user.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantu.lift.user.R;
import com.bantu.lift.user.modelclass.NotificationModel.Datum;
import com.bantu.lift.user.util.NotificationUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    List<Datum> data;
    public NotificationAdapter(Context context, List<Datum> data) {
        this.context = context;
        this.data=data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_adapter, viewGroup, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
      //  myViewHolder.natificationName.setText(data.get(i).getMessage());
        myViewHolder.startLocation.setText(data.get(i).getPickupLocation());
        myViewHolder.endLocation.setText(data.get(i).getDropLocation());
        myViewHolder.startdate.setText(NotificationUtils.parseDateToddMMyyyy(data.get(i).getStartDateTime()));
        myViewHolder.ProfileName.setText(data.get(i).getName());
        myViewHolder.price.setText("R "+data.get(i).getAmount());
        myViewHolder.passerger.setText(data.get(i).getSeats());
        myViewHolder.mobile.setText(data.get(i).getDriverContactNumber());
        myViewHolder.car_Name.setText(data.get(i).getCarName());
        myViewHolder.carNumber.setText(data.get(i).getCarNumber());
        myViewHolder.message.setText(data.get(i).getMessage());
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
        TextView price,  startLocation, endLocation, startdate;
        TextView car_Name,ProfileName,passerger,mobile,carNumber,message;
        Button acceptBtn;
        ImageView carImage,profile;
        Button rejectBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //natificationName=itemView.findViewById(R.id.natificationName);
            startLocation=itemView.findViewById(R.id.startLocation);
            endLocation=itemView.findViewById(R.id.endLocation);
            startdate=itemView.findViewById(R.id.startdate);
            profile=itemView.findViewById(R.id.profile);
            carImage=itemView.findViewById(R.id.carImage);
            car_Name=itemView.findViewById(R.id.car_Name);
            ProfileName=itemView.findViewById(R.id.ProfileName);
            mobile=itemView.findViewById(R.id.mobile);
            passerger=itemView.findViewById(R.id.passerger);
            price=itemView.findViewById(R.id.price);
            carNumber=itemView.findViewById(R.id.carNumber);
            message=itemView.findViewById(R.id.message);

        }
    }
}



