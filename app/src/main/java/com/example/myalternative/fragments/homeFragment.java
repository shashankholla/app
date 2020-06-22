package com.example.myalternative.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.myalternative.AltApp;
import com.example.myalternative.AsyncResponse;
import com.example.myalternative.R;
import com.example.myalternative.homeListAdapter;
import com.example.myalternative.pojoApp;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class homeFragment extends Fragment implements AsyncResponse {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    JsonTask asyncTask = new JsonTask();
    RecyclerView recyclerView;
    ViewGroup rootView;

    public homeFragment() {
    }


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
        recyclerView = rootView.findViewById(R.id.recyclerview);
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
                url = new URL(getString(R.string.suggestedListJsonUrl));
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8), 8);
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
            while(!hasNetwork(getContext()));
            return true;
        }
        protected void onPostExecute(Boolean result) {
          startNetworkTask();
        }
    }

    public boolean hasNetwork(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
