package Backend;
import DatabasePattern.AccountMapper;
import DatabasePattern.UnitOfWork;
import DatabasePattern.VaccinationRecordMapper;

import java.time.*;
import java.util.ArrayList;

public class VaccineRecipient extends Account {

    private int id;
    private LocalDate birthDate; // birthdate of vaccine recipient, including year
    private int age; // age of vaccine recipient
    private Vaccine vaccine;

    public VaccineRecipient(String name, String password, LocalDate birthDate) {
        super(name);
        this.setPassword(password);
        this.setBirthDate(birthDate);
    }

    public static VaccineRecipient createAccount(String name, String password, LocalDate birthDate) {
        VaccineRecipient vaccineRecipient = new VaccineRecipient(name, password, birthDate);
        vaccineRecipient.setLastLoginGhost();
        UnitOfWork.getCurrent().registerNew(vaccineRecipient);
        return vaccineRecipient;
    }

    public VaccineRecipient(String name) {
        super(name);
    }

    // Lazy load
    public Vaccine getVaccine() {
        if (vaccine == null) {
            ArrayList<Record> record = VaccinationRecordMapper.findVaccinationRecordsByRecipientId(this.getId());
            if (record.size() > 0) {
                this.vaccine = record.get(0).getVaccine();
            }
        }
        return vaccine;
    }

    /**
     *
     * @param birthDate set birthdate of the vaccine recipient and calculate the age
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        int currentYear = LocalDate.now().getYear();
        int birthYear = birthDate.getYear();
        this.age = currentYear - birthYear;
//        UnitOfWork.getCurrent().registerDirty(this);
    }

    // Ghost
    public LocalDate getBirthDate() {
        if (this.birthDate == null) {
            VaccineRecipient account = (VaccineRecipient) AccountMapper.findAccountByName(this.getName());
            this.birthDate = account.birthDate;
            this.id = account.getId();
            this.setPassword(account.getPassword());
        }
        return this.birthDate;
    }

}
