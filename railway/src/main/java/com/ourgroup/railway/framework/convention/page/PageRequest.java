package com.ourgroup.railway.framework.convention.page;

import lombok.Data;

/**
 * Page request base class
 */
@Data
public class PageRequest {
    
    /**
     * Current page number (starting from 1)
     */
    private Long current = 1L;
    
    /**
     * Page size
     */
    private Long size = 10L;
}