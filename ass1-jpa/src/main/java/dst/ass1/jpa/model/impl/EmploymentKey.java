package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.IDriver;
import dst.ass1.jpa.model.IEmploymentKey;
import dst.ass1.jpa.model.IOrganization;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmploymentKey implements IEmploymentKey, Serializable {
    @ManyToOne(targetEntity = Driver.class)
    private IDriver driver;

    @ManyToOne(targetEntity = Organization.class)
    private IOrganization organization;

    @Override
    public IDriver getDriver() {
        return driver;
    }

    @Override
    public void setDriver(IDriver driver) {
        this.driver = driver;
    }

    @Override
    public IOrganization getOrganization() {
        return organization;
    }

    @Override
    public void setOrganization(IOrganization organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmploymentKey that = (EmploymentKey) obj;
        return Objects.equals(this.driver.getId(), that.driver.getId()) && Objects.equals(this.organization.getId(), that.organization.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.driver.getId(), this.organization.getId());
    }
}
