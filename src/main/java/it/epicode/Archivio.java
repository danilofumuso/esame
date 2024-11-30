package it.epicode;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.*;

public class Archivio {
    public static final Logger LOGGER = LoggerFactory.getLogger(Archivio.class);
    public static Scanner scanner = new Scanner(System.in);

    public static ArrayList<Catalogo> catalogo = new ArrayList<>();

    public static void aggiungiAlCatalogo() throws DuplicatoException {

        boolean aggiungi = true;

        while (aggiungi) {

            System.out.println("Cosa vuoi aggiungere al catalogo? Libro o Rivista?");
            String tipo = scanner.nextLine().toLowerCase();


            switch (tipo) {
                case "libro":
                    try {
                        System.out.println("Inserisci il codice ISBN del libro");
                        String libroIsbn = scanner.nextLine();
                        if (catalogo.stream().anyMatch(testo -> testo.getISBN().equals(libroIsbn))) {
                            throw new DuplicatoException("Esiste già un elemento con questo codice ISBN, riprova!");
                        }
                        System.out.println("Inserisci il titolo del libro");
                        String titoloLibro = scanner.nextLine();
                        int annoPubblicazioneLibro = 0;
                        try {
                            System.out.println("Inserisci l'anno di pubblicazione del libro");
                            annoPubblicazioneLibro = scanner.nextInt();
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Inserisci solo numeri in questo campo!");
                            scanner.nextLine();
                            continue;
                        }
                        int numeroPagineLibro = 0;
                        try {
                            System.out.println("Inserisci il numero di pagine del libro");
                            numeroPagineLibro = scanner.nextInt();
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Inserisci solo numeri in questo campo!");
                            scanner.nextLine();
                            continue;
                        }
                        System.out.println("Inserisci l'autore del libro");
                        String autore = scanner.nextLine();
                        System.out.println("Inserisci il genere del libro");
                        String genere = scanner.nextLine();

                        Libro libro = new Libro(libroIsbn, titoloLibro, annoPubblicazioneLibro, numeroPagineLibro, autore, genere);
                        catalogo.add(libro);
                        System.out.println("Libro aggiunto correttamente!");

                    } catch (DuplicatoException e) {
                        LOGGER.error(e::getMessage);
                        continue;
                    }
                    break;

                case "rivista":
                    try {
                        System.out.println("Inserisci il codice ISBN della rivista");
                        String rivistaIsbn = scanner.nextLine();
                        if (catalogo.stream().anyMatch(testo -> testo.getISBN().equals(rivistaIsbn))) {
                            throw new DuplicatoException("Esiste già un elemento con questo codice ISBN, riprova!");
                        }
                        System.out.println("Inserisci il titolo della rivista");
                        String titoloRivista = scanner.nextLine();
                        int annoPubblicazioneRivista = 0;
                        try {
                            System.out.println("Inserisci l'anno di pubblicazione della rivista");
                            annoPubblicazioneRivista = scanner.nextInt();
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Inserisci solo numeri in questo campo!");
                            scanner.nextLine();
                            continue;
                        }
                        int numeroPagineRivista = 0;
                        try {
                            System.out.println("Inserisci il numero di pagine della rivista");
                            numeroPagineRivista = scanner.nextInt();
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Inserisci solo numeri in questo campo!");
                            scanner.nextLine();
                            continue;
                        }
                        System.out.println("Inserisci la periodicità della rivista: settimanale, mensile o annuale?");
                        String periodicita = scanner.nextLine().toUpperCase();
                        if (!periodicita.equals("SETTIMANALE") && !periodicita.equals("MENSILE") && !periodicita.equals("ANNUALE")) {
                            throw new IllegalArgumentException("Periodicità non valida!");
                        }

                        Rivista rivista = new Rivista(rivistaIsbn, titoloRivista, annoPubblicazioneRivista, numeroPagineRivista, Periodicita.valueOf(periodicita));
                        catalogo.add(rivista);
                        System.out.println("Rivista aggiunta correttamente!");


                    } catch (DuplicatoException | IllegalArgumentException e) {
                        LOGGER.error(e::getMessage);
                        continue;
                    }
                    break;

                default:
                    System.out.println("Tipologia di testo non riconosciuta, inseriscine una valida!");
                    continue;
            }

            System.out.print("Vuoi aggiungere un altro elemento al catalogo? (si/no): ");
            String risposta = scanner.nextLine().toLowerCase();
            if (risposta.equals("no")) {
                aggiungi = false;
            }
        }
    }

    public static void ricercaConISBN(String codiceIsbn) throws IsbnNonTrovatoException {
        Optional<Catalogo> testoTrovato = catalogo.stream()
                .filter(testo -> testo.getISBN().equals(codiceIsbn))
                .findFirst();

        if (testoTrovato.isPresent()) {
            testoTrovato.get().mostraDettagli();
        } else {
            throw new IsbnNonTrovatoException("Nessun testo trovato con questo codice ISBN: " + codiceIsbn);
        }
    }

    public static void rimuoviConISBN(String codiceIsbn) throws IsbnNonTrovatoException {
        boolean testoDaEliminare = catalogo.removeIf(testo -> testo.getISBN().equals(codiceIsbn));

        if (testoDaEliminare) {
            System.out.println("Libro con ISBN " + codiceIsbn + " rimosso correttamente.");
        } else {
            throw new IsbnNonTrovatoException("Nessun testo trovato da rimuovere con questo codice ISBN: " + codiceIsbn);
        }
    }

    public static void ricercaConAnnoPubblicazione(int annoPubblicazione) throws AnnoPubblicazioneException {
        List<Catalogo> testiTrovati = catalogo.stream()
                .filter(testo -> testo.getAnnoPubblicazione() == annoPubblicazione)
                .toList();

        if (!testiTrovati.isEmpty()) {
            testiTrovati.forEach(Catalogo::mostraDettagli);
        } else {
            throw new AnnoPubblicazioneException("Nessun testo trovato per l'anno di pubblicazione: " + annoPubblicazione);
        }
    }

    public static void ricercaConAutore(String autore) throws AutoreNonTrovatoException {
        List<Catalogo> testiTrovati = catalogo.stream()
                .filter(testo -> ((Libro) testo).getAutore().toLowerCase().contains(autore.toLowerCase()))
                .toList();

        if (!testiTrovati.isEmpty()) {
            testiTrovati.forEach(Catalogo::mostraDettagli);
        } else {
            throw new AutoreNonTrovatoException("Nessun libro trovato con autore: " + autore);
        }
    }

    public static void aggiornaTestoPresenteInCatalogo(String codiceIsbn) throws IsbnNonTrovatoException {
        ricercaConISBN(codiceIsbn);

        Catalogo elemento = catalogo.stream()
                .filter(testo -> testo.getISBN().equals(codiceIsbn))
                .findFirst()
                .orElseThrow(() -> new IsbnNonTrovatoException("Nessun testo trovato con questo codice ISBN: " + codiceIsbn));

        System.out.println("Cosa vuoi aggiornare?");
        System.out.println("1- Titolo testo");
        System.out.println("2- Anno Pubblicazione");
        System.out.println("3- Numero di pagine testo");
        if (elemento instanceof Libro) {
            System.out.println("4- Autore");
            System.out.println("5- Genere");
        } else if (elemento instanceof Rivista) {
            System.out.println("0. Periodicità");
        }

        int modifica = scanner.nextInt();
        scanner.nextLine();

        switch (modifica) {
            case 1:
                System.out.println("Inserisci un nuovo titolo");
                elemento.setTitolo(scanner.nextLine());
                break;
            case 2:
                System.out.println("Inserisci un nuovo anno di pubblicazione");
                elemento.setAnnoPubblicazione(scanner.nextInt());
                scanner.nextLine();
                break;
            case 3:
                System.out.println("Inserisci nuovo numero di pagine");
                elemento.setPagine(scanner.nextInt());
                scanner.nextLine();
                break;
            case 4:
                if (elemento instanceof Libro) {
                    Libro libro = (Libro) elemento;
                    System.out.println("Inserisci nuovo autore");
                    libro.setAutore(scanner.nextLine());
                }
                break;
            case 5:
                if (elemento instanceof Libro) {
                    Libro libro = (Libro) elemento;
                    System.out.println("Inserisci nuovo genere");
                    libro.setGenere(scanner.nextLine());
                }
                break;
            case 0:
                if (elemento instanceof Rivista) {
                    Rivista rivista = (Rivista) elemento;
                    System.out.println("Scegli nuova periodicità (1-SETTIMANALE, 2-MENSILE, 3-SEMESTRALE)");
                    String periodicitaScelta = scanner.nextLine().toUpperCase();
                    rivista.setPeriodicita(Periodicita.valueOf(periodicitaScelta));
                }
        }
        System.out.println("Testo aggiornato con successo!");
    }

    public static void mostraStatisticheCatalogo() {
        if (catalogo.isEmpty()) {
            System.out.println("Il catalogo è vuoto.");
            return;
        }

        long numeroLibri = catalogo.stream()
                .filter(elemento -> elemento instanceof Libro)
                .count();

        long numeroRiviste = catalogo.stream()
                .filter(elemento -> elemento instanceof Rivista)
                .count();

        Catalogo testoPiuLungo = catalogo.stream()
                .max(Comparator.comparingInt(Catalogo::getPagine))
                .orElse(null);

        double mediaPagine = catalogo.stream()
                .mapToInt(Catalogo::getPagine)
                .average()
                .orElse(0.0);

        System.out.println("Statistiche del catalogo:");

        System.out.println("Numero totale di libri: " + numeroLibri);
        System.out.println("Numero totale di riviste: " + numeroRiviste);
        System.out.println("Elemento con il maggior numero di pagine: ");
        testoPiuLungo.mostraDettagli();
        System.out.println("Media delle pagine di tutti gli elementi: " + mediaPagine);
    }

    public static void main(String[] args) throws DuplicatoException {
        System.out.println("Benvenuto nel nostro Catalogo! Premi: ");

        boolean esecuzione = true;

        while (esecuzione) {
            System.out.println("0- Per uscire dal Catalogo!");
            System.out.println("1- Aggiungi un nuovo testo al catalogo");
            System.out.println("2- Ricerca un testo con ISBN");
            System.out.println("3- Rimuovi un testo con ISBN");
            System.out.println("4- Ricerca per anno di pubblicazione");
            System.out.println("5- Ricerca per autore");
            System.out.println("6- Aggiorna un testo esistente");
            System.out.println("7- Mostra statistiche del Catalogo");


            try {
                int sceltaUtente = scanner.nextInt();
                scanner.nextLine();

                switch (sceltaUtente) {
                    case 0:
                        System.out.println("Grazie per aver utilizzato il nostro catalogo. Arrivederci!");
                        esecuzione = false;
                        return;
                    case 1:
                        Archivio.aggiungiAlCatalogo();
                        break;
                    case 2:
                        System.out.println("Inserisci il codice ISBN");
                        String codiceIsbn = scanner.nextLine();
                        try {
                            Archivio.ricercaConISBN(codiceIsbn);
                        } catch (IsbnNonTrovatoException e) {
                            LOGGER.error(e::getMessage);
                        }
                        break;
                    case 3:
                        System.out.println("Inserisci il codice ISBN");
                        String codiceIsbnRimozione = scanner.nextLine();
                        try {
                            Archivio.rimuoviConISBN(codiceIsbnRimozione);
                        } catch (IsbnNonTrovatoException e) {
                            LOGGER.error(e::getMessage);
                        }
                        break;
                    case 4:
                        System.out.println("Inserisci anno di pubblicazione");
                        int annoPubblicazione = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            Archivio.ricercaConAnnoPubblicazione(annoPubblicazione);
                        } catch (AnnoPubblicazioneException e) {
                            LOGGER.error(e::getMessage);
                        }
                        break;
                    case 5:
                        System.out.println("Inserisci il nome dell'autore");
                        String nomeAutore = scanner.nextLine();
                        try {
                            Archivio.ricercaConAutore(nomeAutore);
                        } catch (AutoreNonTrovatoException e) {
                            LOGGER.error(e::getMessage);
                        }
                        break;
                    case 6:
                        System.out.println("Inserisci il codice ISBN");
                        String codiceIsbnModifica = scanner.nextLine();
                        try {
                            Archivio.aggiornaTestoPresenteInCatalogo(codiceIsbnModifica);
                        } catch (IsbnNonTrovatoException e) {
                            LOGGER.error(e::getMessage);
                        }
                        break;
                    case 7:
                        Archivio.mostraStatisticheCatalogo();
                        break;
                    default:
                        System.out.println("Scelta non valida. Per favore, inserisci un numero tra 0 e 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Inserisci solo numeri in questo campo!");
                scanner.nextLine();
            }

            System.out.print("Vuoi ancora interagire con il nostro catalogo? (si/no): ");
            String risposta = scanner.nextLine().toLowerCase();
            if (risposta.equals("no")) {
                System.out.println("Grazie per aver utilizzato il nostro catalogo. Arrivederci!");
                esecuzione = false;
            } else if (!risposta.equals("si")) {
                System.out.println("Inserisci soltanto si o no!");
            }
        }
    }
}

