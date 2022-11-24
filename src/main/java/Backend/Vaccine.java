package Backend;

import DatabasePattern.UnitOfWork;

public class Vaccine {

    private String vaccine;
    private int id;

    public Vaccine(String vaccine, int id){
        this.vaccine = vaccine;
        this.id = id;
    }

    public Vaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Vaccine createVaccine(String vaccineName) {
        Vaccine vaccine = new Vaccine(vaccineName);
        UnitOfWork.getCurrent().registerNew(vaccine);
        return vaccine;
    }

    public String getVaccine(){ return vaccine; }

}
