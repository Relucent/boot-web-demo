package yyl.demo.common.mybatis;

import java.util.List;
import java.util.function.Supplier;

import com.github.pagehelper.PageHelper;
import com.github.relucent.base.common.page.PageUtil;
import com.github.relucent.base.common.page.Pagination;

import yyl.demo.common.model.PageVO;

/**
 * _Mybatis 分页工具类
 */
public class MyPageHelper {

    /**
     * 分页查询
     * 
     * <pre>
     * PageVO page = MyPageHelper.invoke(pagination, () -> mapper.selectList(parameters));
     * </pre>
     * 
     * @param <T> 查询的实体类型
     * @param pagination 分页条件
     * @param select 查询方法
     * @return 分页查询结果
     */
    public static <T> PageVO<T> invoke(Pagination pagination, Select<T> select) {
        try {
            long offset = pagination.getOffset();
            long limit = pagination.getLimit();
            long current = PageUtil.getCurrent(offset, limit);
            com.github.pagehelper.Page<?> localPage = PageHelper.startPage((int) current, (int) limit);
            List<T> records = select.get();
            long total = localPage.getTotal();
            return new PageVO<T>(offset, limit, records, total);
        } finally {
            PageHelper.clearPage();
        }
    }

    /** 查询方法 */
    @FunctionalInterface
    public static interface Select<T> extends Supplier<List<T>> {
    }
}
