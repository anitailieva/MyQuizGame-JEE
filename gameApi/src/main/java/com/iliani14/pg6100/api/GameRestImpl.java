package com.iliani14.pg6100.api;

import com.iliani14.pg6100.Game;
import com.iliani14.pg6100.dto.AnswerDto;
import com.iliani14.pg6100.dto.GameConverter;
import com.iliani14.pg6100.dto.GameDto;
import io.swagger.annotations.ApiParam;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by anitailieva on 15/11/2016.
 */
public class GameRestImpl implements GameRestApi{


    private final String webAddress;

    private EntityManagerFactory factory = Persistence.createEntityManagerFactory("DB");

    private EntityManager em = factory.createEntityManager();

    public GameRestImpl() {

        webAddress = System.getProperty("quizApiAddress", "api.fixer.io");
    }



    @Override
    public List<GameDto> getAllActiveGames() {
        return GameConverter.transform(em.createNamedQuery(Game.GET_ALL_ACTIVE_GAMES).getResultList());
    }

    @Override
    public Response createNewGame(@ApiParam("Number of quizzes in the game. 5 by default") String numberOfQuestions) {
        return null;
    }

    @Override
    public GameDto getGameById(@ApiParam("The id of the game") Long id) {
        return GameConverter.transform(em.find(Game.class, id));
    }

    @Override
    public AnswerDto answerTheQuestion(@ApiParam("The id of the game") Long id, @ApiParam("The answer to the question") String correctAnswer) {
        return null;
    }

    @Override
    public void endGame(@ApiParam("The id of the game") Long id) {
        Game game = em.find(Game.class, id);
        em.getTransaction().begin();
        em.remove(game);
        em.getTransaction().commit();

    }

}
