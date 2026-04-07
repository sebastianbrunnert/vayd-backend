package de.vayd.sebastianbrunnert.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@JsonView(Object.class)
public class VaydPagination<T> {

    private int page;
    private int pages;
    private int size;
    private int total;
    private List<T> elements;

    public VaydPagination(List<T> elements) {
        this.page = 0;
        this.size = elements.size();
        this.pages = 1;
        this.total = elements.size();
        this.elements = elements;
    }

    public VaydPagination(Page<T> page) {
        this.page = page.getNumber();
        this.size = page.getSize();
        this.pages = page.getTotalPages();
        this.total = (int) page.getTotalElements();
        this.elements = page.getContent();
    }

}
