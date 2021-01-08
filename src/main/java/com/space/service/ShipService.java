package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

import java.util.Date;
import java.util.List;

public interface ShipService {

    List<Ship> getShips(String name,
                        String planet,
                        ShipType shipType,
                        Long after,
                        Long before,
                        Boolean isUsed,
                        Double minSpeed,
                        Double maxSpeed,
                        Integer minCrewSize,
                        Integer maxCrewSize,
                        Double minRating,
                        Double maxRating
    );

    Ship createShip(Ship ship);
    Ship updateShip(Ship oldShip, Ship newShip);
    void deleteShip(Ship ship);
    Ship getShip(Long id);
    List<Ship> sortShip(List<Ship> shipList, ShipOrder order);
    List<Ship> getShipFromPage(List<Ship> shipList, Integer pageNumber, Integer pageSize);
    boolean isShipValid(Ship ship);
    double calcRating(double speed, boolean isUsed, Date dateProduct);
}
