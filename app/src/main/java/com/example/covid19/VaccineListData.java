package com.example.covid19;

public class VaccineListData {
    private String centreName;
    private String centreAddress;
    private String fee_type;
    private String amount;
    private String dose1;
    private String dose2;
    private String min_age_limit;
    private String vaccine_type;

    public VaccineListData(String centreName, String centreAddress, String fee_type, String amount, String dose1, String dose2, String min_age_limit, String vaccine_type) {
        this.centreName = centreName;
        this.centreAddress = centreAddress;
        this.fee_type = fee_type;
        this.amount = amount;
        this.dose1 = dose1;
        this.dose2 = dose2;
        this.min_age_limit = min_age_limit;
        this.vaccine_type = vaccine_type;
    }

    public String getCentreName() {
        return centreName;
    }

    public void setCentreName(String centreName) {
        this.centreName = centreName;
    }

    public String getCentreAddress() {
        return centreAddress;
    }

    public void setCentreAddress(String centreAddress) {
        this.centreAddress = centreAddress;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDose1() {
        return dose1;
    }

    public void setDose1(String dose1) {
        this.dose1 = dose1;
    }

    public String getDose2() {
        return dose2;
    }

    public void setDose2(String dose2) {
        this.dose2 = dose2;
    }

    public String getMin_age_limit() {
        return min_age_limit;
    }

    public void setMin_age_limit(String min_age_limit) {
        this.min_age_limit = min_age_limit;
    }

    public String getVaccine_type() {
        return vaccine_type;
    }

    public void setVaccine_type(String vaccine_type) {
        this.vaccine_type = vaccine_type;
    }
}
