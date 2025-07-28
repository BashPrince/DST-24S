package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.*;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Match implements IMatch {
    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @Embedded
    private Money fare;

    @OneToOne(targetEntity = Trip.class, optional = false)
    private ITrip trip;

    @ManyToOne(targetEntity = Driver.class, optional = false)
    private IDriver driver;

    @ManyToOne(targetEntity = Vehicle.class, optional = false)
    private IVehicle vehicle;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public IMoney getFare() {
        return fare;
    }

    @Override
    public void setFare(IMoney money) {
        this.fare = new Money();
        this.fare.setCurrency(money.getCurrency());
        this.fare.setCurrencyValue(money.getCurrencyValue());
    }

    @Override
    public ITrip getTrip() {
        return trip;
    }

    @Override
    public void setTrip(ITrip trip) {
        this.trip = trip;
    }

    @Override
    public IDriver getDriver() {
        return driver;
    }

    @Override
    public void setDriver(IDriver driver) {
        this.driver = driver;
    }

    @Override
    public IVehicle getVehicle() {
        return vehicle;
    }

    @Override
    public void setVehicle(IVehicle vehicle) {
        this.vehicle = vehicle;
    }
}
