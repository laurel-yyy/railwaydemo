package com.ourgroup.railway.framework.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


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
    private Long size = 10L;

    /**
     * total page number
     */
    private Long total;

    /**
     * total record number
     */
    private List<T> records = Collections.emptyList();

    public PageResponse(long current, long size) {
        this(current, size, 0);
    }

    public PageResponse(long current, long size, long total) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
    }

    public PageResponse setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public <R> PageResponse<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(Collectors.toList());
        return ((PageResponse<R>) this).setRecords(collect);
    }
}
