package org.hua.classloader.core;

/**
 * 实现该接口 即可等待变色龙工作 实际传入JAVAsrc 就是要实现该方法
 * @param <T>
 */
public interface Chameleon <T>{
    void process(T t);

}
