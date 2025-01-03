package module;

import controller.RetourController;

import java.sql.Date;
import java.time.LocalDate;

public class Retour {
    private int idRetour;
    private Emprunt empruntretournee;
    private Livre livreretourne;
    private Membre membreemprunteur;
    private Date dateRetour;

    public Retour(){
        idRetour=RetourController.Retour_ID_CPT++;
    }

    public Retour(Emprunt empruntretournee,Livre livreretourne,Membre membreemprunteur,Date dateRetour){
        idRetour=RetourController.Retour_ID_CPT++;
        this.empruntretournee=empruntretournee;
        this.livreretourne=livreretourne;
        this.membreemprunteur=membreemprunteur;
        this.dateRetour=dateRetour;
    }
    public boolean setPenalite(){
        if(dateRetour.after(empruntretournee.getDateEmprunt())){
            long diffInMillis = dateRetour.getTime() - empruntretournee.getDateRetourTheo().getTime();
            long daysLate = diffInMillis / (1000 * 60 * 60 * 24);

            this.membreemprunteur.setPenalized(true);
            this.membreemprunteur.setFinPenalite(Date.valueOf(LocalDate.now().plusDays(2L * daysLate)));
            return true;
        }else{
            return false;
        }
    }



    public int getIdRetour() {
        return idRetour;
    }

    public void setIdRetour(int idRetour) {
        this.idRetour = idRetour;
    }

    public Emprunt getEmpruntretournee() {
        return empruntretournee;
    }

    public void setEmpruntretournee(Emprunt empruntretournee) {
        this.empruntretournee = empruntretournee;
    }

    public Livre getLivreretourne() {
        return livreretourne;
    }

    public void setLivreretourne(Livre livreretourne) {
        this.livreretourne = livreretourne;
    }

    public Membre getMembreemprunteur() {
        return membreemprunteur;
    }

    public void setMembreemprunteur(Membre membreemprunteur) {
        this.membreemprunteur = membreemprunteur;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    @Override
    public String toString() {
        return  idRetour +"," + empruntretournee.getIdE() +"," + livreretourne.getidBook() +"," + membreemprunteur.getUid() +"," + dateRetour ;
    }
}
