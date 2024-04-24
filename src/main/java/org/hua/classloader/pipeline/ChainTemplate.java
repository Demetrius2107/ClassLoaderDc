package org.hua.classloader.pipeline;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class ChainTemplate {

    public static final String DEFAULT = "default";


    private Map<String,Collection<ChainProcess>> templates;

    public ChainTemplate(Map<String, Collection<ChainProcess>> templates){
        this.templates = templates;
    }

    public Map<String,Collection<ChainProcess>> getTemplates(){
        return templates;
    }

}
