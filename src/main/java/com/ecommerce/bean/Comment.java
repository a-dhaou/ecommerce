package com.ecommerce.bean;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Comment implements Bean {

    private long id;
    private String comment;
    private byte status;
    private Date addDate;
    private User user;
    private Item item;
}
