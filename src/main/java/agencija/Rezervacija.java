package agencija;

public class Rezervacija {
    private int Klijent_id;
    private String Aranzman_id, ukupna_cijena, placena_cijena;
    public Rezervacija(){}

    public Rezervacija(int klijent_id, String aranzman_id, String ukupna_cijena, String placena_cijena) {
        Klijent_id = klijent_id;
        Aranzman_id = aranzman_id;
        this.ukupna_cijena = ukupna_cijena;
        this.placena_cijena = placena_cijena;
    }

    public int getKlijent_id() {
        return Klijent_id;
    }

    public String getAranzman_id() {
        return Aranzman_id;
    }

    public String getUkupna_cijena() {
        return ukupna_cijena;
    }

    public String getPlacena_cijena() {
        return placena_cijena;
    }

    public void setKlijent_id(int klijent_id) {
        Klijent_id = klijent_id;
    }

    public void setAranzman_id(String aranzman_id) {
        Aranzman_id = aranzman_id;
    }

    public void setUkupna_cijena(String ukupna_cijena) {
        this.ukupna_cijena = ukupna_cijena;
    }

    public void setPlacena_cijena(String placena_cijena) {
        this.placena_cijena = placena_cijena;
    }

    @Override
    public String toString() {
        return "Klijent id " + Klijent_id + ", Aranzman id: " + Aranzman_id + ", ukupna cijena: " + ukupna_cijena + ", placena cijena: " + placena_cijena;
    }
}
