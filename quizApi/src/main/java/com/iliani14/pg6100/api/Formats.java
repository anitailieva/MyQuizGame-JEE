package com.iliani14.pg6100.api;

import javax.ws.rs.core.MediaType;

/**
 * Created by anitailieva on 08/11/2016.
 */
public interface Formats {
    String V1_JSON = MediaType.APPLICATION_JSON + "; charset=UTF-8; version=1";
    String V1_JSON_MERGE = "application/merge-patch+json; charset=UTF-8; version=1";
    String HAL_V1 = "application/hal+json; charset=UTF-8; version=1";
}
