package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IOrganizationDAO;
import dst.ass1.jpa.model.IOrganization;

import javax.persistence.EntityManager;
import java.util.List;

public class OrganizationDAO implements IOrganizationDAO {
    private EntityManager em;

    public OrganizationDAO(EntityManager em) {
        this.em = em;
    }
    @Override
    public IOrganization findById(Long id) {
        return (IOrganization) this.em.createQuery(
                        "SELECT o FROM Organization o WHERE o.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<IOrganization> findAll() {
        return (List<IOrganization>) this.em.createQuery("SELECT o FROM Organization o").getResultList();
    }
}
