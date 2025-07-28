package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ITripDAO;
import dst.ass1.jpa.model.ITrip;
import dst.ass1.jpa.model.TripState;
import dst.ass1.jpa.util.Constants;

import javax.persistence.EntityManager;
import java.util.List;

public class TripDAO implements ITripDAO {
    private EntityManager em;

    public TripDAO(EntityManager em) {
        this.em = em;
    }
    @Override
    public ITrip findById(Long id) {
        return (ITrip) this.em.createQuery(
                        "SELECT t FROM Trip t WHERE t.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<ITrip> findAll() {
        return (List<ITrip>) this.em.createQuery("SELECT t FROM Trip t").getResultList();
    }

    @Override
    public List<ITrip> findByStatus(TripState state) {
        return (List<ITrip>) this.em.createNamedQuery(Constants.Q_TRIP_BY_STATE)
                .setParameter("state", state)
                .getResultList();
    }
}
