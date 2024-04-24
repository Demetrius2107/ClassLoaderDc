package org.hua.classloader.core;

import org.hua.classloader.service.ChameleonManagerService;

import java.util.Collection;

public class ChameleonHandler <T> implements ChameleonExec<T>{

    private final ChameleonManagerService chameleonManger;

    private ChameleonHandler(ChameleonManagerService chameleonManger){
        this.chameleonManger = chameleonManger;
    }


    @Override
    public void exec(String name, T data) {
        final Collection<ChameleonBean> chameleons = chameleonManger.get(name);
        Do(chameleons,data);
    }


    private void Do(Collection<ChameleonBean> chameleons,T data){
        if(chameleons != null && chameleons.size() > 0){
            for (Chameleon chameleon: chameleons){
                chameleon.process(data);
            }
        }
    }



}
