package com.example.harshit.twopane_blank.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harshit.twopane_blank.R;
import com.example.harshit.twopane_blank.adapter.PosterViewAdapter;
import com.example.harshit.twopane_blank.model.PosterView;
import com.example.harshit.twopane_blank.model.Result;
import com.example.harshit.twopane_blank.utils.Constants;
import com.example.harshit.twopane_blank.utils.MyDBHandler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by harshit on 29/12/15.
 */
public class MovieList extends Fragment {
private boolean mIsDualPane;
    OnMovieSelectedListener mMovieSelectedListener=null;


    private String _sortBy;
    private String sortOrd = "desc";
    private TextView msg;
    private GridView gv;
    private Context c;
    private PosterView output;
    private ImageView imgBtn;
    private PosterViewAdapter adap;
    private MyDBHandler dbH;
    //String ketForFavSort="fav";
    private List<Result> favPosters;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private void showToast(String msg){
        Toast.makeText(c,msg, Toast.LENGTH_SHORT).show();
    }

    public void setMovieSelectedListener(OnMovieSelectedListener listener){
        mMovieSelectedListener=listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mIsDualPane = getResources().getInteger(R.integer.weightOfRight)==1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.movie_grid, container,
                false);
        dbH=new MyDBHandler(getActivity());
        msg = (TextView) v.findViewById(R.id.connectivityPopup);
        gv = (GridView) v.findViewById(R.id.grid_keeper);
        imgBtn = (ImageView) v.findViewById(R.id.menubtn);
        c = getActivity();
//        registerForContextMenu(imgBtn);



        if (isConnected()) {
            msg.setVisibility(View.GONE);
            //loadGrid();
        } else {
            msg.setVisibility(View.VISIBLE);
        }
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("FavLoad", position + ":" + id);
                Result m;

                m=output.getResults().get(position);

                if(null!=mMovieSelectedListener)
                {
                    mMovieSelectedListener.onMovieSeleted(m);
                }
                else{
                    Log.e("FavLoad","Listener NOT init");
                }

            }
        });
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    public void loadGrid(String sortBy) {
        _sortBy=sortBy;
        if(null!=adap && null!=gv){
        adap.notifyDataSetInvalidated();
        gv.invalidateViews();}

        if(!sortBy.equals(Constants.ketForFavSort))
        {
            Map<String, String> reqMap = new HashMap<>();
            reqMap.put("api_key", Constants.movieDbkey);
            reqMap.put("sort_by", sortBy + "." + sortOrd);
            Date now = new Date();
            String strDate = sdf.format(now);
            String url = getResources().getString(R.string.discoverMovie);
            url += Constants.getQueryParamFromMap(reqMap);
            url += "&release_date.lte=" + strDate + "&language=en";
            Log.e("Movie Central", url);
            new HttpAsyncTask().execute(url);
            return;
        }
        else{
            PosterView temp=new PosterView();
            output.setResults(dbH.getFavMovies());
            favPosters=output.getResults();
            int i=0;
            for(Result poster:favPosters)
            {
                Log.e("FavLoad",i+":"+poster.getId()+":"+poster.getTitle());
            }
            temp.setResults(favPosters);

            populateGrid(temp);
            return;
        }
        //RequestModel request=new RequestModel("loadGrid",getResources().getString(R.string.discoverMovie),reqMap,null);

    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            inputStream.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Gson g = new Gson();
            output = g.fromJson(result, PosterView.class);
            populateGrid(output);
        }
    }

    void populateGrid(PosterView data) {
        if(null!=data){
        adap = new PosterViewAdapter(c, data);
        gv.setAdapter(adap);
        }
        else{
            showToast(
                    "Null Object returned, Check network connection"
            );
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.sharedPrefKey, 0);
        _sortBy=sharedPreferences.getString(Constants.settingKeyForSort,"release_date");

        loadGrid(_sortBy);
        Log.e("resume", "Activity resumed");
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.sharedPrefKey,0);
        SharedPreferences.Editor keeper=sharedPreferences.edit();
        keeper.putString(Constants.settingKeyForSort,_sortBy);
        keeper.commit();
    }
}
