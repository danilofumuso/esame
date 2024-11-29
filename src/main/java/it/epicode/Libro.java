package it.epicode;

public class Libro extends Catalogo {
    private String autore;
    private String genere;

    public Libro(String ISBN, String titolo, int annoPubblicazione, int pagine, String autore, String genere) {
        super(ISBN, titolo, annoPubblicazione, pagine);
        this.autore = autore;
        this.genere = genere;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    @Override
    public void mostraDettagli() {
        System.out.println("Libro trovato: " + getTitolo() +
                "\nAutore: " + getAutore() +
                "\nCodice ISBN: " + getISBN() +
                "\nNumero Pagine: " + getPagine() +
                "\nGenere: " + getGenere() +
                "\nAnno di Pubblicazione: " + getAnnoPubblicazione());

    }
}
