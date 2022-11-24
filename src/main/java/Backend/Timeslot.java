package Backend;

import DatabasePattern.TimeslotMapper;
import DatabasePattern.UnitOfWork;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Timeslot {

    private int id;
    private LocalDateTime startTime; // the start time of a time slot
    private LocalDateTime endTime; // the end time of a time slot
    private HealthCareProvider healthCareProvider; // the health care provider that makes the timeslot
    private VaccineRecipient recipient; // the vaccine recipient who book the timeslot, the default value is null
    private Vaccine vaccine; // the vaccine provided in the timeslot
    private Record vaccinationRecord;
    /**
     *
     * @param startTime set the start time of a time slot
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     *
     * @param endTime set the end time of a time slot
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     *
     * @param vaccineRecipient set the recipient who take the vaccine
     */
    public void setVaccineRecipient(VaccineRecipient vaccineRecipient) {
        this.recipient = vaccineRecipient;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    public void setVaccineRecipientGhost(VaccineRecipient vaccineRecipient) {
        this.recipient = vaccineRecipient;
    }

    /**
     *
     * @param vaccine set which vaccine will be used
     */
    public void setVaccine(Vaccine vaccine) {
        this.vaccine = vaccine;
        UnitOfWork.getCurrent().registerDirty(this);
    }

    /**
     *
     * @return vaccine
     */
    public Vaccine getVaccine() {
        return this.vaccine;
    }

    /**
     *
     * @return start time
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     *
     * @return end time
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     *
     * @return the health care provider
     */
    public HealthCareProvider getHealthCareProvider() {
        return healthCareProvider;
    }

    public VaccineRecipient getRecipient() { return recipient; }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return array list of timeslots
     */
    public static ArrayList<Timeslot> getAllTimeslots() {
        return TimeslotMapper.findAllTimeSlots();
    }

    /**
     *
     * @param postcode filter the timeslots by where the health care provider is
     * @return array list of timeslots
     */
    public static ArrayList<Timeslot> filterTimeslotByArea(String postcode) {
        return TimeslotMapper.findAvailableTimeSlotsByArea(postcode);
    }

    public static Timeslot getTimeslotByID(int id) {
        return TimeslotMapper.findTimeslotById(id);
    }

    /**
     *
     * @param HCPName filter the timeslots by the name of health care provider
     * @return array list of timeslots
     */
    public static ArrayList<Timeslot> filterTimeslotByHCP(String HCPName) {
        return TimeslotMapper.findAvailableTimeSlotsByHCP(HCPName);
    }

    public Timeslot(LocalDateTime startTime, LocalDateTime endTime, HealthCareProvider healthCareProvider,
                    VaccineRecipient recipient, Vaccine vaccine) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.healthCareProvider = healthCareProvider;
        this.recipient = recipient;
        this.vaccine = vaccine;
    }


    public Timeslot(LocalDateTime startTime, LocalDateTime endTime, HealthCareProvider HCP, Vaccine vaccine){
        this.startTime = startTime;
        this.endTime = endTime;
        this.healthCareProvider = HCP;
        this.recipient = null;
        this.vaccine = vaccine;
    }

    public static Timeslot createTimeslot(LocalDateTime startTime, LocalDateTime endTime, HealthCareProvider healthCareProvider,
                                          Vaccine vaccine) {
        Timeslot timeslot = new Timeslot(startTime, endTime, healthCareProvider, vaccine);
        UnitOfWork.getCurrent().registerNew(timeslot);
        return timeslot;
    }

    public boolean getEligible(String q1, String q2) {
        if (vaccine.getVaccine().equals("AstraZeneca")) {
            return Integer.parseInt(q1) >= 50 && q2.equals("No");
        } else if (vaccine.getVaccine().equals("Pfizer")) {
            return q2.equals("No");
        } else return q2.equals("No");
    }

}
