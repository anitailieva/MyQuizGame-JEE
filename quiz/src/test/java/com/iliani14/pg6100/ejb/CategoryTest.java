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

    @EJB
    private QuestionEJB questionEJB;

    @Before
    @After
    public void clearDatabase(){
        deleterEJB.deleteEntities(SubSubCategory.class);
        deleterEJB.deleteEntities(SubCategory.class);
        deleterEJB.deleteEntities(Category.class);
    }

    @Test
    public void testCreateCategory(){
        long c = categoryEJB.createCategory("Science");
        assertNotNull(c);
    }

    @Test
    public void testCategoryByName() {
        String name = "Science";
        categoryEJB.createCategory(name);

        Category categories = categoryEJB.getCategoryByName(name);

        assertEquals(name, categories.getName());

    }
    @Test
    public void testGetAllCategories(){
        categoryEJB.createCategory("Science");
        categoryEJB.createCategory("Sports");



        assertEquals(2, categoryEJB.getAllCategories(2, false).size());
    }

    @Test
    public void testDeleteCategory(){
        long id = categoryEJB.createCategory("Science");
        categoryEJB.deleteCategory(id);

        assertNull(categoryEJB.findCategoryById(id));
    }

    @Test
    public void testUpdateCategory(){
        String name1 = "Science";
        String name2 = "Sports";

        long id = categoryEJB.createCategory(name1);
        assertTrue(categoryEJB.updateCategory(id, name2));
        assertEquals(name2, categoryEJB.findCategoryById(id).getName());
    }

    @Test
    public void testCreateSubCategory() {
        long c = categoryEJB.createCategory("Science");
        long sc = subCategoryEJB.createSubCategory(c, "Computer Science");

        assertNotNull(sc);
    }


    @Test
    public void testSubCategoryWithName() {
        long c = categoryEJB.createCategory("Science");
        subCategoryEJB.createSubCategory(c, "Computer Science");

        List<SubCategory> subCategories = subCategoryEJB.getAllSubCategories();

        assertTrue(subCategories.stream().anyMatch(s -> s.getName().equals("Computer Science")));


    }

    @Test
    public void testDeleteSubCategory(){
        long c = categoryEJB.createCategory("Science");
        long s =  subCategoryEJB.createSubCategory(c, "Computer Science");
        long ss = subSubCategoryEJB.createSubSubCategory(s, "Name");

        assertTrue(subCategoryEJB.deleteSubCategory(s));


    }

    @Test
    public void testUpdateSubCategory() {
        String sub1 = "Computer Science";
        String sub2 = "Football";

        long c = categoryEJB.createCategory("Science");
        long s = subCategoryEJB.createSubCategory(c, "Computer Science");


        subCategoryEJB.updateSubCategory(s, sub2);

        assertTrue(subCategoryEJB.updateSubCategory(s, sub2));
        assertEquals(sub2, subCategoryEJB.getAllSubCategories().get(0).getName());

    }

    @Test
    public void testGetSubCategoryByCategoryName(){
        String categoryName = "Science";
        String subcategoryName = "Computer Science";

        long category = categoryEJB.createCategory(categoryName);
        subCategoryEJB.createSubCategory(category, subcategoryName);

        List<SubCategory> subCategories = subCategoryEJB.getSubCategoryByCategoryName(categoryName);

        assertEquals(categoryName, subCategories.get(0).getCategory().getName());
    }

    @Test
    public void testGetSubCategoryByCategoryIdAndId(){
        long category = categoryEJB.createCategory("Sports");
        long subCategory = subCategoryEJB.createSubCategory(category, "Football");

        SubCategory subcategory = subCategoryEJB.getSubCategoryByCategoryIdAndOwnId(category, subCategory);

        assertEquals(category, subcategory.getCategory().getId().longValue());

        assertEquals(subCategory, subcategory.getId().longValue());
    }

    @Test
    public void testCreateSubSubCategory() {
        long c = categoryEJB.createCategory("Science");
        long sub = subCategoryEJB.createSubCategory(c, "Computer Science");
        long subsub = subSubCategoryEJB.createSubSubCategory(sub, "IOS");

        assertNotNull(subsub);
    }


    @Test
    public void testSubSubCategoryWithName() {
        long c = categoryEJB.createCategory("Science");
        long sub = subCategoryEJB.createSubCategory(c, "Computer Science");
        subSubCategoryEJB.createSubSubCategory(sub, "IOS");

        List<SubSubCategory> subSubCategories = subSubCategoryEJB.getAllSubSubCategories();

        assertTrue(subSubCategories.stream().anyMatch(s -> s.getName().equals("IOS")));
    }
    @Test
    public void testGetSubSubCategoryBySubCategoryName(){
        String categoryName = "Science";
        String subcategoryName = "Computer Science";
        String subsubcategoryName = "Java EE";

        long category = categoryEJB.createCategory(categoryName);
        long subcategory = subCategoryEJB.createSubCategory(category, subcategoryName);
        subSubCategoryEJB.createSubSubCategory(subcategory, subsubcategoryName);

        List<SubSubCategory> subsubcategories = subSubCategoryEJB.getSubSubCategoryBySubCategoryName(subcategoryName);

        assertEquals(subcategoryName, subsubcategories.get(0).getSubCategories().getName());

    }
    @Test
    public void testGetSubSubCategoryByCategoryIdAndSubCategoryId(){
        long category = categoryEJB.createCategory("Movies");
        long subcategory = subCategoryEJB.createSubCategory(category, "Thriller");
        subSubCategoryEJB.createSubSubCategory(subcategory, "Most watched");

        List<SubSubCategory> subsubcategory = subSubCategoryEJB.getSubSubCategoriesByCategoryIdAndSubCategoryId(category, subcategory);

        assertEquals(category, subsubcategory.get(0).getSubCategories().getCategory().getId().longValue());

        assertEquals(subcategory, subsubcategory.get(0).getSubCategories().getId().longValue());
    }

    @Test
    public void testGetSubSubCategoryByCategoryIdSubCategoryIdAndOwnId(){
        long cat = categoryEJB.createCategory("Movies");
        long subcat = subCategoryEJB.createSubCategory(cat, "Action");
        long subsubcat = subSubCategoryEJB.createSubSubCategory(subcat, "Top Rated");

        SubSubCategory subsubcategory = subSubCategoryEJB.getSubSubCategoryByCategoryIdSubCategoryIdAndId(cat, subcat, subsubcat);

        assertEquals(cat, subsubcategory.getSubCategories().getCategory().getId().longValue());

        assertEquals(subcat, subsubcategory.getSubCategories().getId().longValue());

        assertEquals(subsubcat, subsubcategory.getId().longValue());
    }

    @Test
    public void testDeleteSubSubCategory(){
        long catId = categoryEJB.createCategory("Cateogory");
        long subId = subCategoryEJB.createSubCategory(catId, "Name");
        long subSubId = subSubCategoryEJB.createSubSubCategory(subId, "Name");

        assertTrue(subSubCategoryEJB.deleteSubSubCategory(subSubId));

    }

    @Test
    public void testUpdateSubSubCategory() {
        String subsub1 = "IOS";
        String subsub2 = "JEE";

        long c = categoryEJB.createCategory("Science");
        long sub = subCategoryEJB.createSubCategory(c, "Computer Science");
        long subsub = subSubCategoryEJB.createSubSubCategory(sub, subsub1);

        subSubCategoryEJB.updateSubSubCategory(subsub, subsub2);

        assertTrue(subSubCategoryEJB.updateSubSubCategory(subsub, subsub2));
        assertEquals(subsub2, subSubCategoryEJB.getAllSubSubCategories().get(0).getName());


    }

}
