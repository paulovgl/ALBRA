package br.com.albra;

public class Obras {

    private String NO;
    private String LI;
    private String LO;
    private String DES;

    public Obras(String NO, String LI, String LO, String DES) {
        this.NO = NO;
        this.LI = LI;
        this.LO = LO;
        this.DES = DES;
    }

    public Obras() {
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }

    public String getLI() {
        return LI;
    }

    public void setLI(String LI) {
        this.LI = LI;
    }

    public String getLO() {
        return LO;
    }

    public void setLO(String LO) {
        this.LO = LO;
    }

    public String getDES() {
        return DES;
    }

    public void setDES(String DES) {
        this.DES = DES;
    }
}
