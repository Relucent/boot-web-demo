package yyl.demo.common.plugin;

import java.util.List;
import java.util.function.Function;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.relucent.base.common.page.PageUtil;
import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.common.page.SimplePage;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * _Mybatis 分页工具类
 */
public class MybatisHelper {

    private static ThreadLocal<PageContext> PAGE_CONTEXT_HOLDER = ThreadLocal.withInitial(PageContext::new);

    /**
     * 获得集合第一个元素
     * @param <E> 元素类型泛型
     * @param collection 集合对象
     * @return 集合第一个元素,如果集合为空返回NULL
     */
    public static <E> E one(List<E> collection) {
        return (collection == null || collection.isEmpty()) ? null : collection.get(0);
    }

    /**
     * 分页查询
     * @param <T> 查询的实体类型
     * @param pagination 分页条件
     * @param select 查询方法
     * @return 分页查询结果
     */
    public static <T> SimplePage<T> selectPage(Pagination pagination, Select<T> select) {
        try {
            PageContext context = PAGE_CONTEXT_HOLDER.get();
            context.setOffset(pagination.getOffset());
            context.setLimit(pagination.getLimit());
            context.setTotal(-1);
            List<T> records = select.get();
            long offset = pagination.getOffset();
            long limit = pagination.getLimit();
            long total = context.getTotal();
            return new SimplePage<T>(offset, limit, records, total);
        } finally {
            release();
        }
    }

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

    /**
     * 获得当前分页条件
     * @return 分页条件
     */
    protected static PageContext getPageContext() {
        return PAGE_CONTEXT_HOLDER.get();
    }

    /**
     * 释放资源
     */
    private static void release() {
        PAGE_CONTEXT_HOLDER.remove();
    }

    /** 查询方法 */
    public static interface Select<T> {
        List<T> get();
    }

    @FunctionalInterface
    public static interface PagingSelect<T> extends Function<IPage<T>, IPage<T>> {
    }

    /** 分页的上下文 */
    @Data
    @Accessors(chain = true)
    protected static class PageContext {
        private long offset = 0L;
        private long limit = 1L;
        private long total = -1L;
    }
}
