package br.udesc.mca.geolife.postgresql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class GeoLifePostgreSQLKMLExport {
    
    public void export(int userId, Long[] trajIds, File kmlFile) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/trajectories", "postgres", "admin");
        PreparedStatement ps = con.prepareStatement("Select longitude, latitude from geolife where userid=? and trajid=?");
        Kml kml = new Kml();

        Placemark p = kml.createAndSetPlacemark();
        p.setDescription("Trajectories");
        MultiGeometry mg = p.createAndSetMultiGeometry();

        for (Long trajId : trajIds) {
            LineString ls = mg.createAndAddLineString();
            List<Coordinate> lc = ls.createAndSetCoordinates();
            ps.setInt(1, userId);
            ps.setLong(2, trajId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lc.add(new Coordinate(rs.getDouble(1), rs.getDouble(2)));
            }
            rs.close();
        }
        ps.close();
        con.close();
        kml.marshal(kmlFile);
    }
    
    public void exportByUser(int userId, File kmlFile) throws Exception {
        List<Long> ll = new ArrayList<>();
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/trajectories", "postgres", "admin");
        PreparedStatement ps = con.prepareStatement("Select distinct trajid from geolife where userid=? order by trajid");
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ll.add(rs.getLong(1));
        }
        rs.close();
        ps.close();
        con.close();
        this.export(userId, ll.toArray(new Long[0]), kmlFile);
    }
    
    public static void main(String[] args) throws Exception {
        int userId = 128;
        //Long[] trajIds = {20081023025304L, 20081026134407L};
        File kmlFile = new File("teste.kml"); 
        GeoLifePostgreSQLKMLExport exp = new GeoLifePostgreSQLKMLExport();
        exp.exportByUser(userId, kmlFile);
    }
}
