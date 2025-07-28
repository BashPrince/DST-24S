package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IMoney;
import dst.ass1.jpa.model.IPaymentInfo;
import dst.ass1.jpa.model.ITripInfo;
import dst.ass1.jpa.model.ITripReceipt;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;

@Entity
public class TripReceipt implements ITripReceipt {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Money total;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride( name = Constants.AO_NAME_TIP_CURRENCY, column = @Column(name = Constants.AO_COLUMN_NAME_TIP_CURRENCY)),
            @AttributeOverride( name = Constants.AO_NAME_TIP_CURRENCY_VALUE, column = @Column(name = Constants.AO_COLUMN_NAME_TIP_CURRENCY_VALUE))
    })
    private Money tip;

    private Boolean paid;

    @OneToOne(targetEntity = TripInfo.class)
    private ITripInfo tripInfo;

    @ManyToOne(targetEntity = PaymentInfo.class, optional = false)
    private IPaymentInfo paymentInfo;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public IMoney getTotal() {
        return total;
    }

    @Override
    public void setTotal(IMoney total) {
        this.total = new Money();
        this.total.setCurrency(total.getCurrency());
        this.total.setCurrencyValue(total.getCurrencyValue());
    }

    @Override
    public Money getTip() {
        return tip;
    }

    @Override
    public void setTip(IMoney tip) {
        this.tip = new Money();
        this.tip.setCurrency(tip.getCurrency());
        this.tip.setCurrencyValue(tip.getCurrencyValue());
    }

    @Override
    public boolean isPaid() {
        return paid;
    }

    @Override
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public IPaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    @Override
    public void setPaymentInfo(IPaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @Override
    public ITripInfo getTripInfo() {
        return tripInfo;
    }

    @Override
    public void setTripInfo(ITripInfo tripInfo) {
        this.tripInfo = tripInfo;
    }
}
