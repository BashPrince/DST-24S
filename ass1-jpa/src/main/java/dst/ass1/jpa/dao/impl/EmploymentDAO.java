package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IEmploymentDAO;
import dst.ass1.jpa.model.IEmployment;

import javax.persistence.EntityManager;
import java.util.List;

public class EmploymentDAO implements IEmploymentDAO {
    private EntityManager em;

    public EmploymentDAO(EntityManager em) {
        this.em = em;
    }
    @Override
    public IEmployment findById(Long id) {
        return (IEmployment) this.em.createQuery(
                        "SELECT e FROM Employment e WHERE e.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<IEmployment> findAll() {
        return (List<IEmployment>) this.em.createQuery("SELECT e FROM Employment e").getResultList();
    }
}
