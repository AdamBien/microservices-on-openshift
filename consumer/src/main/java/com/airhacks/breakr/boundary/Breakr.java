
package com.airhacks.breakr.boundary;

import java.util.concurrent.atomic.LongAdder;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author airhacks.com
 */
public class Breakr {

    LongAdder errorCounter = new LongAdder();

    @AroundInvoke
    public Object guard(InvocationContext ic) throws Exception {
        try {
            if (errorCounter.intValue() < 3) {
                return ic.proceed();
            } else {
                return null;
            }
        } catch (Exception ex) {
            errorCounter.increment();
            throw ex;
        }
    }

}
