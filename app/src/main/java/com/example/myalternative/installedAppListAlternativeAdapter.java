package com.example.myalternative;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class installedAppListAlternativeAdapter extends RecyclerView.Adapter<installedAppListAlternativeAdapter.VH> {
    List<App> mApps;
    Context mContext;

    public installedAppListAlternativeAdapter(List<App> apps, Context context){
        this.mApps = apps;
        this.mContext = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_app_rv, parent, false);
        final VH viewHolder = new VH(v);
        viewHolder.altAppRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mApps.get(viewHolder.getAdapterPosition()).appLink)));
            }
        });
        viewHolder.launchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mApps.get(viewHolder.getAdapterPosition()).appLink)));
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.title.setText(mApps.get(position).appName);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title;
        Button launchBtn;
        RelativeLayout altAppRel;
        VH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.altAppName);
            launchBtn = itemView.findViewById(R.id.launchPlaystore);
            altAppRel = itemView.findViewById(R.id.altAppRel);
        }
    }
}
