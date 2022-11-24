package DatabasePattern;

import Backend.*;

public interface IDataMapper {
    public boolean insert(Object object);
    public boolean delete(Object object);
    public boolean update(Object object);

    public static IDataMapper getMapper(Object object) {
        if (object instanceof Account) {
            return new AccountMapper();
        } else if (object instanceof Record) {
            return new VaccinationRecordMapper();
        } else if (object instanceof Timeslot) {
            return new TimeslotMapper();
        } else if (object instanceof Vaccine) {
            return new VaccinationMapper();
        }
        return null;
    }
}
