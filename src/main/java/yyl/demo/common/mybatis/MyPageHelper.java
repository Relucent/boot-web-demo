package yyl.demo.common.mybatis;

import java.util.function.Function;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
	 * PageVO page = MyPageHelper.invoke(pagination, pd -> mapper.selectList(pd, parameters));
	 * </pre>
	 * 
	 * @param <T> 查询的实体类型
	 * @param pagination 分页条件
	 * @param select 查询方法
	 * @return 分页查询结果
	 */
	public static <T> PageVO<T> invoke(Pagination pagination, Select<T> select) {
		long offset = pagination.getOffset();
		long limit = pagination.getLimit();
		long current = PageUtil.getCurrent(offset, limit);
		IPage<T> localPage = new Page<>(current, limit);
		IPage<T> resultPage = select.apply(localPage);
		long total = localPage.getTotal();
		return new PageVO<T>(offset, limit, resultPage.getRecords(), total);
	}

	/** 查询方法 */
	@FunctionalInterface
	public static interface Select<T> extends Function<IPage<T>, IPage<T>> {
	}
}
