package agencija;


import java.math.BigDecimal;

public class BankovniRacun{
    private int id;
    private String broj_racuna, jmbg;
    private BigDecimal stanje;
    public BankovniRacun(){}

    public BankovniRacun(int id, String broj_racuna, String jmbg, BigDecimal stanje) {
        this.id = id;
        this.broj_racuna = broj_racuna;
        this.jmbg = jmbg;
        this.stanje = stanje;
    }

    public int getId() {
        return id;
    }

    public String getBroj_racuna() {
        return broj_racuna;
    }

    public String getJmbg() {
        return jmbg;
    }

    public BigDecimal getStanje() {
        return stanje;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrojRacuna(String broj_racuna) {
        this.broj_racuna = broj_racuna;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public void setStanje(BigDecimal stanje) {
        this.stanje = stanje;
    }

    @Override
    public String toString() {
        return "id: " + id + ", broj racuna: " + broj_racuna + ", jmbg: " + jmbg + ", stanje: " + stanje;
    }
}
