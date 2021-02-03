package com.tokayoapp.Modal;

public class CompanyModal {

    String com_title;
    String com_id;
    String weight;
    String price;
    String delvfee_id;
    Integer type;

    public String getCom_id() {
        return com_id;
    }

    public void setCom_id(String com_id) {
        this.com_id = com_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDelvfee_id() {
        return delvfee_id;
    }

    public void setDelvfee_id(String delvfee_id) {
        this.delvfee_id = delvfee_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCom_title() {
        return com_title;
    }

    public void setCom_title(String com_title) {
        this.com_title = com_title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}