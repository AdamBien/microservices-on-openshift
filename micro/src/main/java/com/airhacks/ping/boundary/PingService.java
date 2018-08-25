
package com.airhacks.ping.boundary;

import com.airhacks.ping.control.NumberService;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class PingService {

    @Inject
    NumberService number;

    @Inject
    String prefix;

    public String message() {
        return this.prefix + " the number of today is: " + number.number();
    }


}
