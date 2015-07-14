package br.udesc.mca.trajectory.service.statistics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import br.udesc.mca.trajectory.function.Hello;

@Path("/rstatistics")
@Produces(MediaType.APPLICATION_JSON)
public class RStatisticsResource {
    @GET
    public String hello() throws Exception {
        Hello h = new Hello();
        return h.hello();
    }
}
