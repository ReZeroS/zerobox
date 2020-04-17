package club.qqtim.executor.validator.support;

import club.qqtim.annotation.ValidRule;
import club.qqtim.executor.validator.Validator;

/**
 * @version: 1.0
 * @author: rezeros.github.io
 * @date: 2020/4/7
 * @description:
 */
public class LiquibaseFormatValidator implements Validator {


    @ValidRule
    private boolean validTableName(){
        return true;
    }


    @Override
    public boolean valid() {
        // get load data from context


        // filter the data required valid


        // valid them
        return true;
    }
}
