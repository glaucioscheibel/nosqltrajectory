package br.udesc.mca.geolife.postgresql;

import java.util.Date;

class TranspMode {
    Date dt1;
    Date dt2;
    String type;
    
    boolean isBetween(Date dt) {
        return dt.after(dt1) && dt.before(dt2);
    }

    @Override
    public String toString() {
        return "TranspMode [dt1=" + dt1 + ", dt2=" + dt2 + ", type=" + type + "]";
    }
}
