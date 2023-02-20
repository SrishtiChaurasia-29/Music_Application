package com.example.musicapplication;

import android.net.Uri;

public class musiclist {
    private String title,artist,duration;
    private boolean isplaying;
    private Uri musicFile;

    public musiclist(String title, String artist, String duration, boolean isplaying,Uri musicFile) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.isplaying = isplaying;
        this.musicFile=musicFile;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getDuration() {
        return duration;
    }

    public boolean isIsplaying() {
        return isplaying;
    }

    public Uri getMusicFile() {
        return musicFile;
    }

    public void setIsplaying(boolean isplaying) {
        this.isplaying = isplaying;
    }
}
