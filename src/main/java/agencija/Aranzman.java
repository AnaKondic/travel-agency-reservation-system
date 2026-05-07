package agencija;

import java.sql.Date;

public class Aranzman {
    private String id, naziv_putovanja, destinacija, prevoz, cijena_aranzmana;
    private Date datum_polaska, datum_dolaska;
    private Integer Smjestaj_id;
    public Aranzman(){}

    public Aranzman(String id, String naziv_putovanja, String destinacija, String prevoz, String cijena_aranzmana, Date datum_polaska, Date datum_dolaska, Integer smjestaj_id) {
        this.id = id;
        this.naziv_putovanja = naziv_putovanja;
        this.destinacija = destinacija;
        this.prevoz = prevoz;
        this.cijena_aranzmana = cijena_aranzmana;
        this.datum_polaska = datum_polaska;
        this.datum_dolaska = datum_dolaska;
        this.Smjestaj_id = smjestaj_id;
    }

    public String getNaziv_putovanja() {
        return naziv_putovanja;
    }

    public String getDestinacija() {
        return destinacija;
    }

    public String getPrevoz() {
        return prevoz;
    }

    public Date getDatum_polaska() {
        return datum_polaska;
    }

    public Date getDatum_dolaska() {
        return datum_dolaska;
    }

    public String getCijena_aranzmana() {
        return cijena_aranzmana;
    }

    public String getId() {
        return id;
    }

    public Integer getSmjestaj_id() {
        return Smjestaj_id;
    }

    public void setNaziv_putovanja(String naziv_putovanja) {
        this.naziv_putovanja = naziv_putovanja;
    }

    public void setDestinacija(String destinacija) {
        this.destinacija = destinacija;
    }

    public void setPrevoz(String prevoz) {
        this.prevoz = prevoz;
    }

    public void setDatum_polaska(Date datum_polaska) {
        this.datum_polaska = datum_polaska;
    }

    public void setDatum_odlaska(Date datum_dolaska) {
        this.datum_dolaska = datum_dolaska;
    }

    public void setCijena_aranzmana(String cijena_aranzmana) {
        this.cijena_aranzmana = cijena_aranzmana;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSmjestaj_id(Integer smjestaj_id) {
        Smjestaj_id = smjestaj_id;
    }

    @Override
    public String toString() {
        return  naziv_putovanja + ", " + destinacija + ", " + prevoz + ", datum polaska: " + datum_polaska + ", datum dolaska: " + datum_dolaska + ", cijena: " + cijena_aranzmana + "KM";
    }
}
