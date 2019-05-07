package yyl.demo.common.model;

import com.github.relucent.base.util.collect.Mapx;

public class MapReference {

    private Mapx value;

    public MapReference(Mapx value) {
        this.value = value;
    }

    public Mapx get() {
        return value;
    }
}
