package dst.ass1.jooq.model.impl;

import dst.ass1.jooq.model.IRiderPreference;

import java.util.Map;
import java.util.Objects;

public class RiderPreference implements IRiderPreference {
    private Long riderId;
    private String area;

    private String vehicleClass;

    private Map<String, String> preferences;


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(String vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long personId) {
        this.riderId = personId;
    }

    public Map<String, String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RiderPreference that = (RiderPreference) obj;

        return this.getRiderId().equals(that.getRiderId()) &&
                this.getArea().equals(that.getArea()) &&
                this.getVehicleClass().equals(that.getVehicleClass()) &&
                this.getPreferences().equals(that.getPreferences());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.getRiderId(), this.getArea(), this.getVehicleClass(), this.getPreferences().hashCode());
    }
}
