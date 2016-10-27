package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;
import com.iliani14.pg6100.entity.Question;
import com.iliani14.pg6100.entity.SubCategory;
import com.iliani14.pg6100.entity.SubSubCategory;
import com.iliani14.pg6100.util.DeleterEJB;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by anitailieva on 26/10/2016.
 */
@RunWith(Arquillian.class)
public class QuestionTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "com.iliani14.pg6100")
                .addClass(DeleterEJB.class)
                .addPackages(true, "org.apache.commons.codec")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private CategoryEJB categoryEJB;

    @EJB
    private SubCategoryEJB subCategoryEJB;

    @EJB
    private SubSubCategoryEJB subSubCategoryEJB;

    @EJB
    private QuestionEJB questionEJB;

    @EJB
    private DeleterEJB deleterEJB;

    @Before
    @After
    public void clearDatabase(){
        deleterEJB.deleteQuestions();
        deleterEJB.deleteEntities(SubSubCategory.class);
        deleterEJB.deleteEntities(SubCategory.class);
        deleterEJB.deleteEntities(Category.class);

    }


    @Test
    public void testCreateQuestion(){
        long c = categoryEJB.createCategory("Science");
        long sc = subCategoryEJB.createSubCategory(c, "Computer Science");
        long ssc = subSubCategoryEJB.createSubSubCategory(sc, "C#");

        List<String> answers = Arrays.asList("1995", "2000", "2010", "1990");
        String theCorrectAnswer = "2000";

        long question = questionEJB.createQuestion(ssc, "When was C# created?", answers, theCorrectAnswer);

        assertNotNull(question);

    }


    @Test
    public void testDeleteQuestion(){
        long c = categoryEJB.createCategory("Science");
        long sc = subCategoryEJB.createSubCategory(c, "Computer Science");
        long ssc = subSubCategoryEJB.createSubSubCategory(sc, "C#");

        List<String> answers = Arrays.asList("1995", "2000", "2010", "1990");
        String theCorrectAnswer = "2000";

        questionEJB.createQuestion(ssc, "When was C# created?", answers, theCorrectAnswer);

        List<Question> questions = questionEJB.getAllQuestions();
        assertEquals(1, questions.size());

        long id = questions.get(0).getId();
        questionEJB.deleteQuestion(id);

        assertEquals(0, questionEJB.getAllQuestions().size());
    }



    @Test
    public void testUpdateQuestion() {
        long c = categoryEJB.createCategory("Science");
        long sc = subCategoryEJB.createSubCategory(c, "Computer Science");
        long ssc = subSubCategoryEJB.createSubSubCategory(sc, "C#");

        String question1 = "When was Java created?";
        List<String> answers = Arrays.asList("1995", "2000", "2010", "1990");
        String theCorrectAnswer = "1995";

        questionEJB.createQuestion(ssc, question1, answers, theCorrectAnswer);

        assertEquals(question1, questionEJB.getAllQuestions().get(0).getText());

        List<Question> questions = questionEJB.getAllQuestions();

        long id = questions.get(0).getId();
        String question2 = "What year was Java created?";

        questionEJB.updateQuestion(id, question2);

        assertEquals(question2, questionEJB.getAllQuestions().get(0).getText());

    }

}
