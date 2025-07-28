package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ITripInfoDAO;
import dst.ass1.jpa.model.ITripInfo;
import dst.ass1.jpa.util.Constants;
import dst.ass1.jpa.util.TupleResult;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

public class TripInfoDAO implements ITripInfoDAO {
    private EntityManager em;

    public TripInfoDAO(EntityManager em) {
        this.em = em;
    }
    @Override
    public ITripInfo findById(Long id) {
        return (ITripInfo) this.em.createQuery(
                        "SELECT t FROM TripInfo t WHERE t.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<ITripInfo> findAll() {
        return (List<ITripInfo>) this.em.createQuery("SELECT t FROM TripInfo t").getResultList();
    }

    @Override
    public List<TupleResult<Long, Double>> findRidersAverageRating() {
        List<Object[]> result = this.em.createNamedQuery(Constants.Q_AVERAGE_RATING_RIDER).getResultList();

        List<TupleResult<Long, Double>> tupleResults = new LinkedList<>();

        for (Object[] r : result) {
            tupleResults.add(new TupleResult<>((Long) r[0], (Double) r[1]));
        }

        return tupleResults;
    }
}
