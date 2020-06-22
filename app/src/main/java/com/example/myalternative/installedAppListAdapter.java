package com.example.myalternative;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class installedAppListAdapter extends RecyclerView.Adapter<installedAppListAdapter.View_Holder> {
    List<App> mApps;
    Context mContext;
    View.OnClickListener mClickListener;
    RecyclerView rv;
    pojoApp[] altApps;
    showNoApps setShowNoAppsInterface;
    public installedAppListAdapter(List<App> apps, Context context, RecyclerView rv, pojoApp[] altApps, showNoApps setShowNoAppsInterface){
        this.mApps = apps;
        this.mContext = context;
        this.rv = rv;
        this.altApps = altApps;
        this.setShowNoAppsInterface = setShowNoAppsInterface;
    }

    public void setClickListener(View.OnClickListener callback) {
        mClickListener = callback;
    }
    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.installed_apps_card_layout, parent, false);
        final View_Holder viewHolder = new View_Holder(v);

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //card not expanded
                if(viewHolder.constraintLayout.getVisibility() == View.GONE){

                    TransitionManager.beginDelayedTransition(viewHolder.cv, new AutoTransition());
                    viewHolder.constraintLayout.setVisibility(View.VISIBLE);
                    viewHolder.expandBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_up_black_24dp, 0, 0, 0);
                }
                //card expanded
                else{
                    TransitionManager.beginDelayedTransition(rv, new AutoTransition());
                    viewHolder.constraintLayout.setVisibility(View.GONE);
                    viewHolder.expandBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_down_black_24dp, 0, 0, 0);
                }
            }
        });


        viewHolder.expandBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(viewHolder.constraintLayout.getVisibility() == View.GONE){
                    TransitionManager.beginDelayedTransition(viewHolder.cv, new AutoTransition());
                    viewHolder.constraintLayout.setVisibility(View.VISIBLE);
                    viewHolder.expandBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_up_black_24dp, 0, 0, 0);
                }
                else{
                    TransitionManager.beginDelayedTransition(rv, new AutoTransition());
                    viewHolder.constraintLayout.setVisibility(View.GONE);
                    viewHolder.expandBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_keyboard_arrow_down_black_24dp, 0, 0, 0);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, final int position) {

        holder.title.setText(mApps.get(position).appName);
        holder.description.setText(mApps.get(position).pname);
        holder.imageView.setImageDrawable(mApps.get(position).appIcon);
        List<App> altAppsList = new ArrayList<>();
        for(pojoApp p : altApps){
            if(mApps.get(position).pname.contains(p.packageName)){
                setShowNoAppsInterface.showNoAppsMethod();
                for(Alternative a : p.alternative){
                    altAppsList.add(new App(a.altName, "", a.link));
                }
            }
        }
        installedAppListAlternativeAdapter altAppsRVAdapter = new installedAppListAlternativeAdapter(altAppsList, mContext);
        holder.altAppRV.setAdapter(altAppsRVAdapter);
        holder.altAppRV.setLayoutManager(new LinearLayoutManager(mContext));
        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Open the specific App Info page:
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + mApps.get(position).pname));
                    mContext.startActivity(intent);

                } catch ( ActivityNotFoundException e ) {
                    //Open the generic Apps page:
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView description;
        ImageView imageView;
        ConstraintLayout constraintLayout;
        Button expandBtn;
        RecyclerView altAppRV;
        Button infoBtn;
        View_Holder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cardView);
            title = itemView.findViewById(R.id.appName);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.expandableView);
            expandBtn = itemView.findViewById(R.id.expandBtn);
            altAppRV = itemView.findViewById(R.id.altAppsRV);
            altAppRV.addItemDecoration(new DividerItemDecoration(altAppRV.getContext(), DividerItemDecoration.VERTICAL));
            infoBtn = itemView.findViewById(R.id.infoBtn);
        }
    }

    public interface showNoApps{
        void showNoAppsMethod();
    }
}
