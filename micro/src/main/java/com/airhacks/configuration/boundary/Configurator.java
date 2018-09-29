
package com.airhacks.configuration.boundary;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author airhacks.com
 */
public class Configurator {

    @Produces
    public String expose(InjectionPoint ip) {
        String fieldName = ip.getMember().getName();
        return System.getenv().getOrDefault(fieldName, System.getProperty(fieldName, "!!!!"));
    }
}
