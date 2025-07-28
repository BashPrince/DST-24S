package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ILocationDAO;
import dst.ass1.jpa.model.ILocation;

import javax.persistence.EntityManager;
import java.util.List;

public class LocationDAO implements ILocationDAO {
    private EntityManager em;

    public LocationDAO(EntityManager em) {
        this.em = em;
    }
    @Override
    public ILocation findById(Long id) {
        return (ILocation) this.em.createQuery(
                        "SELECT l FROM Location l WHERE l.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<ILocation> findAll() {
        return (List<ILocation>) this.em.createQuery("SELECT l FROM Location l").getResultList();
    }
}
