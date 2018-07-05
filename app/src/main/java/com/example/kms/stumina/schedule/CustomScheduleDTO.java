package com.example.kms.stumina.schedule;

public class CustomScheduleDTO {
    private String rsch_date;
    private String rsch_name;
    private String sr_name;
    private String sr_location;
    private String rsch_idx;

    public String getRsch_idx() {
        return rsch_idx;
    }

    public void setRsch_idx(String rsch_idx) {
        this.rsch_idx = rsch_idx;
    }

    public String getRsch_date() {
        return rsch_date;
    }

    public void setRsch_date(String rsch_date) {
        this.rsch_date = rsch_date;
    }

    public String getRsch_name() {
        return rsch_name;
    }

    public void setRsch_name(String rsch_name) {
        this.rsch_name = rsch_name;
    }

    public String getSr_name() {
        return sr_name;
    }

    public void setSr_name(String sr_name) {
        this.sr_name = sr_name;
    }

    public String getSr_location() {
        return sr_location;
    }

    public void setSr_location(String sr_location) {
        this.sr_location = sr_location;
    }
}
