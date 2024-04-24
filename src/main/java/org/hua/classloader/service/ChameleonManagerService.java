package org.hua.classloader.service;

import org.hua.classloader.core.Chameleon;
import org.hua.classloader.core.ChameleonBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class ChameleonManagerService {
    private ConcurrentSkipListMap<String, Collection<ChameleonBean>> chameleons = new ConcurrentSkipListMap<>();

    public ConcurrentSkipListMap<String,Collection<ChameleonBean>> list(){
        return chameleons;
    }

    public Collection<ChameleonBean> get(String name){
        return chameleons.get(name);
    }

    public void remove(String className){
        remove(null,className);
    }


    public void remove(String chameleonName,String className){
        //提取共享逻辑到一个单独的方法中
        BiFunction<String,Collection<ChameleonBean>,Collection<ChameleonBean>> processEntry = (key,value)->{
            Collection<ChameleonBean> updatedList = value
                    .stream()
                    .filter(bean -> !bean.getClassName().equals(className))
                    .collect(Collectors.toList());
            return updatedList.isEmpty() ? null : updatedList;
        };

        if(chameleonName != null  && !chameleonName.isEmpty() && chameleons.containsKey(chameleonName)){
            //如果chameleonName存在,只处理这一个条目
            chameleons.compute(chameleonName,processEntry);
        } else {
            //如果chameleonName不存在,处理所有条目
            chameleons.keySet().forEach(key ->chameleons.compute(key,processEntry));
        }
        rem(chameleonName,className);


    }

    protected void rem(String chameleonName,String className){

    }

    public void register(Class clazz){

    }

    public void register(String name, String javaSrc, Chameleon chameleon) {
        if (!chameleons.containsKey(name)) {
            chameleons.put(name, new ArrayList<>());
        }

        final String className = chameleon.getClass().getName();
        final ChameleonBean chameleonBean = new ChameleonBean(chameleon, javaSrc, className);
        final ArrayList<ChameleonBean> chameleonBeans = (ArrayList<ChameleonBean>) chameleons.get(name);

        for (int i = 0; i < chameleonBeans.size(); i++) {
            if (chameleonBeans.get(i).getClassName().equals(className)) {
                chameleonBeans.set(i, chameleonBean);
                return;
            }

            chameleonBeans.add(chameleonBean);
            save(name,javaSrc,chameleon);
        }
    }
        protected void save(String name, String javaSrc, Chameleon chameleon){}

}
