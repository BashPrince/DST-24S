package dst.ass1.jpa.dao.impl;

import dst.ass1.jpa.dao.IVehicleDAO;
import dst.ass1.jpa.model.IVehicle;

import javax.persistence.EntityManager;
import java.util.List;

public class VehicleDAO implements IVehicleDAO {
    private EntityManager em;

    public VehicleDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public IVehicle findById(Long id) {
        return (IVehicle) this.em.createQuery(
                        "SELECT v FROM Vehicle v WHERE v.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<IVehicle> findAll() {
        return (List<IVehicle>) this.em.createQuery("SELECT v FROM Vehicle v").getResultList();
    }
}
