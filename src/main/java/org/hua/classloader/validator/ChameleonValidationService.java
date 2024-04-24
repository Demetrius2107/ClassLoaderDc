package org.hua.classloader.validator;

public interface ChameleonValidationService {
    void validator(String javaSrc) throws JavaSrcValidatorException;
}
