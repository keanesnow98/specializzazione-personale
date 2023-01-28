package it.antonio.sp.rest;
/*

import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.TransactionManagement;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
*/

//@Path("print")
public class wsREST {


    //@GET
    public void create() {
        System.out.println("WS-CALL-POST");
    }

    /*@PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void edit(@PathParam("id") Integer id, Factura entity) {
        facturabean.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        facturabean.remove(facturabean.find(id));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Factura find(@PathParam("id") Integer id) {
        return facturabean.find(id);
    }*/

     /** Servicio de Listar Clientes **/
    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Factura> listar(@QueryParam("inicio") @DefaultValue("0") String inicio, 
            @QueryParam("cantidad") @DefaultValue("10") String cantidad, 
            @QueryParam("orderBy") @DefaultValue("id") String orderBy,
            @QueryParam("orderDir") @DefaultValue("ASC") String orderDir) {
        return facturabean.listar(inicio, cantidad, orderBy, orderDir);
    }

    @GET
    @Path("{from}/{to}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Factura> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return facturabean.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(facturabean.countBEAN());
    }
    */
    
}
