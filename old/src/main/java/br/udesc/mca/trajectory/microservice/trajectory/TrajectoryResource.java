package br.udesc.mca.trajectory.microservice.trajectory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import br.udesc.mca.trajectory.dao.PersistenceDAO;
import br.udesc.mca.trajectory.dao.PersistenceModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/trajectory")
public class TrajectoryResource {
    private final PersistenceModel model;

    public TrajectoryResource(String persistenceModel) {
        this.model = PersistenceModel.valueOf(persistenceModel);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public br.udesc.mca.trajectory.model.Trajectory get(@QueryParam("id") Long id) {
        PersistenceDAO dao = PersistenceDAO.getInstance(model);
        return dao.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String post(String json) {
        // TODO: rever parametro history/process... na conversao para json volta
        // como "process" e na conversao para objeto tem que ser "history"
        ObjectMapper mapper = new ObjectMapper();
        br.udesc.mca.trajectory.model.Trajectory t = null;
        try {
            t = mapper.readValue(json, br.udesc.mca.trajectory.model.Trajectory.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\": \"ERROR\", \"message\":\"" + e.getMessage() + "\"}";
        }
        PersistenceDAO dao = PersistenceDAO.getInstance(model);

        dao.store(t);

        return "{\"status\": \"OK\", \"id\": " + t.getId() + "}";
    }
}
