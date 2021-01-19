package auryadov.springframework.recipe.controllers;

import auryadov.springframework.recipe.domain.Recipe;
import auryadov.springframework.recipe.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {

    IndexController indexController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        indexController = new IndexController(recipeService);
    }

    @Test
    void testMockMVC() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();

        mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void getIndexPage() {
        Set<Recipe> recipeSet = new HashSet<>();
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        recipe1.setId(1L);
        recipe2.setId(2L);
        recipeSet.add(recipe1);
        recipeSet.add(recipe2);

        when(recipeService.getRecipes()).thenReturn(recipeSet);
        String index = indexController.getIndexPage(model);

        ArgumentCaptor<Set<Recipe>> captor = ArgumentCaptor.forClass(Set.class);

        assertEquals("index", index);
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), captor.capture());
        Set<Recipe> recipes = captor.getValue();
        assertEquals(2, recipes.size());
    }
}