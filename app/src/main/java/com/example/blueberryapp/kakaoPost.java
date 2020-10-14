package com.example.blueberryapp;

import com.google.gson.annotations.SerializedName;
import com.google.type.DateTime;

public class kakaoPost {

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
                     String cancel_url) {
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
    }
}
