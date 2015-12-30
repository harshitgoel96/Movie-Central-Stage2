package com.example.harshit.twopane_blank.utils;

import java.util.Map;
import java.util.Set;

/**
 * Created by harsh on 12/24/2015.
 */
public class Constants {
    public static final String movieDbkey="REMOVED";
    public static String getQueryParamFromMap(Map<String,String> map){
        Set<String> keySet=map.keySet();
        StringBuilder strB=new StringBuilder("?");
        for (String key: keySet
             ) {
            strB.append(key);
            strB.append("=");
            strB.append(map.get(key));
            strB.append("&");
        }
        strB=strB.deleteCharAt(strB.length()-1);
        return strB.toString();
    }
    public static final String imageBase="http://image.tmdb.org/t/p/w185/";
    private static final String yotubeImgUnf="http://img.youtube.com/vi/%s/0.jpg";
    private static final String youtubeVideoLink="http://www.youtube.com/watch?v=";
    private static final String vidLinkUnf="http://api.themoviedb.org/3/movie/%s/videos?api_key=%s";
    private static final String revLink="http://api.themoviedb.org/3/movie/%s/reviews?api_key=%s";
    public static final String keyName="data";
    public static String getYoutubeVideoLink(String vId){
        return youtubeVideoLink+vId;
    }
    public static String getYoutubeVideoImage(String vId){
        return String.format(yotubeImgUnf,vId);
    }
    public static String getVideoLink(String id){
        return String.format(vidLinkUnf,id,movieDbkey);
    }
    public static String getReviewLink(String id){
        return String.format(revLink,id,movieDbkey);
    }
    public static final String DATABASE_NAME="Movie-Central.db";
    public static final int DATABASE_VERSION=1;
    public static String[] fav_mov_cols={"adult",
            "video",
            "popularity",
            "voteAverage",
            "id",
            "voteCount",
            "backdropPath",
            "posterPath",
            "originalLanguage",
            "originalTitle",
            "overview",
            "releaseDate",
            "title"};
    public static String fam_mov_sql="CREATE TABLE IF NOT EXISTS 'fav_move'( " +
            "'adult' NUMERIC, " +
            "'video' NUMERIC, " +
            "'popularity' real, " +
            "'voteAverage' real, " +
            "'id' Integer, " +
            "'voteCount' Integer, " +
            "'backdropPath' text, " +
            "'posterPath' text, " +
            "'originalLanguage' text, " +
            "'originalTitle' text, " +
            "'overview' text, " +
            "'releaseDate' text, " +
            "'title' text " +
            ")";

    public static final String ketForFavSort="fav";
}
