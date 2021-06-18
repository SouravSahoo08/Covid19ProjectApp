package com.example.covid19;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {
    private CountryListData[] listdata;

    public CountryListAdapter(CountryListData[] listdata) {
        this.listdata = listdata;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.tracking_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CountryListData myListData = listdata[position];
        holder.cname.setText(listdata[position].getCountryName());
        holder.stat.setText(listdata[position].getStats());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "click on item: " + myListData.getCountryName(), Toast.LENGTH_LONG).show();
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
            this.cname = (TextView) itemView.findViewById(R.id.countryName);
            this.stat = (TextView) itemView.findViewById(R.id.stats);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);

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
