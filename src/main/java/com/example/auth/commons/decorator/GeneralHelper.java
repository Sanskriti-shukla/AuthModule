package com.example.auth.commons.decorator;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class GeneralHelper {

    int defaultPageValue = 5;

    public PageRequest getPagination(Integer page, Integer limit) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = defaultPageValue;
        }
        return PageRequest.of(page, limit);
    }
}
