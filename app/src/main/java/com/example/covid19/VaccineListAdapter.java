package com.example.covid19;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VaccineListAdapter extends RecyclerView.Adapter<VaccineListAdapter.VaxViewHolder> {
    private final VaccineListData[] listData;
    private Context mContext;

    public VaccineListAdapter(ArrayList<VaccineListData> listData) {
        this.listData = listData.toArray(new VaccineListData[0]);
    }

    @NonNull
    @Override
    public VaxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.vaccine_cardlayout, parent, false);
        return new VaxViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VaxViewHolder holder, int position) {
        final VaccineListData myListData = listData[position];
        holder.center.setText(listData[position].getCentreName());
        holder.cent_add.setText(listData[position].getCentreAddress());
        holder.minAge.setText(listData[position].getMin_age_limit());
        holder.vacType.setText(listData[position].getVaccine_type());
        holder.fee.setText(listData[position].getFee_type());
        holder.firstDose.setText(listData[position].getDose1());
        holder.secondDose.setText(listData[position].getDose2());
        if (listData[position].getDose1().equals("0")) {
            holder.firstDose.setBackgroundColor(0xBEFF0A0A);
        } else {
            holder.firstDose.setBackgroundColor(0xCE59F605);
            holder.firstDose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cowin.gov.in/home"));
                    mContext.startActivity(browserIntent);
                }
            });
        }
        if (listData[position].getDose2().equals("0")) {
            holder.secondDose.setBackgroundColor(0xBEFF0A0A);
        } else {
            holder.secondDose.setBackgroundColor(0xCE59F605);
            holder.secondDose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cowin.gov.in/home"));
                    mContext.startActivity(browserIntent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

    public static class VaxViewHolder extends RecyclerView.ViewHolder {
        public TextView center;
        public TextView cent_add;
        public TextView minAge;
        public TextView vacType;
        public TextView fee;
        public TextView firstDose;
        public TextView secondDose;
        public RelativeLayout relativeLayout;

        public VaxViewHolder(View itemView) {
            super(itemView);
            this.center = itemView.findViewById(R.id.centreName);
            this.cent_add = itemView.findViewById(R.id.centreAddress);
            this.minAge = itemView.findViewById(R.id.min_age_limit);
            this.vacType = itemView.findViewById(R.id.vaccine_type);
            this.fee = itemView.findViewById(R.id.fee_type);
            this.firstDose = itemView.findViewById(R.id.dose1);
            this.secondDose = itemView.findViewById(R.id.dose2);
            relativeLayout = itemView.findViewById(R.id.Vaccine_recyclerView);

            //animateRelativeLayout();
        }
    }
}
