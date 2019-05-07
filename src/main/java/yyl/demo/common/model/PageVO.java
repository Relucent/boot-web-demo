package yyl.demo.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.relucent.base.common.collection.CollectionUtil;
import com.github.relucent.base.common.page.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("分页查询结果(PageVO)")
@Data
@SuppressWarnings("serial")
public class PageVO<T> implements Page<T> {

    // =================================Fields================================================
    /** 查询的偏移量(从0开始) */
    @ApiModelProperty(value = "查询的偏移量，从0开始", example = "0")
    private long offset = 1;
    /** 每页记录条数 */
    @ApiModelProperty("每页记录条数")
    private long limit = 20;
    /** 当前页数据 */
    @ApiModelProperty("当前页数据列表")
    private List<T> records;
    /** 总记录数 */
    @ApiModelProperty("总记录数")
    private long total = 0;

    // =================================Constructors===========================================
    /**
     * 构造函数
     */
    public PageVO() {
        this.records = new ArrayList<>();
    }

    /**
     * 构造函数
     * @param records 当前页数据
     */
    public PageVO(List<T> records) {
        this(0, records.size(), records, records.size());
    }

    /**
     * 构造函数
     * @param offset 记录开始索引号
     * @param limit 页面最大记录数
     * @param records 当前页数据
     * @param total 总记录数
     */
    public PageVO(long offset, long limit, List<T> records, long total) {
        this.offset = offset;
        this.limit = limit;
        this.records = records;
        this.total = total;
    }

    // =================================Methods================================================
    /**
     * 转换分页记录类型
     * @param <R> 转换的类型
     * @param mapper 转换函数
     * @return 新的分页对象
     */
    public <R> PageVO<R> mapRecords(Function<T, R> mapper) {
        return new PageVO<>(offset, limit, CollectionUtil.map(records, mapper), total);
    }

    /**
     * 遍历全部元素
     * @param action 处理函数
     */
    public void forEach(Consumer<T> action) {
        records.forEach(action);
    }
}
