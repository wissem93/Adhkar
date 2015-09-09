package com.example.root.adhkar.classobject;

/**
 * Created by root on 28/06/15.
 */
public class Quran {
    int nbresura;
    String namesura;
    int pagestart, nbreAyeh;
    String makki;


    public Quran() {
    }

    public int getNbresura() {
        return nbresura;
    }

    public int getPagestart() {
        return pagestart;
    }

    public String getNamesura() {
        return namesura;
    }

    public int getNbreAyeh() {
        return nbreAyeh;
    }

    public String getMakki() {
        return makki;
    }

    public void setNbresura(int nbresura) {
        this.nbresura = nbresura;
    }

    public void setNamesura(String namesura) {
        this.namesura = namesura;
    }

    public void setNbreAyeh(int nbreAyeh) {
        this.nbreAyeh = nbreAyeh;
    }

    public void setPagestart(int pagestart) {
        this.pagestart = pagestart;
    }

    public void setMakki(String makki) {
        this.makki = makki;
    }

    public Quran(int nbresura,String namesura,
                 int pagestart, int nbreAyeh, String ismakki) {
        this.namesura = namesura;
        makki = ismakki;
        this.pagestart = pagestart;
        this.nbreAyeh = nbreAyeh;
        this.nbresura=nbresura;

    }
}
