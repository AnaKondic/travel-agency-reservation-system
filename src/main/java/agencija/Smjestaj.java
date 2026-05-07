package agencija;

public class Smjestaj {
    private int id;
    private String naziv, vrsta_sobe,broj_zvjezdica, cjena_po_nocenju;
    public Smjestaj(){}

    public Smjestaj(String naziv, String vrsta_sobe, int id, String broj_zvjezdica, String cjena_po_nocenju) {
        this.naziv = naziv;
        this.vrsta_sobe = vrsta_sobe;
        this.id = id;
        this.broj_zvjezdica = broj_zvjezdica;
        this.cjena_po_nocenju = cjena_po_nocenju;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getVrsta_sobe() {
        return vrsta_sobe;
    }

    public int getId() {
        return id;
    }

    public String getBroj_zvjezdica() {
        return broj_zvjezdica;
    }

    public String getCjena_po_nocenju() {
        return cjena_po_nocenju;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setVrsta_sobe(String vrsta_sobe) {
        this.vrsta_sobe = vrsta_sobe;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBroj_zvjezdica(String broj_zvjezdica) {
        this.broj_zvjezdica = broj_zvjezdica;
    }

    public void setCjena_po_nocenju(String cjena_po_nocenju) {
        this.cjena_po_nocenju = cjena_po_nocenju;
    }

    @Override
    public String toString() {
        return "id " + id + ", naziv: " + naziv + ", broj zvjezdica: " + broj_zvjezdica + ", vrsta sobe: " + vrsta_sobe + ", cjena po nocenju: " + cjena_po_nocenju;
    }
}
