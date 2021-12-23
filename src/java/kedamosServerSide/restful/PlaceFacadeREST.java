/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.restful;

import java.util.List;
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
import kedamosServerSide.entities.Event;
import kedamosServerSide.entities.Place;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("kedamosserverside.entities.place")
public class PlaceFacadeREST extends AbstractFacade<Place> {

    @PersistenceContext(unitName = "KedamosServerSidePU")
    private EntityManager em;
    
    public PlaceFacadeREST() {
        super(Place.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(Place entity) {
        if(entity.getEvents()!=null){
            for(Event event: entity.getEvents()){
                if(!em.contains(event)){
                    em.merge(event);
                    //em.flush();
                }
            }
        }
        super.create(entity);
        
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, Place entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Place find(@PathParam("id") Long id) {
        return super.find(id);
    }
    
    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<Place> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    public List<Place> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("placeByAddress/{address}")
    @Produces({MediaType.APPLICATION_XML})
    public Place getPlaceByAddress(@PathParam("address") String address){
        
        Place place = null;
        try{
            place = (Place) em.createNamedQuery("getPlaceByAddress").setParameter("address", address).getSingleResult();
        }catch(Exception e){
            
        }
        return place;
    }
    
    @DELETE
    @Path("deleteByAddress/{address}")
    public void deletePlaceByAddress(@PathParam("address") String address){
        
        try{
            em.createNamedQuery("deletePlaceByAddress").setParameter("address", address);
        }catch(Exception e){
            
        }
    }
    
    /*@PUT
    @Path("updateByAddress/{address}")
    @Consumes(MediaType.APPLICATION_XML)
    public void updateByAddress(@PathParam("address") String address){
        try{
            em.createNamedQuery("updateByAddress").setParameter("address", address);
        }catch(Exception e){
            
        }
    }*/
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
