/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.restful;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import kedamosServerSide.entities.Revise;
import kedamosServerSide.entities.ReviseId;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("kedamosserverside.entities.revise")
public class ReviseFacadeREST extends AbstractFacade<Revise> {

    @PersistenceContext(unitName = "KedamosServerSidePU")
    private EntityManager em;

    private ReviseId getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;user_id=user_idValue;event_id=event_idValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        kedamosServerSide.entities.ReviseId key = new kedamosServerSide.entities.ReviseId();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> user_id = map.get("user_id");
        if (user_id != null && !user_id.isEmpty()) {
            key.setUser_id(new java.lang.Long(user_id.get(0)));
        }
        java.util.List<String> event_id = map.get("event_id");
        if (event_id != null && !event_id.isEmpty()) {
            key.setEvent_id(new java.lang.Long(event_id.get(0)));
        }
        return key;
    }

    public ReviseFacadeREST() {
        super(Revise.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Revise entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") PathSegment id, Revise entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        kedamosServerSide.entities.ReviseId key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Revise find(@PathParam("id") PathSegment id) {
        kedamosServerSide.entities.ReviseId key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<Revise> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Revise> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("revisionByEmail/{event_id}/{email}")
    @Produces({MediaType.APPLICATION_XML})
    public Set<Revise> getRevisionsByMail(@PathParam("event_id") Long event_id, @PathParam("email") String email){
        
        Set<Revise> revise = null;
        try{
            revise = new HashSet<>(em.createNamedQuery("getRevisionByMail").setParameter("event_id", event_id).setParameter("email", email).getResultList());
        }catch(Exception e){
            
        }
        return revise;
    }
    /*           EN PROCESO PERO NO OBLIGATORIO
    @GET
    @Path("revisionByUsername/{event_id}/{username}")
    @Produces({MediaType.APPLICATION_XML})
    public Set<Revise> getRevisionsByUsername(@PathParam("event_id") Long event_id, @PathParam("username") String username){
        
        Set<Revise> revise = null;
        try{
            revise = new HashSet<>(em.createNamedQuery("getRevisionByMail").setParameter("event_id", event_id).setParameter("username", username).getResultList());
        }catch(Exception e){
            
        }
        return revise;
    }
    */
    @GET
    @Path("everyEventRevisions/{event_id}")
    @Produces({MediaType.APPLICATION_XML})
    public Set<Revise> getEveryEventRevisions(@PathParam("event_id") Long event_id){
        
        Set<Revise> revise = null;
        try{
            revise = new HashSet<>(em.createNamedQuery("getEveryEventRevisions").setParameter("event_id", event_id).getResultList());
        }catch(Exception e){
            
        }
        return revise;
    }
    
    @GET
    @Path("everyUserRevisions/{email}")
    @Produces({MediaType.APPLICATION_XML})
    public Set<Revise> getEveryUserRevisions(@PathParam("email") String email){
        
        Set<Revise> revise = null;
        try{
            revise = new HashSet<>(em.createNamedQuery("getEveryUserRevisions").setParameter("email", email).getResultList());
        }catch(Exception e){
            
        }
        return revise;
    }
    
    @DELETE
    @Path("deleteRevision/{event_id}/{email}")
    public void deleteRevision(@PathParam("event_id") Long event_id, @PathParam("email") String email){
        try{
            em.createNamedQuery("deleteRevision").setParameter("event_id", event_id).setParameter("email", email).executeUpdate();
        }catch(Exception e){
            
        } 
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
