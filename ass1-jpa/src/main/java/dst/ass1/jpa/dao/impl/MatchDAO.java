package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IMatchDAO;
import dst.ass1.jpa.model.IMatch;

import javax.persistence.EntityManager;
import java.util.List;

public class MatchDAO implements IMatchDAO {
    private EntityManager em;

    public MatchDAO(EntityManager em) {
        this.em = em;
    }
    @Override
    public IMatch findById(Long id) {
        return (IMatch) this.em.createQuery(
                        "SELECT m FROM Match m WHERE m.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<IMatch> findAll() {
        return (List<IMatch>) this.em.createQuery("SELECT m FROM Match m").getResultList();
    }
}
