package com.xy.lib.callback;

/**
 * @author XieYan
 * @date 2020/8/3 14:18
 */
public interface XConsumer<T> {
    /**
     * Consume the given value.
     *
     * @param t the value
     * @throws Exception on error
     */
    void accept(T t);
}
