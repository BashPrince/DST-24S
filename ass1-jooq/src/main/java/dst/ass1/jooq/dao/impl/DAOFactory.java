package dst.ass1.jooq.dao.impl;

import dst.ass1.jooq.dao.IDAOFactory;
import dst.ass1.jooq.dao.IRiderPreferenceDAO;
import org.jooq.DSLContext;

public class DAOFactory implements IDAOFactory {

    private DSLContext dslContext;

    public DAOFactory(DSLContext dlsContext) {
        this.dslContext = dlsContext;
    }

    @Override
    public IRiderPreferenceDAO createRiderPreferenceDao() {
        return new RiderPreferenceDAO(dslContext);
    }
}
