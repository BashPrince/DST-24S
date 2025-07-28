package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IDriverDAO;
import dst.ass1.jpa.model.IDriver;

import javax.persistence.EntityManager;
import java.util.List;

public class DriverDAO implements IDriverDAO {
    private EntityManager em;

    public DriverDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public IDriver findById(Long id) {
        return (IDriver) this.em.createQuery(
                        "SELECT d FROM Driver d WHERE d.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<IDriver> findAll() {
        return (List<IDriver>) this.em.createQuery("SELECT d FROM Driver d").getResultList();
    }
}
