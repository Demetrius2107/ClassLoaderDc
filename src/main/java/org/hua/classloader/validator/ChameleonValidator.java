package org.hua.classloader.validator;

import org.hua.classloader.core.ChameleonSrcCode;
import org.hua.classloader.pipeline.ChainContext;
import org.hua.classloader.pipeline.ChainProcess;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChameleonValidator  implements ChameleonValidationService, ChainProcess<ChameleonSrcCode> {

    String patternAnnotation = "@ChameleonX\\(.*?\\)";

    String patternInterface = "implements\\s+([^\\{]+)";

    String patternMethod = "public void process\\([^)]*\\) \\{[^}]*\\}";

    String patternClass = "Chameleon(\\\\<.*?\\\\>)?";

    @Override
    public void process(ChainContext<ChameleonSrcCode> context) {
        try{
            validator(context.getProcessModel().getJavaSrc());
        } catch (JavaSrcValidatorException e){
            context.setNeedBreak(true);
            context.setResponse(e.getMessage());
            context.setException(e);
        }
    }

    @Override
    public void validator(String javaSrc) throws JavaSrcValidatorException {

    }

    //检查是否为空
    protected void validatorIsEmpty(String javaSrc) throws JavaSrcValidatorException{
        if(javaSrc == null || javaSrc.isEmpty()){
            throw new JavaSrcValidatorException("java src is empty");
        }
    }


    //检查注解
    protected void validatorAnnotation(String javaSrc) throws JavaSrcValidatorException{
        Pattern regex = Pattern.compile(patternAnnotation);
        Matcher matcher = regex.matcher(javaSrc);
        if(!matcher.find()){
            throw new JavaSrcValidatorException("not found `@Chameleonx` annotation");
        }
    }


    //检查注解
    protected void validatorInterface(String javaSrc) throws JavaSrcValidatorException{
        Pattern regex = Pattern.compile(patternInterface);
        Matcher matcher = regex.matcher(javaSrc);
        boolean flag = false;
        //检测实现 Chameleon
        if(matcher.find()){
            String interfaces = matcher.group(1).trim();
            String[] interfaceList = interfaces.split("\\s*\\s*");
            String pattern = patternClass;
            for(String intf: interfaceList){
                regex = Pattern.compile(pattern);
                matcher = regex.matcher(intf);
                if(matcher.find()){
                    return;
                }
            }
        }
        throw new JavaSrcValidatorException("not found `Chameleon` interface");
    }

    protected void validatorMethod(String javaSrc) throws JavaSrcValidatorException{
        Pattern regex = Pattern.compile(patternMethod);
        Matcher matcher = regex.matcher(javaSrc);
        if(!matcher.find()){
            throw new JavaSrcValidatorException("not found `public void process` method");
        }
    }

}
