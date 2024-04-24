package org.hua.classloader.pipeline;

import java.util.regex.Pattern;

/**
 * 上下文
 * @param <T>
 */
public class ChainContext<T extends ChainModel> {

    /**
     * 标识责任链的code
     */
    private String code;

    /**
     * 存储责任链上下文数据模型
     */
    private T processModel;


    /**
     * 责任链中断标识
     */
    private Boolean needBreak = false;

    private String response;
    private Throwable throwable;

    public void setException(Throwable throwable){
        this.throwable = throwable;
    }

    public Throwable getException(){
        return throwable;
    }

    public void setResponse(String response){
        this.response = response;
    }

    public String getResponse(){
        return response;
    }

    /**
     * 流程处理结果
     * @return
     */
    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public T getProcessModel(){
        return processModel;
    }

    public void setProcessModel(T processModel){
        this.processModel = processModel;
    }

    public Boolean getNeedBreak(){
        return  needBreak;
    }

    public void setNeedBreak(Boolean needBreak){
        this.needBreak = needBreak;
    }

}
