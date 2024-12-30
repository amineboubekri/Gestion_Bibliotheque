package module;


import java.sql.Date;

public class Membre extends User {
    private boolean penalized=false;
    private Date finPenalite;

    public Membre(){
        super();
    }

    public boolean isPenalized() {
        return penalized;
    }

    public void setPenalized(boolean penalized) {
        this.penalized = penalized;
    }

    public Date getFinPenalite() {
        return finPenalite;
    }

    public void setFinPenalite(Date penalite) {
        this.finPenalite = penalite;
    }

    public Membre(int uid, String lastName, String firstName, String password, int age, String adresse){
        super(lastName,firstName,password,age,adresse);
    }
    public String getFullName(){
        return this.lastName+" "+this.firstName;
    }


}
