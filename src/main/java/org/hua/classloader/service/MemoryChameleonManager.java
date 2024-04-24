package org.hua.classloader.service;

import org.hua.classloader.annotation.ChameleonX;
import org.hua.classloader.core.Chameleon;
import org.hua.classloader.core.ChameleonSrcCode;
import org.hua.classloader.pipeline.ChainContext;
import org.hua.classloader.pipeline.ChainProcess;


/**
 * 基于内存管理变色龙
 */
public class MemoryChameleonManager extends ChameleonManagerService implements ChainProcess<ChameleonSrcCode> {
    @Override
    public void process(ChainContext<ChameleonSrcCode> context) {
        final ChameleonSrcCode processModel = context.getProcessModel();
        final Chameleon chameleon = processModel.getChameleon();
        final String name = chameleon.getClass().getAnnotation(ChameleonX.class).value();
        try{
            super.register(name, processModel.getJavaSrc(), chameleon);
        } catch (Exception e){
            context.setNeedBreak(true);
            context.setException(e);
        }
    }
}
