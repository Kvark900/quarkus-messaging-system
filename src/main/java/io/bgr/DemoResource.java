package io.bgr;

import io.quarkus.panache.common.Sort;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/demo")
public class DemoResource {

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<DemoModel> hello() {
        return DemoModel.listAll(Sort.by("t0").descending());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String add(DemoModel demo) {
        demo.persist();
        return "OK";
    }
}