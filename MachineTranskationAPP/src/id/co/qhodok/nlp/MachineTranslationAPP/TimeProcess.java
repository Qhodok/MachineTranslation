/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp.MachineTranslationAPP;

/**
 *
 * @author Andika
 */
public class TimeProcess {

    public int days;
    public int hours;
    public int minutes;
    public int second;
    public int milisecond;

    public TimeProcess(long time) {
        long temp = 0;
        this.days = (int) (time / (1000 * 60 * 60 * 24));
        temp = (time - ((1000 * 60 * 60 * 24) * this.days));
        this.hours = (int) (temp / (1000 * 60 * 60));
        temp = (temp - (this.hours * (1000 * 60 * 60)));
        this.minutes = (int) (temp / (1000 * 60));
        temp = (temp - (this.minutes * (1000 * 60)));
        this.second = (int) (temp / 1000);
        temp = temp - (this.second / 1000);
        this.milisecond = (int) temp;

    }
}
