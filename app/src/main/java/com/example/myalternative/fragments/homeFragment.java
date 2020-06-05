package com.example.myalternative.fragments;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myalternative.AltApp;
import com.example.myalternative.App;
import com.example.myalternative.AsyncResponse;


import com.example.myalternative.R;
import com.example.myalternative.homeListAdapter;
import com.example.myalternative.installedAppListAdapter;
import com.example.myalternative.pojoApp;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link appList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeFragment extends Fragment implements AsyncResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tv;
    List<ResolveInfo> pkgAppsList;
    List<App> myApps = new ArrayList<App>();
    JsonTask asyncTask = new JsonTask();
    RecyclerView recyclerView;
    ViewGroup rootView;
    ProgressDialog progressDialog;

    public homeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment appList.
     */
    // TODO: Rename and change types and number of parameters
    public static appList newInstance(String param1, String param2) {
        appList fragment = new appList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public void processFinish(String output) {
        Gson gson = new Gson();
        pojoApp[] jsonObject = gson.fromJson(output, pojoApp[].class);
        List<AltApp> visibleApps = new ArrayList<AltApp>();
        for (pojoApp p : jsonObject) {
            visibleApps.add(new AltApp(p.name, p.altAppName(), p.altAppLink(), p.appIcon, p.altAppIcon()));
        }
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        homeListAdapter adapter = new homeListAdapter(visibleApps, getActivity(), recyclerView, jsonObject);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    private class JsonTask extends AsyncTask<String, String, String> {
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            HttpURLConnection connection = null;
            URL url;
            try {
                url = new URL("https://raw.githubusercontent.com/shashankholla/app/master/suggestedApps.json");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();
                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }
                connection.disconnect();
                result = sBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
            sharedPreferencesEditor.putString(
                    "homeList", s);
            sharedPreferencesEditor.apply();
            delegate.processFinish(s);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_app_list, container, false);

        return rootView;
    }

    public void startNetworkTask(){
        asyncTask.delegate =this;
        asyncTask.execute();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(hasNetwork(getContext())){
            asyncTask.delegate =this;
            asyncTask.execute();

        } else{
            String s;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            s = sharedPreferences.getString("homeList","");
            if(s == ""){
                CheckOnlineStatus c = new CheckOnlineStatus();
                c.execute();

            } else {
                processFinish(s);
            }
        }

    }

    private class CheckOnlineStatus extends AsyncTask<Void, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            //This is a background thread, when it finishes executing will return the result from your function.
            while(!hasNetwork(getContext()));
            return true;
        }

        protected void onPostExecute(Boolean result) {
          startNetworkTask();
        }
    }

    public boolean hasNetwork(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()){
            return true;
        }

        return false;
    }
}
