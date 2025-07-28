package dst.ass1.jpa.model.impl;

import dst.ass1.jpa.model.*;
import dst.ass1.jpa.util.Constants;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedList;

@Entity
public class Organization implements IOrganization {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(targetEntity = Employment.class)
    private Collection<IEmployment> employments = new LinkedList<>();

    @ManyToMany(targetEntity = Vehicle.class)
    private Collection<IVehicle> vehicles = new LinkedList<>();

    @ManyToMany(targetEntity = Organization.class)
    @JoinTable(
            name = Constants.J_ORGANIZATION_PARTS,
            joinColumns = @JoinColumn(
                    name = Constants.I_ORGANIZATION_PARTS
            ),
            inverseJoinColumns = @JoinColumn(
                    name = Constants.I_ORGANIZATION_PART_OF
            )
    )
    private Collection<IOrganization> parts = new LinkedList<>();

    @ManyToMany(targetEntity = Organization.class, mappedBy = "parts")
    private Collection<IOrganization> partOf = new LinkedList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Collection<IVehicle> getVehicles() {
        return vehicles;
    }

    @Override
    public void setVehicles(Collection<IVehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public void addVehicle(IVehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    @Override
    public Collection<IOrganization> getPartOf() {
        return partOf;
    }

    @Override
    public void setPartOf(Collection<IOrganization> partOf) {
        this.partOf = partOf;
    }

    @Override
    public Collection<IOrganization> getParts() {
        return parts;
    }

    @Override
    public void setParts(Collection<IOrganization> parts) {
        this.parts = parts;
    }

    @Override
    public void addPart(IOrganization part) {
        this.parts.add(part);
    }

    @Override
    public void addPartOf(IOrganization partOf) {
        this.partOf.add(partOf);
    }

    @Override
    public Collection<IEmployment> getEmployments() {
        return employments;
    }

    @Override
    public void setEmployments(Collection<IEmployment> employments) {
        this.employments = employments;
    }

    @Override
    public void addEmployment(IEmployment employment) {
        this.employments.add(employment);
    }
}
