package com.iliani14.pg6100.dto.hal;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by anitailieva on 29/11/2016.
 */
public class HalLink {

    @ApiModelProperty("URL of the link")
    public String href;

    public HalLink(){}

    public HalLink(String href) {
        this.href = href;
    }
}
