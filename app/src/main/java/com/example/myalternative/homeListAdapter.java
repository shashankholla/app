package com.example.myalternative;


import android.content.Context;
import android.content.Intent;

import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

public class homeListAdapter extends RecyclerView.Adapter<homeListAdapter.View_Holder> {
    List<AltApp> mApps;
    Context mContext;
    View.OnClickListener mClickListener;
    RecyclerView rv;
    pojoApp[] altApps;

    public homeListAdapter(List<AltApp> apps, Context context, RecyclerView rv, pojoApp[] altApps){
        this.mApps = apps;
        this.mContext = context;
        this.rv = rv;
        this.altApps = altApps;
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }
    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        final View_Holder viewHolder = new View_Holder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, final int position) {
        final AltApp a = mApps.get(position);
        holder.theApp.setText(a.theApp);
        holder.altApp.setText(a.altApp);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse((a.altAppLink))));
            }
        });
        Glide.with(mContext).load(a.theAppIcon).placeholder(R.drawable.ic_apps_black_24dp).into(holder.theAppIcon);
        Glide.with(mContext).load(a.altAppIcon).placeholder(R.drawable.ic_apps_black_24dp).into(holder.altAppIcon);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {

       TextView theApp;
       TextView altApp;
       ImageView theAppIcon;
       ImageView altAppIcon;
       Button btn;
        View_Holder(View itemView) {
            super(itemView);
            theApp = itemView.findViewById(R.id.theAppName);
            altApp = itemView.findViewById(R.id.altAppName);
            theAppIcon = itemView.findViewById(R.id.theAppImage);
            altAppIcon = itemView.findViewById(R.id.altAppImg);
            btn = itemView.findViewById(R.id.button1);
        }
    }
}
