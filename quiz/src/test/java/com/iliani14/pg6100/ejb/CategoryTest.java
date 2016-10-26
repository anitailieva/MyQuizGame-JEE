package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;
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
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by anitailieva on 26/10/2016.
 */

@RunWith(Arquillian.class)
public class CategoryTest {

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
    private DeleterEJB deleterEJB;

    @Before
    @After
    public void clearDatabase(){
        deleterEJB.deleteEntities(Category.class);
    }

    @Test
    public void testCreateCategory(){
        Category c = categoryEJB.createCategory("Science");
        assertNotNull(c.getId());
    }

    @Test
    public void testCategoryWithName() {
        categoryEJB.createCategory("Science");
        List<Category> categories = categoryEJB.getAllCategories();

        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("Science")));

    }
    @Test
    public void testGetAllCategories(){
        categoryEJB.createCategory("Science");
        categoryEJB.createCategory("Sports");


        assertEquals(2, categoryEJB.getAllCategories().size());
    }

    @Test
    public void testDeleteCategory(){
        String name = "Science";
        categoryEJB.createCategory(name);

        List<Category> categories = categoryEJB.getAllCategories();
        assertEquals(1, categories.size());

        long id = categories.get(0).getId();
        categoryEJB.deleteCategory(id);

        assertEquals(0, categoryEJB.getAllCategories().size());
    }


}
