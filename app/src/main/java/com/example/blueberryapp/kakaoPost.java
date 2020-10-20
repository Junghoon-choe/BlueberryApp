package com.example.blueberryapp;

import com.google.gson.annotations.SerializedName;
import com.google.type.DateTime;

public class kakaoPost {

    public kakaoPost string;
    @SerializedName("body")

    //POST
    private String cid;

    private String partner_order_id;

    private String partner_user_id;

    private String item_name;

    private Integer quantity;

    private Integer total_amount;

    private Integer vat_amount;

    private Integer tax_free_amount;

    private String approval_url;

    private String fail_url;

    private String cancel_url;

//    //GET
    private String tid;

    private String next_redirect_app_url;

    private String next_redirect_mobile_url;

    private String next_redirect_pc_url;

    private String android_app_scheme;

    private String ios_app_scheme;

    private DateTime created_at;


    public kakaoPost(String cid,
                     String partner_order_id,
                     String partner_user_id,
                     String item_name,
                     Integer quantity,
                     Integer total_amount,
                     Integer vat_amount,
                     Integer tax_free_amount,
                     String approval_url,
                     String fail_url,
                     String cancel_url,
                     String tid,
                     String next_redirect_app_url,
                     String next_redirect_mobile_url,
                     String next_redirect_pc_url,
                     String android_app_scheme,
                     String ios_app_scheme,
                     DateTime created_at
    ){
        this.cid = cid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.item_name = item_name;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.vat_amount = vat_amount;
        this.tax_free_amount = tax_free_amount;
        this.approval_url = approval_url;
        this.fail_url = fail_url;
        this.cancel_url = cancel_url;
        this.tid = tid;
        this.next_redirect_app_url = next_redirect_app_url;
        this.next_redirect_mobile_url = next_redirect_mobile_url;
        this.next_redirect_pc_url = next_redirect_pc_url;
        this.android_app_scheme = android_app_scheme;
        this.ios_app_scheme = ios_app_scheme;
        this.created_at = created_at;
    }

    public String getCid() {
        return cid;
    }

    public String getPartner_order_id() {
        return partner_order_id;
    }

    public String getPartner_user_id() {
        return partner_user_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public Integer getVat_amount() {
        return vat_amount;
    }

    public Integer getTax_free_amount() {
        return tax_free_amount;
    }

    public String getApproval_url() {
        return approval_url;
    }

    public String getFail_url() {
        return fail_url;
    }

    public String getCancel_url() {
        return cancel_url;
    }



    public String getTid() {
        return tid;
    }

    public String getNext_redirect_app_url() {
        return next_redirect_app_url;
    }

    public String getNext_redirect_mobile_url() {
        return next_redirect_mobile_url;
    }

    public String getNext_redirect_pc_url() {
        return next_redirect_pc_url;
    }

    public String getAndroid_app_scheme() {
        return android_app_scheme;
    }

    public String getIos_app_scheme() {
        return ios_app_scheme;
    }

    public DateTime getCreated_at() {
        return created_at;
    }
}
