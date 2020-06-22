package com.example.myalternative.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myalternative.App;
import com.example.myalternative.AsyncResponse;
import com.example.myalternative.MainActivity;
import com.example.myalternative.PInfo;
import com.example.myalternative.R;
import com.example.myalternative.installedAppListAdapter;
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
import java.util.Collections;
import java.util.List;

public class appList extends Fragment implements AsyncResponse, installedAppListAdapter.showNoApps {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    AlertDialog dialog;
    private String mParam1;
    private String mParam2;
    List<App> myApps = new ArrayList<App>();
    JsonTask asyncTask = new JsonTask();
    RecyclerView recyclerView;
    ViewGroup rootView;
    ProgressDialog progressDialog;
    TextView noApps;

    public appList() {
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
        progressDialog = ProgressDialog.show(getActivity(), getString(R.string.loadingProgressDialogTitle), getString(R.string.loadingProgressDialogDesc), true);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<PInfo> getPackages() {
        ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */

        return apps;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (!(getSysPackages) && (p.versionName == null)) {
                continue;
            }

            PInfo newInfo = new PInfo();
            newInfo.appname = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.icon = p.applicationInfo.loadIcon(getActivity().getPackageManager());
            if (!newInfo.pname.startsWith("com.android") && !newInfo.pname.startsWith("com.google"))
                res.add(newInfo);
        }
        Collections.sort(res);
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void processFinish(String output) {


        ArrayList<PInfo> packages = getPackages();
        for (PInfo p : packages) {

                myApps.add(new App(p.appname, p.pname, "", p.icon));
        }
        Gson gson = new Gson();
        pojoApp[] jsonObject = gson.fromJson(output, pojoApp[].class);
        List<App> visibleApps = new ArrayList<App>();
        for (App app : myApps) {
            for (pojoApp p : jsonObject) {
                if (app.pname.contains(p.packageName)) {
                    visibleApps.add(app);
                }
            }

        }
        recyclerView = rootView.findViewById(R.id.recyclerview);
        installedAppListAdapter adapter = new installedAppListAdapter(visibleApps, getActivity(), recyclerView, jsonObject, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressDialog.dismiss();
    }

    public boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void showNoAppsMethod() {
             noApps.setVisibility(View.GONE);
     }

    private class JsonTask extends AsyncTask<String, String, String> {
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(String... strings) {

            String result = null;
            HttpURLConnection connection = null;
            URL url;
            try {
                url = new URL(getString(R.string.appListJsonUrl));
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
            sharedPreferencesEditor.putString("appList", s);
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
        noApps = rootView.findViewById(R.id.noapps);
        return rootView;
    }

    public void startNetworkTask() {
        asyncTask.delegate = this;
        asyncTask.execute();
    }

    private class CheckOnlineStatus extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            //This is a background thread, when it finishes executing will return the result from your function.
            while (!hasNetwork(getContext())) ;
            return true;
        }

        protected void onPostExecute(Boolean result) {

            dialog.dismiss();
            startNetworkTask();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        builder.setTitle("ERROR");
        builder.setMessage("Network Unavailable!");
        builder.setPositiveButton("Retry Now", null);
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (hasNetwork(getContext())) {
                            dialog.dismiss();
                            startNetworkTask();
                        } else {
                            Toast.makeText(getContext(), "Network still not available!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (hasNetwork(getContext())) {
            startNetworkTask();

        } else {
            String s;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            s = sharedPreferences.getString("appList", "");
            if (s == "") {
                dialog.show();
                CheckOnlineStatus c = new CheckOnlineStatus();
                c.execute();
                Toast.makeText(getContext(), "Network Unavailable!", Toast.LENGTH_LONG).show();


            } else {
                processFinish(s);
            }
        }
    }
}
