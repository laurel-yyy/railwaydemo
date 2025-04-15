package com.ourgroup.railway.framework.convention.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Page response for pagination results
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * current page number
     */
    private Long current;
    
    /**
     * page size
     */
    private Long size;
    
    /**
     * total records
     */
    private Long total;
    
    /**
     * records on current page
     */
    private List<T> records;
    
    /**
     * Create an empty page response
     */
    public static <T> PageResponse<T> empty() {
        return PageResponse.<T>builder()
                .current(1L)
                .size(10L)
                .total(0L)
                .records(Collections.emptyList())
                .build();
    }
    
    /**
     * Create a page response with data
     */
    public static <T> PageResponse<T> of(List<T> records, Long current, Long size, Long total) {
        return PageResponse.<T>builder()
                .current(current)
                .size(size)
                .total(total)
                .records(records)
                .build();
    }
    
    /**
     * Check if has next page
     */
    public boolean hasNext() {
        if (total == null || current == null || size == null) {
            return false;
        }
        return (current * size) < total;
    }
    
    /**
     * Get total pages
     */
    public Long getTotalPages() {
        if (total == null || size == null || size == 0) {
            return 0L;
        }
        return (total + size - 1) / size;
    }
}