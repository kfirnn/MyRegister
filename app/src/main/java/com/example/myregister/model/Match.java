package com.example.myregister.model;

public class Match {
    String id,date,hour,city, stadium,rivals,result;

    public Match(String id, String date, String hour, String city, String stadium, String rivals, String result) {
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.city = city;
        this.stadium = stadium;
        this.rivals = rivals;
        this.result = result;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getRivals() {
        return rivals;
    }

    public void setRivals(String rivals) {
        this.rivals = rivals;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    @Override
    public String toString() {
        return "Match{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", city='" + city + '\'' +
                ", stadium='" + stadium + '\'' +
                ", rivals='" + rivals + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}



