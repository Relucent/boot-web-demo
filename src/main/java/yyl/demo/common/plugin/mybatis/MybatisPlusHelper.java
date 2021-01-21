package yyl.demo.common.plugin.mybatis;

import java.util.function.Function;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.relucent.base.common.page.PageUtil;
import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.common.page.SimplePage;

/**
 * _Mybatis 分页工具类
 */
public class MybatisPlusHelper {

    /**
     * 分页查询(基于_mybatisplus)
     *
     * <pre>
     * PageVo page = DaoHelper.selectPage(pagination, p -> mapper.selectPage(p, queryWrapper));
     * </pre>
     *
     * @param <T> 返回的记录对象类型
     * @param pagination 分页条件
     * @param select 分页查询调用方法
     * @return 分页查询结果
     */
    public static <T> SimplePage<T> selectPage(Pagination pagination, PagingSelect<T> select) {
        IPage<T> conditionPage = toIPage(pagination, true);
        IPage<T> resultPage = select.apply(conditionPage);
        long current = resultPage.getCurrent();
        long limit = resultPage.getSize();
        long offset = PageUtil.getOffset(current, limit);
        return new SimplePage<>(offset, limit, resultPage.getRecords(), resultPage.getTotal());
    }

    public static <T> IPage<T> toIPage(Pagination pagination, boolean isSearchCount) {
        long offset = pagination.getOffset();
        long limit = pagination.getLimit();
        return toIPageByBounds(offset, limit, isSearchCount);
    }

    private static <T> IPage<T> toIPageByBounds(long offset, long limit, boolean isSearchCount) {
        long current = PageUtil.getCurrent(offset, limit);
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<T>(current, limit, isSearchCount);
    }

    @FunctionalInterface
    public static interface PagingSelect<T> extends Function<IPage<T>, IPage<T>> {
    }
}
