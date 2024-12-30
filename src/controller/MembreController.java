package controller;

import interfaces.MembreFile;
import module.Membre;

import java.io.*;
import java.util.*;

public class MembreController {
    public static List<Membre> membersList =new ArrayList<>();
    public static int MEMBER_ID_CPT=0;

    public static Membre findMember(int id){
        for(Membre m:membersList){
            if(m.getUid()==id){
                return m;
            }
        }
        return null;
    }

    public static void readMemberFile() {
        try{
            BufferedReader ois=new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\Membres.csv"));
            String s;
            while((s=ois.readLine())!=null) {
                if(s.isEmpty()){return;}
                Membre m=new Membre();
                String[] memberField=s.split(",");
                if(MEMBER_ID_CPT<Integer.parseInt(memberField[0])) {
                    MEMBER_ID_CPT=Integer.parseInt(memberField[0])+1;
                }
                m.setUid(Integer.parseInt(memberField[0]));
                m.setLastName(memberField[1]);
                m.setFirstName(memberField[2]);
                m.setPassword(memberField[3]);
                m.setAge(Integer.parseInt(memberField[4]));
                m.setAdresse(memberField[5]);
                membersList.add(m);
            }
            ois.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void WriteMemberFile() {
        try{
            BufferedWriter oos=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"\\src\\Membres.csv"));
            for(Membre m: membersList){
                oos.write(m.toString());
                oos.newLine();
            }
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
