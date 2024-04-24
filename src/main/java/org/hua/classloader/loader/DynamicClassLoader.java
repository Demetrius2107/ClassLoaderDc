package org.hua.classloader.loader;

import org.hua.classloader.core.Chameleon;
import org.hua.classloader.core.ChameleonSrcCode;
import org.hua.classloader.pipeline.ChainContext;
import org.hua.classloader.pipeline.ChainProcess;

import java.util.Map;

public class DynamicClassLoader  extends ClassLoader implements ChainProcess<ChameleonSrcCode> {

    public DynamicClassLoader(){
        super(DynamicClassLoader.class.getClassLoader());
    }

    public Class<?> load(Map<String,byte[]> bytecode) throws ClassNotFoundException{
        if (null != bytecode || bytecode.size() != 0){
            /**
             * 加载得到Class对象
             */
            final String name = bytecode.keySet().iterator().next();
            byte[] buf = bytecode.get(name);
            if(buf == null){
                return super.findClass(name);
            }
            return new DynamicClassLoader().loadClassByte(buf);
        } else {
            throw new ClassNotFoundException("can not found class");
        }
    }

    public Class loadClassByte(byte[] classBytes){
        return defineClass(null,classBytes,0, classBytes.length);
    }

    @Override
    public void process(ChainContext<ChameleonSrcCode> context) {
        try{
            final Class<?> load = load(context.getProcessModel().getClassMap());
            context.getProcessModel().setChameleon((Chameleon) load.newInstance());
        } catch (Exception e){
            context.setNeedBreak(true);
            context.setException(e);
        }
    }
}
