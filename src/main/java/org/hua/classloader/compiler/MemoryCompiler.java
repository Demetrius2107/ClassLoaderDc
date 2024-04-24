package org.hua.classloader.compiler;

import org.hua.classloader.core.ChameleonSrcCode;
import org.hua.classloader.pipeline.ChainContext;
import org.hua.classloader.pipeline.ChainProcess;

import java.io.IOException;
import java.util.Map;

public class MemoryCompiler implements Compiler, ChainProcess<ChameleonSrcCode> {
    @Override
    public Map<String, byte[]> compile(String javaSrc) throws IOException {
        return null;
    }

    @Override
    public void process(ChainContext<ChameleonSrcCode> context) {

    }
}
