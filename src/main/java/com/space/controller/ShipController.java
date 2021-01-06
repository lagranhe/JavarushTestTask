package com.space.controller;

import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShipController {

    private ShipRepository shipRepository;

    public ShipController() {
    }

    @Autowired
    public ShipController(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }
    
}
