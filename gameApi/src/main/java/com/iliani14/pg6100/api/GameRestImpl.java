package com.iliani14.pg6100.api;

import com.iliani14.pg6100.Game;
import com.iliani14.pg6100.dto.AnswerDto;
import com.iliani14.pg6100.dto.GameConverter;
import com.iliani14.pg6100.dto.GameDto;
import com.iliani14.pg6100.dto.IdDto;
import com.iliani14.pg6100.dto.collection.ListDTO;
import com.iliani14.pg6100.dto.hal.HalLink;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import io.swagger.annotations.ApiParam;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by anitailieva on 15/11/2016.
 */
public class GameRestImpl implements GameRestApi{

    @Context
    UriInfo uriInfo;

    private final String quizApiWebAddress;

    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("DB");

    private EntityManager em = factory.createEntityManager();

    public GameRestImpl() {

        quizApiWebAddress = System.getProperty("quizApiAddress", "quizApi.com/quiz/api/");
    }

    @Override
    public synchronized ListDTO<GameDto> getAllActiveGames(Integer offset, Integer limit) {

        if(offset < 0){
            throw new WebApplicationException("Negative offset: "+offset, 400);
        }

        if(limit < 1){
            throw new WebApplicationException("Limit should be at least 1: "+limit, 400);
        }

        int maxFromDb = 50;

        @SuppressWarnings("unchecked")
        List<Game> list = em.createNamedQuery(Game.GET_ALL_ACTIVE_GAMES)
                .setMaxResults(maxFromDb)
                .getResultList();

        if(offset != 0 && offset >=  list.size()){
            throw new WebApplicationException("Offset "+ offset + " out of bound "+ list.size(), 400);
        }

        ListDTO<GameDto> listDTO = GameConverter.transform(list, offset, limit);

        UriBuilder builder = uriInfo.getBaseUriBuilder()
                .path("/category/randomQuizzes")
                .queryParam("limit", limit);

        listDTO._links.self = new HalLink(builder.clone()
                .queryParam("offset", offset)
                .build().toString()
        );

        if (!list.isEmpty() && offset > 0) {
            listDTO._links.previous = new HalLink(builder.clone()
                    .queryParam("offset", Math.max(offset - limit, 0))
                    .build().toString()
            );
        }
        if (offset + limit < list.size()) {
            listDTO._links.next = new HalLink(builder.clone()
                    .queryParam("offset", offset + limit)
                    .build().toString()
            );
        }

        return listDTO;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public synchronized Response createGame(
            @ApiParam("Optional parameter specifying number of quizzes in the game. " +
                    "Default value is 5 if absent")
                    String limit) {

        long subsubId = 1; //TODO

        String address = "http://" + quizApiWebAddress + "/category/randomQuizzes?limit=" +
                limit  +"&filter=ss_" + subsubId;

        Response response = new CallGameApi(address, Request.CREATE_GAME).execute();

        IdDto result = response.readEntity(IdDto.class);

        List<Long> questionIds = result.ids;

        Game game = new Game();
        game.setQuestions(questionIds);
        game.setActive(true);

        em.getTransaction().begin();
        em.persist(game);
        em.getTransaction().commit();

        return Response.status(200)
                .entity(game.getId())
                .location(UriBuilder
                        .fromUri("games/" + game.getId())
                        .build())
                .build();
    }

    @Override
    public GameDto getGameById(@ApiParam("The id of the game") Long id) {
        return GameConverter.transform(em.find(Game.class, id));
    }

    @Override
    public AnswerDto answerQuestion(Long id, String answer) {
        return null;
    }

    @Override
    public void endGame(@ApiParam("The id of the game") Long id) {
        Game game = em.find(Game.class, id);
        em.getTransaction().begin();
        em.remove(game);
        em.getTransaction().commit();

    }


    private class CallGameApi extends HystrixCommand<Response> {

        private final String address;
        private final Request request;

        @SuppressWarnings("WeakerAccess")
        protected CallGameApi(String address, Request request) {
            super(HystrixCommandGroupKey.Factory.asKey("Interactions with QuizApi"));
            this.address = address;
            this.request = request;
        }


        @Override
    protected Response run() throws Exception {

        URI uri = UriBuilder
                .fromUri(address)
                .build();
        Client client = ClientBuilder.newClient();

        Response response = null;
        switch (request) {
            case CREATE_GAME:
                response = client.target(uri)
                        .request("application/json")
                        .post(null);
                break;
            case CHECK_ANSWER:
                response = client.target(uri)
                        .request("application/json")
                        .get();
                break;
        }
        return response;
    }

    @Override
    protected Response getFallback() {
        //this is what is returned in case of exceptions or timeouts
        return null;
    }
}
}
