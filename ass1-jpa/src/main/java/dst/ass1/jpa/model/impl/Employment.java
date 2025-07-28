package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IEmployment;
import dst.ass1.jpa.model.IEmploymentKey;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Employment implements IEmployment {

    @EmbeddedId
    private EmploymentKey employmentKey;

    private Date since;

    private Boolean active;

    @Override
    public IEmploymentKey getId() {
        return employmentKey;
    }

    @Override
    public void setId(IEmploymentKey employmentKey) {
        this.employmentKey = new EmploymentKey();
        this.employmentKey.setDriver(employmentKey.getDriver());
        this.employmentKey.setOrganization(employmentKey.getOrganization());
    }

    @Override
    public Date getSince() {
        return since;
    }

    @Override
    public void setSince(Date since) {
        this.since = since;
    }

    public Boolean isActive() {
        return active;
    }

    @Override
    public void setActive(Boolean active) {
        this.active = active;
    }
}
