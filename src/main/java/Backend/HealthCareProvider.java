package Backend;

import DatabasePattern.UnitOfWork;
import java.time.LocalDateTime;

public class HealthCareProvider extends Account{

    private String postcode; // the postcode of health care providers

    public static HealthCareProvider createAccount(String name, String password, String postcode) {
        HealthCareProvider healthCareProvider = new HealthCareProvider(name, password, postcode);
        healthCareProvider.setLastLoginGhost();
        UnitOfWork.getCurrent().registerNew(healthCareProvider);
        return healthCareProvider;
    }

    public HealthCareProvider(String name) {
        super(name);
    }

    public HealthCareProvider(String name, String password, String postcode) {
        super(name);
        this.setPassword(password);
        this.setPostcode(postcode);
    }

    /**
     *
     * @return postcode of health care provider
     */
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
//        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     * add timeslots to the database
     * @param startTime the start time of the timeslot
     * @param endTime the end time of the timeslot
     * @param vaccine which vaccine will be used
     */
    public void addTimeslots(LocalDateTime startTime, LocalDateTime endTime, Vaccine vaccine) {
        Timeslot.createTimeslot(startTime, endTime, this, vaccine);
    }

    public void addRecord(VaccineRecipient vaccineRecipient, Timeslot timeslot, Vaccine vaccine) {
        Record.createRecord(vaccineRecipient, timeslot, this, vaccine);
    }

}
