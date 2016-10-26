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
        deleterEJB.deleteEntities(SubSubCategory.class);
        deleterEJB.deleteEntities(SubCategory.class);
        deleterEJB.deleteEntities(Category.class);

    }


    @Test
    public void testCreateQuestion(){
        Category c = categoryEJB.createCategory("Science");
        SubCategory sc = subCategoryEJB.createSubCategory(c, "Computer Science");
        SubSubCategory ssc = subSubCategoryEJB.createSubSubCategory(sc, "C#");

        List<String> answers = Arrays.asList("1995", "2000", "2010", "1990");
        String theCorrectAnswer = "2000";

        Question question = questionEJB.createQuestion(ssc, "When was C# created?", answers, theCorrectAnswer);

        assertNotNull(question.getId());

    }

    @Test
    public void testDeleteQuestion(){

    }

    @Test
    public void updateQuestion() {


    }

}
