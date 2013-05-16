package com.mangofactory.swagger.springmvc.example;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public abstract class BaseController<T extends Pet> {

    // some dependency here
    // some crud here
    // e.g.
    @RequestMapping(method= RequestMethod.POST)
    public @ResponseBody
    int createObject(@RequestBody T object) {
        // do some logic here
        return 1;
    }
}
