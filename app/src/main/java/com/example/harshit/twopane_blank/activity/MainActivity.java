package com.example.harshit.twopane_blank.activity;

import android.app.ActionBar;
import android.content.Intent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.harshit.twopane_blank.R;
import com.example.harshit.twopane_blank.model.Result;
import com.example.harshit.twopane_blank.utils.Constants;
import com.example.harshit.twopane_blank.utils.MyDBHandler;
import com.google.gson.Gson;

public class MainActivity extends FragmentActivity implements OnMovieSelectedListener {
    boolean mIsDualPane;
    private MyDBHandler dbH;
    ImageView imgBtn;

    MovieList gridFrag;
    View gridFragV;
    View detailFragV;
    String sortBy="release_date";
    private MovieDetail detailFrm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        dbH=new MyDBHandler(this);

        imgBtn=(ImageView)findViewById(R.id.menubtn);
        gridFragV= ((View)findViewById(R.id.gird_view));
        detailFragV= ((View)findViewById(R.id.detail_view));
        registerForContextMenu(imgBtn);
        imgBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        openContextMenu(imgBtn);
                    }
                }
        );
        mIsDualPane = getResources().getInteger(R.integer.weightOfRight)==1;
        Log.e("DUAL", String.valueOf(mIsDualPane));
       if(mIsDualPane)
       {
           detailFragV.setVisibility(View.VISIBLE);
       }
        FragmentManager fragmentManager=getSupportFragmentManager();
        gridFrag= (MovieList)fragmentManager.findFragmentById(R.id.gird_view);
        gridFrag.setMovieSelectedListener(this);
        gridFrag.loadGrid("release_date");
        detailFrm=(MovieDetail)fragmentManager.findFragmentById(R.id.detail_view);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menues, menu);
        MenuItem mI= menu.findItem(R.id.listFav);
        if(dbH.count()<1){

            mI.setVisible(false);

        }
        else{
            mI.setVisible(true);
        }


    }
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle item selection

        //return false;

        switch (item.getItemId()) {
            case R.id.sortByRelease:
                //newGame();
                if (!sortBy.equals("release_date")) {

                    sortBy = "release_date";
                    gridFrag.loadGrid(sortBy);
                }
                else{
                    showToast("Already Sorted");
                }
                return true;
            case R.id.sortByRating:
                //showHelp();
                if (!sortBy.equals("vote_average")) {

                    sortBy = "vote_average";
                    gridFrag.loadGrid(sortBy);
                }
                else{
                    showToast("Already Sorted");
                }
                return true;
            case R.id.sortByPopularity:
                if (!sortBy.equals("popularity")) {

                    sortBy = "popularity";
                    gridFrag.loadGrid(sortBy);
                }else{
                    showToast("Already Sorted");
                }
                return true;
            case R.id.listFav:
                if (!sortBy.equals(Constants.ketForFavSort)) {

                    sortBy = "fav";
                    gridFrag.loadGrid(sortBy);
                }else{
                    showToast("Already Sorted");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMovieSeleted(Result object) {
        detailFragV.setVisibility(View.VISIBLE);
        Log.e("MainActivity","Listen Success");
        if(!mIsDualPane) {
            LinearLayout.LayoutParams layoutParamOfDet=(LinearLayout.LayoutParams)detailFragV.getLayoutParams();
            LinearLayout.LayoutParams layoutParamsOfLis=(LinearLayout.LayoutParams)gridFragV.getLayoutParams();
            layoutParamOfDet.weight=1f;
            layoutParamsOfLis.weight=0f;
            gridFragV.setLayoutParams(layoutParamsOfLis);
            detailFragV.setLayoutParams(layoutParamOfDet);
            detailFrm.loadDetails(object);
        }
        else{
            detailFrm.loadDetails(object);
        }
    }

    @Override
    public void onBackPressed (){
        if(!mIsDualPane){
            LinearLayout.LayoutParams layoutParamOfDet=(LinearLayout.LayoutParams)detailFragV.getLayoutParams();
            LinearLayout.LayoutParams layoutParamsOfLis=(LinearLayout.LayoutParams)gridFragV.getLayoutParams();
            if(layoutParamOfDet.weight==1f){
                layoutParamOfDet.weight=0f;
                detailFragV.setLayoutParams(layoutParamOfDet);
                layoutParamsOfLis.weight=1f;
                gridFragV.setLayoutParams(layoutParamsOfLis);
                return;
            }
        }
        super.onBackPressed();
    }
}
