package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipController {

    private ShipService shipService;

    public ShipController() {
    }

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @RequestMapping(value = "/ships", method = RequestMethod.GET)
    public List<Ship> getAllShip (@RequestParam (value = "name", required = false) String name,
                                  @RequestParam (value = "planet", required = false) String planet,
                                  @RequestParam (value = "shipType", required = false) ShipType shipType,
                                  @RequestParam (value = "after", required = false) Long after,
                                  @RequestParam (value = "before", required = false) Long before,
                                  @RequestParam (value = "isUsed", required = false) Boolean isUsed,
                                  @RequestParam (value = "minSpeed", required = false) Double minSpeed,
                                  @RequestParam (value = "maxSpeed", required = false) Double maxSpeed,
                                  @RequestParam (value = "minCrewSize", required = false) Integer minCrewSize,
                                  @RequestParam (value = "maxCrewSize", required = false) Integer maxCrewSize,
                                  @RequestParam (value = "minRating", required = false) Double minRating,
                                  @RequestParam (value = "maxRating", required = false) Double maxRating,
                                  @RequestParam (value = "order", required = false) ShipOrder order,
                                  @RequestParam (value = "pageNumber", required = false) Integer pageNumber,
                                  @RequestParam (value = "pageSize", required = false) Integer pageSize
    ){
        final List<Ship> ships = new ArrayList<>(shipService.getShips(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating));
        final List<Ship> sortedShips = shipService.sortShip(ships, order);
        return shipService.getShipFromPage(sortedShips, pageNumber, pageSize);
    }

    @RequestMapping(value = "/ships/count", method = RequestMethod.GET)
    public Integer getShipsCount (@RequestParam (value = "name", required = false) String name,
                                  @RequestParam (value = "planet", required = false) String planet,
                                  @RequestParam (value = "shipType", required = false) ShipType shipType,
                                  @RequestParam (value = "after", required = false) Long after,
                                  @RequestParam (value = "before", required = false) Long before,
                                  @RequestParam (value = "isUsed", required = false) Boolean isUsed,
                                  @RequestParam (value = "minSpeed", required = false) Double minSpeed,
                                  @RequestParam (value = "maxSpeed", required = false) Double maxSpeed,
                                  @RequestParam (value = "minCrewSize", required = false) Integer minCrewSize,
                                  @RequestParam (value = "maxCrewSize", required = false) Integer maxCrewSize,
                                  @RequestParam (value = "minRating", required = false) Double minRating,
                                  @RequestParam (value = "maxRating", required = false) Double maxRating
    ){
        final List<Ship> ships = new ArrayList<>(shipService.getShips(name, planet, shipType, after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating));
        return ships.size();
    }

    @RequestMapping(value = "/ships/{id}" , method = RequestMethod.GET)
    public ResponseEntity<Ship> getShip (@PathVariable(value = "id") String stringId){
        final Long id = convertIdToLong(stringId);

        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Ship ship = shipService.getShip(id);
        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @RequestMapping(value = "/ships", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship){
        if (!shipService.isShipValid(ship)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ship.setSpeed(ship.getSpeed());
        if (ship.getUsed() == null) ship.setUsed(false);
        final double rating = shipService.calcRating(ship.getSpeed(), ship.getUsed(), ship.getProdDate());
        ship.setRating(rating);
        final Ship savedShip = shipService.createShip(ship);
        return new ResponseEntity<>(savedShip, HttpStatus.OK);
    }

    @RequestMapping(value = "/ships/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Ship> updateShip(@PathVariable(value = "id") String stringId,
                                           @RequestBody  Ship newShip){
        final Long id = convertIdToLong(stringId);
        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Ship shipForChange = shipService.getShip(id);
        if (shipForChange == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            final Ship updatedShip = shipService.updateShip(shipForChange, newShip);
            return new ResponseEntity<>(updatedShip, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/ships/{id}" , method = RequestMethod.DELETE)
    public ResponseEntity<Ship> deleteShip (@PathVariable(value = "id") String stringId){
        final Long id = convertIdToLong(stringId);

        if (id == null || id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        final Ship ship = shipService.getShip(id);
        if (ship == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        shipService.deleteShip(ship);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Long convertIdToLong(String string){
        if (string == null){
            return null;
        } else try {
            return Long.parseLong(string);
        } catch (NumberFormatException e){
            return null;
        }
    }

}
