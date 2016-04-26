/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp;

import id.co.qhodok.nlp.MachineTranslation.MachineTranslation;
import id.co.qhodok.nlp.MachineTranslation.Utils.Util;
import id.co.qhodok.nlp.MachineTranslation.model.WordReordering;
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
        MachineTranslation machineTranslation = new MachineTranslation();
        machineTranslation.training(source, target,"sample"+File.separatorChar+"dict.xml", 2,true);
        machineTranslation.translation("my name is andika putra permana",true);
        machineTranslation.save(System.getProperty("user.home") + File.separator + ".machine_translation");
    }

}
