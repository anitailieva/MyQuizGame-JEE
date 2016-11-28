package com.iliani14.pg6100.dto.hal;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by anitailieva on 29/11/2016.
 */
public class HalObject {

    @ApiModelProperty("HAL links")
    public HalLinkSet _links;
}
