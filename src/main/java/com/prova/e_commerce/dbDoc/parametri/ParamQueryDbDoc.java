package com.prova.e_commerce.dbDoc.parametri;

import java.util.Map;

public class ParamQueryDbDoc {
    private Map<String, Object> filters;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String order;

    // Costruttore
    public ParamQueryDbDoc() {}

    // Getter e Setter
    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ParamQuery{" +
                "filters=" + filters +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
