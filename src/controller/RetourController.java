package controller;
import module.Retour;

import java.io.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class RetourController {
    public static List<Retour> retourList=new ArrayList<>();
    public static int Retour_ID_CPT=0;

    public static void readRetourFile() {
        try {
            BufferedReader ois = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\Retours.csv"));
            String line;
            while((line=ois.readLine()) !=null){
                if(line.isEmpty()){return;}
                Retour re=new Retour();
                String[] returnFields =line.split(",");
                if(Retour_ID_CPT<Integer.parseInt(returnFields[0])){Retour_ID_CPT=Integer.parseInt(returnFields[0])+1;}
                re.setIdRetour(Integer.parseInt(returnFields[0]));
                re.setLivreretourne(LivreController.findBook(Integer.parseInt(returnFields[2])));
                re.setMembreemprunteur(MembreController.findMember(Integer.parseInt(returnFields[3])));
                re.setEmpruntretournee(EmpruntController.findEmprunt(Integer.parseInt(returnFields[1])));
                re.setDateRetour(Date.valueOf(returnFields[4]));

                retourList.add(re);
            }
            ois.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeRetourFile() {
        try{
            BufferedWriter oos=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\src\\Retours.csv"));
            for(Retour re:retourList){
                oos.write(re.toString());
                oos.newLine();
            }
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
