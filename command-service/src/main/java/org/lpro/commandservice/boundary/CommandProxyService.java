/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lpro.commandservice.boundary;

import org.lpro.commandservice.entity.SandwichBean;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author lucas
 */
@FeignClient(name="category-service")
@RibbonClient(name="category-service")
public interface CommandProxyService {
    
    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/commands/{id}/sandwichs")
    public SandwichBean getSandwichCommand(@PathVariable("id") String id);

}
