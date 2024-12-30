package module;

import controller.EmpruntController;

import java.sql.Date;

public class Emprunt {
    private int idE;
    private Livre livreEmprunte;
    private Membre emprunteur;
    private Date dateEmprunt;
    private Date dateRetourTheo;
    private Date dateRetourReel;

    public Emprunt(){idE= EmpruntController.EMPRUNT_ID_CPT++;}
    public Emprunt(Livre livreEmprunte,Membre emprunteur,Date dateEmprunt,Date dateRetourTheo,Date dateRetourReel){
        this.idE=EmpruntController.EMPRUNT_ID_CPT++;
        this.livreEmprunte=livreEmprunte;
        this.emprunteur=emprunteur;
        this.dateEmprunt=dateEmprunt;
        this.dateRetourTheo=dateRetourTheo;
        this.dateRetourReel=dateRetourReel;
    }

    public Date getDateRetourReel() {
        return dateRetourReel;
    }

    public void setDateRetourReel(Date dateRetourReel) {
        this.dateRetourReel = dateRetourReel;
    }

    public Date getDateRetourTheo() {
        return dateRetourTheo;
    }

    public void setDateRetourTheo(Date dateRetourTheo) {
        this.dateRetourTheo = dateRetourTheo;
    }

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public Membre getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Membre emprunteur) {
        this.emprunteur = emprunteur;
    }

    public Livre getLivreEmprunte() {
        return livreEmprunte;
    }

    public void setLivreEmprunte(Livre livreEmprunte) {
        this.livreEmprunte = livreEmprunte;
    }

    public int getIdE() {
        return idE;
    }

    public void setIdE(int idE) {
        this.idE = idE;
    }

    @Override
    public String toString() {
        return  idE +"," + livreEmprunte.getidBook() +"," + emprunteur.getUid() +"," + dateEmprunt +"," + dateRetourTheo +"," + dateRetourReel;
    }
}
