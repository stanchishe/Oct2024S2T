package org.example;

public class Transcript {
    private String audio_url;
    private String id;
    private String status;
    private String text;
    private String language_code;
    private  String audio_duration;

    public String getAudio_duration() {
        return audio_duration;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public String getId() {
        return id;
    }

    public String getAudio_url() {
        return audio_url;
    }

    public String getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    public void setAudio_url(String audio_url) {
        this.audio_url = audio_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setText(String text) {
        this.text = text;
    }
}
