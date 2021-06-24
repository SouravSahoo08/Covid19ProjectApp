package com.example.covid19;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {
    private final CountryListData[] listdata;
    Dialog dialog;
    private Context mContext;

    public CountryListAdapter(Context mContext, ArrayList<CountryListData> listdata) {
        this.listdata = listdata.toArray(new CountryListData[0]);
        //this.mContext = mContext;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.tracking_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CountryListData myListData = listdata[position];
        holder.cname.setText(listdata[position].getCountryName());
        holder.stat.setText(listdata[position].getNo_of_cases());
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.custom_dialogbox);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView heading = dialog.findViewById(R.id.heading);
                TextView population = dialog.findViewById(R.id.populationText);
                TextView cases = dialog.findViewById(R.id.casesText);
                TextView active = dialog.findViewById(R.id.activeCasesText);
                TextView recovered = dialog.findViewById(R.id.recoveredText);
                TextView critical = dialog.findViewById(R.id.criticaltext);
                TextView death = dialog.findViewById(R.id.deathText);
                TextView affectedCount = dialog.findViewById(R.id.affect_countriesText);

                heading.setText(myListData.getCountryName());
                population.setText(myListData.getPopulation());
                cases.setText(myListData.getNo_of_cases());
                active.setText(myListData.getNo_of_active_cases());
                recovered.setText(myListData.getRecovered_cases());
                critical.setText(myListData.getCritical_cases());
                death.setText(myListData.getDeaths());
                if (myListData.getCountryName().equals("World")) {
                    affectedCount.setVisibility(View.VISIBLE);
                    dialog.findViewById(R.id.affTxt).setVisibility(View.VISIBLE);
                    affectedCount.setText(myListData.getAffected_countries());
                } else {
                    affectedCount.setVisibility(View.INVISIBLE);
                    dialog.findViewById(R.id.affTxt).setVisibility(View.INVISIBLE);
                }
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cname;
        public TextView stat;
        public RelativeLayout relativeLayout;
        private ObjectAnimator yAnimation;
        private ObjectAnimator alphaAnimation;
        private AnimatorSet set;

        public ViewHolder(View itemView) {
            super(itemView);
            this.cname = itemView.findViewById(R.id.countryName);
            this.stat = itemView.findViewById(R.id.no_of_cases);
            relativeLayout = itemView.findViewById(R.id.relativeLayout1);

            //animateRelativeLayout();
        }

        final long duration = 3000;

        private void animateRelativeLayout() {
            yAnimation = ObjectAnimator.ofFloat(relativeLayout, View.TRANSLATION_Y, 100f, 0f);
            yAnimation.setDuration(duration);
            alphaAnimation = ObjectAnimator.ofFloat(relativeLayout, View.ALPHA, 0.0f, 1.0f);
            alphaAnimation.setDuration(duration);
            set = new AnimatorSet();
            set.playTogether(yAnimation, alphaAnimation);
            set.start();
        }
    }
}
