package yyl.demo.common.identifier;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;

import com.github.relucent.base.util.identifier.SerialIdWorker;

import yyl.demo.common.standard.Idable;

/**
 * 序列ID生成器<br>
 * 格式为 日期(年月日时分秒)+毫秒+毫秒内计数 +后缀 (23位)<br>
 * yyyyMMddHHmmssSSS++++?? <br>
 * 12345678901234567890123 <br>
 */
public class IdHelper {

    // ==============================Fields===========================================
    private static final AtomicReference<String> SUFFIX = new AtomicReference<>("00");

    // ==============================Methods==========================================
    /**
     * 下一个ID
     * @return
     */
    public static String nextId() {
        return SerialIdWorker.DEFAULT.nextId() + SUFFIX.get();
    }

    /**
     * 设置ID(如果ID为空)
     * @param entity 实体对象
     */
    public static void setIfEmptyId(Idable entity) {
        if (entity != null && StringUtils.isEmpty(entity.getId())) {
            entity.setId(nextId());
        }
    }

    protected static void setSuffix(String suffix) {
        SUFFIX.set(StringUtils.substring(StringUtils.leftPad(suffix, SUFFIX.get().length(), '0'), 0, 2));
    }
}
