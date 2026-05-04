package com.githubx.usersms.criteria.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
public class SearchRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8514625832019794838L;

    private List<FilterRequest> filters;

    private List<SortRequest> sorts;

    private Integer page;

    private Integer size;


    public List<FilterRequest> getFilters() {
        if (Objects.isNull(this.filters)) return new ArrayList<>();
        return this.filters;
    }

    public List<SortRequest> getSorts() {
        if (Objects.isNull(this.sorts)) return new ArrayList<>();
        return this.sorts;
    }

    public SearchRequest(List<FilterRequest> filters, List<SortRequest> sorts, Integer page, Integer size) {
        this.filters = filters;
        this.sorts = sorts;
        this.page = page;
        this.size = size;
    }

    public SearchRequest(List<FilterRequest> filters, List<SortRequest> sorts) {
        this.filters = filters;
        this.sorts = sorts;
    }

    public SearchRequest(List<FilterRequest> filters) {
        this.filters = filters;
    }

}
