package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IPaymentInfo;
import dst.ass1.jpa.model.IRider;
import dst.ass1.jpa.model.ITrip;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedList;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "email" }) })
@NamedQuery(
        name = Constants.Q_RIDER_BY_EMAIL,
        query = "SELECT r FROM Rider r WHERE r.email = :email"
)
@NamedQuery(
        name = "activeRiderId",
        query = "SELECT r.id FROM Rider r JOIN r.trips t JOIN t.tripInfo ti " +
                "WHERE ti.completed >= :start and ti.completed <= :end"
)
public class Rider extends PlatformUser implements IRider {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(columnDefinition = "BINARY VARYING(20)")
    private byte[] password;

    @OneToMany(targetEntity = PaymentInfo.class)
    private Collection<IPaymentInfo> paymentInfos = new LinkedList<>();

    @OneToMany(targetEntity = Trip.class, mappedBy = "rider")
    private Collection<ITrip> trips = new LinkedList<>();

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public byte[] getPassword() {
        return password;
    }

    @Override
    public void setPassword(byte[] password) {
        this.password = password;
    }

    @Override
    public Collection<IPaymentInfo> getPaymentInfos() {
        return paymentInfos;
    }

    @Override
    public void setPaymentInfos(Collection<IPaymentInfo> paymentInfos) {
        this.paymentInfos = paymentInfos;
    }

    @Override
    public Collection<ITrip> getTrips() {
        return trips;
    }

    @Override
    public void setTrips(Collection<ITrip> trips) {
        this.trips = trips;
    }

    @Override
    public void addTrip(ITrip trip) {
        trips.add(trip);
    }
}
