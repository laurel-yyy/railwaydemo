package com.ourgroup.railway.framework.page;

import lombok.Data;

@Data
public class PageRequest {

    /**
     * current page number
     */
    private Long current = 1L;

    /**
     * page size
     */
    private Long size = 10L;
}
