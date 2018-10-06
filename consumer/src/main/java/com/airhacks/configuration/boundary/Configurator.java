
package com.airhacks.configuration.boundary;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author airhacks.com
 */
public class Configurator {

    @Produces
    public String exposeString(InjectionPoint ip) {
        String fieldName = ip.getMember().getName();
        return System.getenv().getOrDefault(fieldName, System.getProperty(fieldName, "!!!!"));
    }
    @Produces
    public Long exposeLong(InjectionPoint ip) {
        String rawValue = this.exposeString(ip);
        return Long.parseLong(rawValue);
    }

    @Produces
    public Integer exposeInteger(InjectionPoint ip) {
        String rawValue = this.exposeString(ip);
        return Integer.parseInt(rawValue);
    }
}
