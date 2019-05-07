package yyl.demo.common.model;

import com.github.relucent.base.common.page.Pagination;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("分页查询对象(PaginationQO)")
@Data
@SuppressWarnings("serial")
public class PaginationQO<T> implements Pagination {
    @ApiModelProperty(value = "查询的偏移量，从0开始", example = "0")
    private long offset = 1;
    @ApiModelProperty(value = "每页记录条数", example = "20")
    private long limit = 20;
    @ApiModelProperty(value = "查询条件")
    private T filter;
}