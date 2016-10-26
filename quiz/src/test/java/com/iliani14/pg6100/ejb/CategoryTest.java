package com.iliani14.pg6100.ejb;

import com.iliani14.pg6100.entity.Category;
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
    private SubCategoryEJB subCategoryEJB;

    @EJB
    private SubSubCategoryEJB subSubCategoryEJB;

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

    @Test
    public void testUpdateCategory(){
        String name1 = "Science";
        String name2 = "Sports";

        categoryEJB.createCategory(name1);
        assertEquals(name1, categoryEJB.getAllCategories().get(0).getName());

        List<Category> categories = categoryEJB.getAllCategories();
        long id = categories.get(0).getId();
        categoryEJB.updateCategory(id, name2);

        assertEquals(name2, categoryEJB.getAllCategories().get(0).getName());
    }

    @Test
    public void testCreateSubCategory() {
        Category c = categoryEJB.createCategory("Science");
        SubCategory sc = subCategoryEJB.createSubCategory(c, "Computer Science");

        assertNotNull(sc.getId());
    }


    @Test
    public void testSubCategoryWithName() {
        Category c = categoryEJB.createCategory("Science");
        subCategoryEJB.createSubCategory(c, "Computer Science");

        List<SubCategory> subCategories = subCategoryEJB.getAllSubCategories();

        assertTrue(subCategories.stream().anyMatch(s -> s.getName().equals("Computer Science")));


    }

    @Test
    public void testDeleteSubCategory(){
        Category c = categoryEJB.createCategory("Science");
        subCategoryEJB.createSubCategory(c, "Computer Science");

        List<SubCategory> subCategories = subCategoryEJB.getAllSubCategories();
        assertEquals(1, subCategories.size());

        long id = subCategories.get(0).getId();
        subCategoryEJB.deleteSubCategory(id);

        assertEquals(0, subCategoryEJB.getAllSubCategories().size());

    }

    @Test
    public void testUpdateSubCategory() {
        String sub1 = "Computer Science";
        String sub2 = "Football";

        Category c = categoryEJB.createCategory("Science");
        subCategoryEJB.createSubCategory(c, "Computer Science");
        assertEquals(sub1, subCategoryEJB.getAllSubCategories().get(0).getName());

        List<SubCategory> subCat= subCategoryEJB.getAllSubCategories();
        long id = subCat.get(0).getId();
        subCategoryEJB.updateSubCategory(id, sub2);

        assertEquals(sub2, subCategoryEJB.getAllSubCategories().get(0).getName());

    }

    @Test
    public void testCreateSubSubCategory() {
        Category c = categoryEJB.createCategory("Science");
        SubCategory sub = subCategoryEJB.createSubCategory(c, "Computer Science");
        SubSubCategory subsub = subSubCategoryEJB.createSubSubCategory(sub, "IOS");

        assertNotNull(subsub.getId());
    }


    @Test
    public void testSubSubCategoryWithName() {
        Category c = categoryEJB.createCategory("Science");
        SubCategory sub = subCategoryEJB.createSubCategory(c, "Computer Science");
        subSubCategoryEJB.createSubSubCategory(sub, "IOS");

        List<SubSubCategory> subSubCategories = subSubCategoryEJB.getAllSubSubCategories();

        assertTrue(subSubCategories.stream().anyMatch(s -> s.getName().equals("IOS")));


    }

    @Test
    public void testDeleteSubSubCategory(){
        Category c = categoryEJB.createCategory("Science");
        SubCategory sub = subCategoryEJB.createSubCategory(c, "Computer Science");
        subSubCategoryEJB.createSubSubCategory(sub, "IOS");

        List<SubSubCategory> subSubCategories = subSubCategoryEJB.getAllSubSubCategories();
        assertEquals(1, subSubCategories.size());

        long id = subSubCategories.get(0).getId();
        subSubCategoryEJB.deleteSubSubCategory(id);

        assertEquals(0, subSubCategoryEJB.getAllSubSubCategories().size());
    }

    @Test
    public void testUpdateSubSubCategory() {
        String subsub1 = "IOS";
        String subsub2 = "JEE";

        Category c = categoryEJB.createCategory("Science");
        SubCategory sub = subCategoryEJB.createSubCategory(c, "Computer Science");
        subSubCategoryEJB.createSubSubCategory(sub, subsub1);

        assertEquals(subsub1, subSubCategoryEJB.getAllSubSubCategories().get(0).getName());

        List<SubSubCategory> subSubCategories = subSubCategoryEJB.getAllSubSubCategories();
        long id = subSubCategories.get(0).getId();

        subSubCategoryEJB.updateSubSubCategory(id, subsub2);

        assertEquals(subsub2, subSubCategoryEJB.getAllSubSubCategories().get(0).getName());



    }

}
