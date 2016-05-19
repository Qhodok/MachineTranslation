/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.nlp;

import id.co.nlp.MachineTranslation.MachineTranslation;
import id.co.nlp.MachineTranslation.Utils.Util;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Andika
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String target = Util.read("sample" + File.separatorChar + "ind.txt").trim().toLowerCase();
        String source = Util.read("sample" + File.separatorChar + "eng.txt").trim().toLowerCase();
        MachineTranslation machineTranslation = new MachineTranslation(System.getProperty("user.home") + File.separator + ".machine_translation");
        machineTranslation.training(source, target,"sample"+File.separatorChar+"dictionary.xml", 2,true);
        System.out.println(machineTranslation.translation("my name is hhaaha hahaha",true));
        //machineTranslation.save(System.getProperty("user.home") + File.separator + ".machine_translation");
    }

}
