package com.iliani14.pg6100.dto.hal;

import io.swagger.annotations.ApiModelProperty;

/*
    Usually a HAL JSON for a resource should have a "self"
    link to itself
 */
public class HalLinkSet {

    @ApiModelProperty("Link to the current resource")
    public HalLink self;
}
