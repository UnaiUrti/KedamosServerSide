/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.restful;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import kedamosServerSide.entities.Client;
import kedamosServerSide.entities.EventManager;
import kedamosServerSide.entities.User;
import kedamosServerSide.entities.UserPrivilege;
import kedamosServerSide.security.Crypt;
import kedamosServerSide.security.Email;

/**
 *
 * @author Freak
 */
@Stateless
@Path("kedamosserverside.entities.user")
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "KedamosServerSidePU")
    private EntityManager em;

    public UserFacadeREST() {
        super(User.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML})
    public void create(User entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML})
    public void edit(@PathParam("id") Long id, User entity) {
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
    public User find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @GET
    @Path("validateLogin/{username}/{password}")
    @Produces({MediaType.APPLICATION_XML})
    public List<User> validateLogin(@PathParam("username") String username,
            @PathParam("password") String password) {
        List<User> user = null;
        String hashPassword = Crypt.hash(Crypt.decryptAsimetric(password));
        user = (List<User>) em.createNamedQuery("validateLogin")
                .setParameter("username", username)
                .getResultList();
        if (user.isEmpty()) {
            throw new NotFoundException();
        } else {
            if (!user.get(0).getPassword().equalsIgnoreCase(hashPassword)) {
                throw new NotAuthorizedException("La contraseña no coincide");
            }
        }
        return user;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
