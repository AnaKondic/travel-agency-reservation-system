package agencija;

public class Admin {
    private int id;
    private String ime, prezime, korisnicko_ime, lozinka;
    public Admin(){}

    public Admin(int id, String ime, String prezime, String korisnicko_ime, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnicko_ime = korisnicko_ime;
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public int getId() {
        return id;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id " + id + ", ime: " + ime + ", prezime: " + prezime + ", korisnicko ime: " + korisnicko_ime + ", lozinka: " + lozinka;
    }
}
