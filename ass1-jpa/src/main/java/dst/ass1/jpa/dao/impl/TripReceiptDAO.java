package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.ITripReceiptDAO;
import dst.ass1.jpa.model.PaymentMethod;
import dst.ass1.jpa.model.impl.PaymentInfo;
import dst.ass1.jpa.model.impl.TripInfo;
import dst.ass1.jpa.model.impl.TripReceipt;
import dst.ass1.jpa.util.TupleResult;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TripReceiptDAO implements ITripReceiptDAO {
    private EntityManager em;

    public TripReceiptDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<TupleResult<PaymentMethod, Double>> calculateAverageTipPerPaymentMethod(Date start, Date end) {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<TripReceipt> tripReceiptRoot = cq.from(TripReceipt.class);
        Join<TripReceipt, PaymentInfo> tripReceiptPaymentInfoJoin = tripReceiptRoot.join("paymentInfo");
        Join<TripReceipt, TripInfo> tripReceiptTripInfoJoin = tripReceiptRoot.join("tripInfo");

        Expression<Number> quot = cb.quot(
                tripReceiptRoot.get("tip").get("currencyValue"),
                tripReceiptRoot.get("total").get("currencyValue"));

        Expression<Double> avg = cb.prod(cb.avg(quot), 100.0);
        cq.multiselect(tripReceiptPaymentInfoJoin.get("method"), avg);

        if (start != null && end != null) {
            cq.where(
                    cb.and(
                            cb.greaterThanOrEqualTo(tripReceiptTripInfoJoin.get("completed"), start),
                            cb.lessThanOrEqualTo(tripReceiptTripInfoJoin.get("completed"), end)
                    )
            );
        } else if (start != null) {
            cq.where(cb.greaterThanOrEqualTo(tripReceiptTripInfoJoin.get("completed"), start));
        } else if (end != null) {
            cq.where(cb.lessThanOrEqualTo(tripReceiptTripInfoJoin.get("completed"), end));
        }

        cq.groupBy(tripReceiptPaymentInfoJoin.get("method"));
        cq.orderBy(cb.desc(avg));

        return em.createQuery(cq).getResultList()
                .stream()
                .map(r -> new TupleResult<>((PaymentMethod) r[0], (Double) r[1]))
                .collect(Collectors.toList());
    }
}
