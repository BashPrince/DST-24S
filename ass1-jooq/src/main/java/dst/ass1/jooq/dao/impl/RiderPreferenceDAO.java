package dst.ass1.jooq.dao.impl;

import dst.ass1.jooq.dao.IRiderPreferenceDAO;
import dst.ass1.jooq.model.IRiderPreference;
import dst.ass1.jooq.model.impl.RiderPreference;
import dst.ass1.jooq.model.public_.tables.records.PreferenceRecord;
import dst.ass1.jooq.model.public_.tables.pojos.Preference;
import dst.ass1.jooq.model.public_.tables.records.RiderPreferenceRecord;
import org.jooq.DSLContext;

import static dst.ass1.jooq.model.public_.Tables.PREFERENCE;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.*;
import org.jooq.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static dst.ass1.jooq.model.public_.Tables.RIDER_PREFERENCE;

public class RiderPreferenceDAO implements IRiderPreferenceDAO {
    private DSLContext dslContext;

    public RiderPreferenceDAO(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void updatePreferences(IRiderPreference model) {
        DSLContext oldContext = dslContext;

        try {
            dslContext.transaction((Configuration trx) -> {
                // Set this instance's dslContext to new derived context so that
                // delete() and insert() work with the derived context
                this.dslContext = trx.dsl();
                // Full update instead of partial
                this.delete(model.getRiderId());
                this.insert(model);
            });
        } finally {
            this.dslContext = oldContext;
        }
    }

    @Override
    public IRiderPreference findById(Long id) {
        Record4<
                Long,
                String,
                String,
                List<Preference>
                > record = dslContext.select(
                        RIDER_PREFERENCE.RIDER_ID,
                        RIDER_PREFERENCE.AREA,
                        RIDER_PREFERENCE.VEHICLE_CLASS,
                        // Nested record here:
                        multiset(
                                select(
                                        PREFERENCE.ID,
                                        PREFERENCE.RIDER_ID,
                                        PREFERENCE.PREF_KEY,
                                        PREFERENCE.PREF_VALUE
                                )
                                        .from(PREFERENCE)
                                        .where(PREFERENCE.RIDER_ID.eq(id))


                        ).as("prefs").convertFrom(r -> r.map(mapping((a, b, c, d) -> new Preference(a, b, c, d))))
                )
                .from(RIDER_PREFERENCE)
                .where(RIDER_PREFERENCE.RIDER_ID.eq(id))
                .fetchOne();

        if (record == null)
            return null;

        RiderPreference riderPref = new RiderPreference();
        riderPref.setRiderId(record.get(RIDER_PREFERENCE.RIDER_ID));
        riderPref.setArea(record.get(RIDER_PREFERENCE.AREA));
        riderPref.setVehicleClass(record.get(RIDER_PREFERENCE.VEHICLE_CLASS));

        Map<String, String> prefs = new HashMap<>();

        for (Preference p : record.value4()) {
            prefs.put(p.getPrefKey(), p.getPrefValue());
        }

        riderPref.setPreferences(prefs);

        return riderPref;
    }

    @Override
    public List<IRiderPreference> findAll() {
        Result<Record5<
             Long,
             String,
             String,
             String,
             String>> result = dslContext.select(
                     PREFERENCE.RIDER_ID, PREFERENCE.PREF_KEY, PREFERENCE.PREF_VALUE, RIDER_PREFERENCE.AREA, RIDER_PREFERENCE.VEHICLE_CLASS)
                .from(RIDER_PREFERENCE)
                .join(PREFERENCE)
                .on(PREFERENCE.RIDER_ID.eq(RIDER_PREFERENCE.RIDER_ID))
                .fetch();

        // Map from rider id to preference map
        Map<Long, RiderPreference> riderIdToRiderPref = new HashMap<>();

        // Build up the relation in memory
        for (Record5<Long, String, String, String, String> rec : result) {
            if (riderIdToRiderPref.get(rec.value1()) == null) {
                RiderPreference riderPref = new RiderPreference();
                riderPref.setRiderId(rec.value1());
                riderPref.setArea(rec.value4());
                riderPref.setVehicleClass(rec.value5());
                riderPref.setPreferences(new HashMap<>());
                riderIdToRiderPref.put(rec.value1(), riderPref);
            }

            Map<String, String> prefs = riderIdToRiderPref.get(rec.value1()).getPreferences();
            prefs.put(rec.value2(), rec.value3());
        }

        return new LinkedList<>(riderIdToRiderPref.values());
    }

    @Override
    public IRiderPreference insert(IRiderPreference model) {
        dslContext.transaction((Configuration trx) -> {
            DSLContext trxDslContext = trx.dsl();

            RiderPreferenceRecord riderPref = trxDslContext.newRecord(RIDER_PREFERENCE);
            riderPref.setRiderId(model.getRiderId());
            riderPref.setArea(model.getArea());
            riderPref.setVehicleClass(model.getVehicleClass());
            riderPref.store();

            InsertValuesStep3<PreferenceRecord, Long, String, String> inserter =
                    trxDslContext.insertInto(
                            PREFERENCE,
                            PREFERENCE.RIDER_ID,
                            PREFERENCE.PREF_KEY,
                            PREFERENCE.PREF_VALUE);

            for (Map.Entry<String, String> entry : model.getPreferences().entrySet()) {
                inserter = inserter.values(model.getRiderId(), entry.getKey(), entry.getValue());
            }

            inserter.execute();
        });

        return model;
    }

    @Override
    public void delete(Long id) {
        dslContext.transaction((Configuration trx) -> {
            DSLContext trxDslContext = trx.dsl();

            trxDslContext.delete(PREFERENCE)
                    .where(PREFERENCE.RIDER_ID.eq(id))
                    .execute();

            trxDslContext.delete(RIDER_PREFERENCE)
                    .where(RIDER_PREFERENCE.RIDER_ID.eq(id))
                    .execute();
        });
    }
}
