package org.hua.classloader.core;

import org.hua.classloader.pipeline.ChainModel;

import java.util.Map;

/**
 * 流水线中处理上下文
 * @param <T>
 */
public class ChameleonSrcCode <T> implements ChainModel {

    private final String javaSrc;

    private Chameleon chameleon;

    private Map<String,byte[]> classMap;

    public Chameleon getChameleon(){
        return chameleon;
    }

    public void setChameleon(Chameleon chameleon){
        this.chameleon = chameleon;
    }

    public Map<String,byte[]> getClassMap(){
        return classMap;
    }

    public void setClassMap(Map<String,byte[]> classMap){
        this.classMap = classMap;
    }

    public ChameleonSrcCode(String javaSrc) {
        this.javaSrc = javaSrc;
    }


    public String getJavaSrc(){
        return javaSrc;
    }
}
