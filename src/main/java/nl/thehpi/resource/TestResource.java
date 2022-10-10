package nl.thehpi.resource;

import nl.thehpi.entity.Hans;
import nl.thehpi.entity.HansChild;
import nl.thehpi.entity.HansFriend;

import javax.ejb.Stateless;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Stateless
@Path("test")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class TestResource {

  @PersistenceContext
  EntityManager em;

  @GET
  @Path("hans/{id}")
  public Hans getHans(@PathParam("id") String id) {
    return em.find(Hans.class, id);
  }

  @GET
  @Path("children/{id}")
  public HansChild getChild(@PathParam("id") String id) {
    return em.find(HansChild.class, id);
  }

  @POST
  @Path("hans/{test}")
  public Hans create(@PathParam("test") String test) {
    Hans hans = new Hans();
    hans.setTest(test);
    em.persist(hans);
    return hans;
  }

  @POST
  @Path("hans/{id}/children/{test}")
  public HansChild createChild(@PathParam("id") String id, @PathParam("test") String test) {
    Hans hans = em.find(Hans.class, id);
    HansChild child = new HansChild();
    child.setTest(test);
    hans.addChild(child);
    return child;
  }

  @POST
  @Path("hans/{id}/friend/{test}")
  public HansFriend createFriend(@PathParam("id") String id, @PathParam("test") String test) {
    Hans hans = em.find(Hans.class, id);
    HansFriend friend = new HansFriend();
    friend.setTest(test);
    hans.addFriend(friend);
    return friend;
  }

  @PUT
  @Path("children/{id}/{test}")
  public HansChild updateChild(@PathParam("id") String id, @PathParam("test") String test) {
    HansChild child = em.find(HansChild.class, id);
    child.setTest(test);
    return child;
  }

  @PUT
  @Path("hans/{id}/evict")
  public boolean evictHans(@PathParam("id") String id) {
    Cache cache = em.getEntityManagerFactory().getCache();
    boolean inCache = cache.contains(Hans.class, id);
    cache.evict(Hans.class,id);
    return inCache;
  }

  @PUT
  @Path("children/{id}/evict")
  public boolean evictHansChild(@PathParam("id") String id) {
    Cache cache = em.getEntityManagerFactory().getCache();
    boolean inCache = cache.contains(HansChild.class, id);
    cache.evict(HansChild.class, id);
    return inCache;
  }
}
