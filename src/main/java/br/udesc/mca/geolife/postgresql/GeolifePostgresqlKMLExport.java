package br.udesc.mca.geolife.postgresql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.postgresql.geometric.PGpoint;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class GeolifePostgresqlKMLExport {
    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/trajectories", "postgres", "admin");
        PreparedStatement ps = con.prepareStatement("Select latlon from geolife where id=? and trajid=?");
        int id = 0;
        long[] trajids = {20081023025304L, 20081026134407L};
        Kml kml = new Kml();
        
        Placemark p = kml.createAndSetPlacemark();
        p.setDescription("Trajectories");
        MultiGeometry mg = p.createAndSetMultiGeometry();
        
        for (long trajid : trajids) {
            
            LineString ls = mg.createAndAddLineString();
            List<Coordinate> lc = ls.createAndSetCoordinates();
            ps.setInt(1, id);
            ps.setLong(2, trajid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PGpoint point = (PGpoint) rs.getObject(1);
                lc.add(new Coordinate(point.y, point.x));
            }
            rs.close();
        }
        ps.close();
        con.close();
        kml.marshal(new File("teste.kml"));
    }
}
