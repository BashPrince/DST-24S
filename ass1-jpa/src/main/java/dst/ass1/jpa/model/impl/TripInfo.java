package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.ITrip;
import dst.ass1.jpa.model.ITripInfo;
import dst.ass1.jpa.model.ITripReceipt;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQuery(
        name = Constants.Q_AVERAGE_RATING_RIDER,
        query = "SELECT r.id, AVG(ti.riderRating) " +
                "FROM Rider r JOIN r.trips t JOIN t.tripInfo ti " +
                "GROUP BY r.id " +
                "ORDER BY AVG(ti.riderRating) DESC"
)
public class TripInfo implements ITripInfo {
    @Id
    @GeneratedValue
    private Long id;

    private Date completed;

    private Double distance;

    private Integer driverRating;

    private Integer riderRating;

    @OneToOne(targetEntity = Trip.class, optional = false)
    private ITrip trip;

    @OneToOne(targetEntity = TripReceipt.class, cascade = CascadeType.REMOVE)
    private ITripReceipt tripReceipt;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getCompleted() {
        return completed;
    }

    @Override
    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    @Override
    public Double getDistance() {
        return distance;
    }

    @Override
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public Integer getDriverRating() {
        return driverRating;
    }

    @Override
    public void setDriverRating(Integer driverRating) {
        this.driverRating = driverRating;
    }

    @Override
    public Integer getRiderRating() {
        return riderRating;
    }

    @Override
    public void setRiderRating(Integer riderRating) {
        this.riderRating = riderRating;
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
    public ITripReceipt getReceipt() {
        return tripReceipt;
    }

    @Override
    public void setReceipt(ITripReceipt receipt) {
        this.tripReceipt = receipt;
    }
}
