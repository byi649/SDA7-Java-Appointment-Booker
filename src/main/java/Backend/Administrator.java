package Backend;
import DatabasePattern.AccountMapper;
import DatabasePattern.UnitOfWork;
import DatabasePattern.VaccinationMapper;
import java.time.*;

public class Administrator extends Account{

    public static Administrator createAccount(String name, String password) {
        Administrator administrator = new Administrator(name, password);
        UnitOfWork.getCurrent().registerNew(administrator);
        return administrator;
    }

    public Administrator(String name) {
        super(name);
    }

    public Administrator(String name, String password) {
        super(name);
        this.setPassword(password);
    }

    /**
     * administrator could add new health care provider to the system
     * @param name the name of health care provider
     * @param postcode the postcode of health care provider
     */
    public void addHealthCareProvider(String name, String password, String postcode){
        HealthCareProvider.createAccount(name, password, postcode);
    }

    /**
     * administrator could add new vaccine to the system
     * @param vaccineName name of new vaccine
     */
    public void addVaccine(String vaccineName) {
        Vaccine.createVaccine(vaccineName);
    }

    /**
     * administrator could add new vaccine recipient to the system
     * @param name name of vaccine recipient
     * @param birthDate date of birth of vaccine recipient, including year so no need to add age manually
     */
    public void addVaccineRecipient(String name, String password, LocalDate birthDate) {
        VaccineRecipient.createAccount(name, password, birthDate);
    }

}
