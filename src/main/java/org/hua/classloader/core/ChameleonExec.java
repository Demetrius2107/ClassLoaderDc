package org.hua.classloader.core;

public interface ChameleonExec<T> {

    /**
     *
     * @param name 指定的变色龙 {@link org.hua.classloader.annotation.ChameleonX} # value
     * @param data 数据
     */
    void exec(String name,T data);
}
