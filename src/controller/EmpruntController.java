package controller;

import module.Emprunt;
import module.Livre;

import java.io.*;
import java.sql.Date;
import java.util.*;

public class EmpruntController  {
    public static List<Emprunt> empruntList=new ArrayList<>();
    public static int EMPRUNT_ID_CPT=0;

    public static Emprunt findEmprunt(int id){
        for(Emprunt e:empruntList){
            if(e.getIdE()==id){
                return e;
            }
        }
        return null;
    }
    public static void readEmpruntFile() {
        try {
            BufferedReader ois = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\Emprunts.csv"));
            String line;
            while((line=ois.readLine()) !=null){
                if(line.isEmpty()){return;}
                Emprunt e=new Emprunt();
                String[] empruntFields=line.split(",");
                if(EMPRUNT_ID_CPT<Integer.parseInt(empruntFields[0])){EMPRUNT_ID_CPT=Integer.parseInt(empruntFields[0])+1;}
                e.setIdE(Integer.parseInt(empruntFields[0]));
                e.setLivreEmprunte(LivreController.findBook(Integer.parseInt(empruntFields[1])));
                e.setEmprunteur(MembreController.findMember(Integer.parseInt(empruntFields[2])));
                e.setDateEmprunt(Date.valueOf(empruntFields[3]));
                e.setDateRetourTheo(Date.valueOf(empruntFields[4]));
                if(empruntFields[5].equals("null")){
                    e.setDateRetourReel(null);
                }else {
                    e.setDateRetourReel(Date.valueOf(empruntFields[5]));
                }
                empruntList.add(e);
            }
            ois.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeEmpruntFile() {
        try{
            BufferedWriter oos=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\src\\Emprunts.csv"));
            for(Emprunt e:empruntList){
                oos.write(e.toString());
                oos.newLine();
            }
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
