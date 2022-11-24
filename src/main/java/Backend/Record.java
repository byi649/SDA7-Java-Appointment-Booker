package Backend;

import DatabasePattern.UnitOfWork;

import java.time.LocalDate;

public class Record {

    private int id;
    private VaccineRecipient vaccineRecipient; // the recipient who take the vaccine
    private Timeslot timeslot; // when does the vaccine recipient take the vaccine
    private HealthCareProvider healthCareProvider; // who provides the vaccine to the recipient
    private Vaccine vaccine; // the vaccine taken by the vaccine recipient
    private LocalDate date; // the date

    /**
     *
     * @return date of vaccination
     */
    public LocalDate getDate() {
        return date;
    }
    public HealthCareProvider getHealthCareProvider(){ return healthCareProvider; }
    public VaccineRecipient getVaccineRecipient() { return vaccineRecipient; }
    public Vaccine getVaccine(){ return vaccine; }
    public Timeslot getTimeSlot() { return timeslot; }
    public int getId(){ return id; }

    public Record(VaccineRecipient vaccineRecipient, Timeslot timeslot, HealthCareProvider healthCareProvider, Vaccine vaccine) {
        this.vaccineRecipient = vaccineRecipient;
        this.timeslot = timeslot;
        this.healthCareProvider = healthCareProvider;
        this.vaccine = vaccine;
        this.date = timeslot.getEndTime().toLocalDate();
    }

    public static Record createRecord(VaccineRecipient vaccineRecipient, Timeslot timeslot, HealthCareProvider healthCareProvider, Vaccine vaccine) {
        Record record = new Record(vaccineRecipient, timeslot, healthCareProvider, vaccine);
        UnitOfWork.getCurrent().registerNew(record);
        return record;
    }

}
