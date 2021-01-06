package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;

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
                        Double maxRating,
                        ShipOrder order,
                        Integer pageNumber,
                        Integer pageSize
    );

    Ship createShip(Ship ship);
    Ship updateShip(Ship oldShip, Ship newShip);
    void deleteShip(Long id);
    Ship getShip(Long id);
    List<Ship> sortShip(List<Ship> shipList, ShipOrder order);
    List<Ship> getShipFromPage(List<Ship> shipList, Integer pageNumber, Integer pageSize);
    boolean isShipValid(Ship ship);
    Double calcRating(Ship ship);
}
