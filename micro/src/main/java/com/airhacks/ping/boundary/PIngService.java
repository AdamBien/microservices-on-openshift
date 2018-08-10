
package com.airhacks.ping.boundary;

import com.airhacks.ping.control.NumberService;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class PIngService {

    @Inject
    NumberService number;

    public String message() {
        return " the number of today is: " + number.number();
    }


}
