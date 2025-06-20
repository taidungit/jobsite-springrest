package vn.dungmount.jobsite.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Meta {
    private int page;
    private int pageSize;
    private int pages;
    private Long total;
}
