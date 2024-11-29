package it.epicode;

public class Rivista extends Catalogo{
   private Periodicita periodicita;

    public Rivista(String ISBN, String titolo, int annoPubblicazione, int pagine, Periodicita periodicita ) {
        super(ISBN, titolo, annoPubblicazione, pagine);
        this.periodicita=periodicita;
    }

    public Periodicita getPeriodicita() {
        return periodicita;
    }

    public void setPeriodicita(Periodicita periodicita) {
        this.periodicita = periodicita;
    }

    public void mostraDettagli() {
        System.out.println("Rivista trovata: " + getTitolo() +
                "\nCodice ISBN: " + getISBN() +
                "\nNumero Pagine: " + getPagine() +
                "\nAnno di Pubblicazione: " + getAnnoPubblicazione() +
                "\nPeriodicità: " + getPeriodicita());

    }

}
