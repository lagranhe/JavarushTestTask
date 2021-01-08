package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ShipServiceImpl implements ShipService{

    private ShipRepository shipRepository;

    public ShipServiceImpl() {
    }

    @Autowired
    public ShipServiceImpl(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Ship createShip(Ship ship) {
        return shipRepository.save(ship);
    }

    @Override
    public Ship updateShip(Ship oldShip, Ship newShip) {
        return null;
    }

    @Override
    public void deleteShip(Ship ship) {
        shipRepository.delete(ship);
    }

    @Override
    public Ship getShip(Long id) {
        return shipRepository.findById(id).orElse(null);
    }

    @Override
    public List<Ship> getShips(String name,
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
    )
    {
        final Date afterDate = after == null ? null : new Date(after);
        final Date beforeDate = before == null ? null : new Date(before);
        List<Ship> ships = new ArrayList<>();
        shipRepository.findAll().forEach(ship -> {
            if (name != null && !ship.getName().contains(name)) return;
            if (planet != null && !ship.getPlanet().contains(planet)) return;
            if (shipType != null && !ship.getShipType().equals(shipType)) return;
            if (afterDate != null && !ship.getProdDate().after(afterDate)) return;
            if (beforeDate != null && !ship.getProdDate().before(beforeDate)) return;
            if (isUsed != null && ship.getUsed().booleanValue() != isUsed.booleanValue()) return;
            if (minSpeed != null && ship.getSpeed().compareTo(minSpeed) < 0) return;
            if (maxSpeed != null && ship.getSpeed().compareTo(maxSpeed) > 0) return;
            if (minCrewSize != null && ship.getCrewSize().compareTo(minCrewSize) < 0) return;
            if (maxCrewSize != null && ship.getCrewSize().compareTo(maxCrewSize) > 0) return;
            if (minRating != null && ship.getRating().compareTo(minRating) < 0) return;
            if (maxRating != null && ship.getRating().compareTo(maxRating) > 0) return;
            ships.add(ship);
        });
        return ships;
    }

    @Override
    public List<Ship> sortShip(List<Ship> shipList, ShipOrder order) {
        if (order != null){
            shipList.sort((ship1, ship2) -> {
                switch (order) {
                    case ID: return ship1.getId().compareTo(ship2.getId());
                    case RATING: return ship1.getRating().compareTo(ship2.getRating());
                    case SPEED: return ship1.getSpeed().compareTo(ship2.getSpeed());
                    case DATE: return ship1.getProdDate().compareTo(ship2.getProdDate());
                    default: return 0;
                }
            } );
        }
        return shipList;
    }

    @Override
    public List<Ship> getShipFromPage(List<Ship> shipList, Integer pageNumber, Integer pageSize) {
        final int page = pageNumber == null ? 0 : pageNumber;
        final int size = pageSize == null ? 3 : pageSize;
        int from = page * size;
        int to = (page + 1) * size;
        if (to > shipList.size()) {
            to = shipList.size();
        }
        return shipList.subList(from, to);
    }

    @Override
    public boolean isShipValid(Ship ship) {
        return (ship != null
                && isStringValid(ship.getName())
                && isStringValid(ship.getPlanet())
                && isProdDateValid(ship.getProdDate())
                && isSpeedValid(ship.getSpeed())
                && isCrewSizeValid(ship.getCrewSize()));
    }

    @Override
    public double calcRating(double speed, boolean isUsed, Date dateProduct) {
        final int currentYear = 3019;
        final int productionYear = getYearFormDate(dateProduct);
        final double ratio = (isUsed) ? 0.5 : 1;
        double rating = (80 * speed * ratio) / (currentYear - productionYear + 1);
        return round(rating);
    }

    public boolean isStringValid(String value){
        final int maxStringLength = 50;
        return value != null && !value.isEmpty() && value.length() <= maxStringLength;
    }

    public boolean isSpeedValid(Double speed){
        final double minSpeed = 0.01;
        final double maxSpeed = 0.99;
        return speed != null && round(speed) >= minSpeed && round(speed) <= maxSpeed;
    }

    public boolean isCrewSizeValid(Integer crewSize){
        final int minCrewSize = 1;
        final int maxCrewSize = 9999;
        return crewSize != null && crewSize >= minCrewSize && crewSize <= maxCrewSize;
    }

    public boolean isProdDateValid(Date date){
        final Date minProdDate = getDateFormYear(2018);
        final Date maxProdDate = getDateFormYear(3019);
        return date != null && date.after(minProdDate) && date.before(maxProdDate);
    }

    public int getYearFormDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public Date getDateFormYear(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    public double round(double value){
        return Math.round(value*100)/100D;
    }
}
