package controller;

import interfaces.LivreFile;
import module.Emprunt;
import module.Livre;
import java.util.*;
import java.io.*;

public class LivreController {
    public static List<Livre> livreslist=new ArrayList<>();
    public static int LIVRE_ID_CPT=0;

    public static Livre findBook(int id){
        for(Livre l:livreslist){
            if(l.getidBook()==id){
                return l;
            }
        }
        return null;
    }

    public static void readLivreFile() {
        try{
            BufferedReader ois=new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\Livres.csv"));
            String line;
            while((line=ois.readLine())!=null) {
                if(line.isEmpty()){return;}
                Livre l=new Livre();
                String[] livrefields=line.split(",");
                if(LIVRE_ID_CPT<Integer.parseInt(livrefields[0])){
                    LIVRE_ID_CPT=Integer.parseInt(livrefields[0])+1;
                }
                l.setIdBook(Integer.parseInt(livrefields[0]));
                l.setTitre(livrefields[1]);
                l.setAuteur(livrefields[2]);
                l.setAnneepub(Integer.parseInt(livrefields[3]));
                l.setGenre(livrefields[4]);
                l.setNbCopies(Integer.parseInt(livrefields[5]));
                livreslist.add(l);
            }
            ois.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void WriteLivreFile() {
        try{
            BufferedWriter oos=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\src\\Livres.csv"));
            for(Livre l:livreslist){
                oos.write(l.toString());
                oos.newLine();
            }
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
