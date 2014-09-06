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
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class GeolifePostgresqlKMLExport {
    public static void main(String[] args) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/trajectories", "postgres", "admin");
        int id = 0;
        long trajid = 20081023025304L; 
        Kml kml = new Kml();
        Placemark p = kml.createAndSetPlacemark();
        p.setDescription("Trajectory");
        LineString ls = p.createAndSetLineString();
        List<Coordinate> lc =  ls.createAndSetCoordinates();
        PreparedStatement ps = con.prepareStatement("Select latlon from geolife where id=? and trajid=?");
        ps.setInt(1, id);
        ps.setLong(2, trajid);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            PGpoint point = (PGpoint) rs.getObject(1);
            lc.add(new Coordinate(point.x, point.y));
        }
        rs.close();
        ps.close();
        con.close();
        kml.marshal(new File("teste.kml"));
    }
}
