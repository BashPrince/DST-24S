package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IRiderDAO;
import dst.ass1.jpa.model.IRider;
import dst.ass1.jpa.util.Constants;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

public class RiderDAO implements IRiderDAO {
    private EntityManager em;

    public RiderDAO(EntityManager em) {
        this.em = em;
    }
    @Override
    public IRider findById(Long id) {
        return (IRider) this.em.createQuery(
                        "SELECT r FROM Rider r WHERE r.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<IRider> findAll() {
        return (List<IRider>) this.em.createQuery("SELECT r FROM Rider r").getResultList();
    }

    @Override
    public IRider findByEmail(String email) {
        List<IRider> result = (List<IRider>) this.em.createNamedQuery(Constants.Q_RIDER_BY_EMAIL)
                .setParameter("email", email)
                .getResultList();

        return result.size() == 1 ? result.get(0) : null;
    }

    @Override
    public List<IRider> findRidersWithNoTrips(Date start, Date end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException();
        }

        Set<Long> activeRiderIds = new HashSet<>(
                this.em.createNamedQuery("activeRiderId", Long.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList());

        List<IRider> allRiders = this.findAll();

        return allRiders.stream().filter(r -> !activeRiderIds.contains(r.getId())).collect(Collectors.toList());
    }
}
