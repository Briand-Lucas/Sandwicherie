package org.lpro.categoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lpro.categoryservice.boundary.CategoryResource;
import org.lpro.categoryservice.boundary.SandwichResource;
import org.lpro.categoryservice.entity.Category;
import org.lpro.categoryservice.entity.Sandwich;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryServiceApplicationTests {

	  @Autowired
        private CategoryResource cr;
          
         @Autowired 
        private SandwichResource sr;
        
        @Autowired
        private TestRestTemplate restTemplate;
        
        @Before
        public void setupContext(){
             cr.deleteAll();
             sr.deleteAll();
        }

        
        @Test
        public void getOneCategoryAPI(){
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            
            ResponseEntity<String> response = restTemplate.getForEntity("/categories/" + c1.getId(), String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Pain complet");
        }
        
        @Test
        public void getAllCategoriesAPI(){
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            Category c2 = new Category("Pain perdu", "Description : Pain perdu");
            c2.setId(UUID.randomUUID().toString());
            cr.save(c2);
            
            ResponseEntity<String> response = restTemplate.getForEntity("/categories", String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Pain complet");
            assertThat(response.getBody()).contains("Pain perdu");
        }
        
        @Test
        public void notFoundCategoryAPI() throws Exception {
            ResponseEntity<String> response = restTemplate.getForEntity("/categories/42", String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            
        }
        
        @Test
        public void postCategoryAPI() throws Exception{
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entite = new HttpEntity<>(this.toJsonString(c1), headers);
            ResponseEntity<Category> response = restTemplate.postForEntity("/categories", entite, Category.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            
            URI location = response.getHeaders().getLocation();
            response = restTemplate.getForEntity(location, Category.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
        
        private String toJsonString(Object o) throws Exception{
            ObjectMapper map = new ObjectMapper();
            return map.writeValueAsString(o);
        }
        
        @Test
        public void putCategoryAPI() throws Exception{
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);

            Category c2 = new Category("Pain perdu", "Description : Pain perdu");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entite = new HttpEntity<>(this.toJsonString(c2), headers);
            restTemplate.put("/categories/"+ c1.getId(), entite);
            ResponseEntity<String> response = restTemplate.getForEntity("/categories/" + c1.getId(), String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Pain perdu");
            
        }
        
        
        @Test
        public void deleteCategoryAPI(){
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            
            
            
            restTemplate.delete("/categories/" + c1.getId());
            
            
            ResponseEntity<String> response = restTemplate.getForEntity("/categories", String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
             assertThat(!response.getBody().contains("Pain Complet"));
        
        
        }
        
        @Test
        public void getSandwichsByCategoryAPI(){
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
             
            Sandwich s1 = new Sandwich("Sandwich complet", 4);
            s1.setCategory(c1);
            s1.setId(UUID.randomUUID().toString());
            sr.save(s1);
            ResponseEntity<String> response = restTemplate.getForEntity("/categories/" + c1.getId() + "/sandwichs", String.class);
            
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Sandwich complet");      
        }
        
        @Test
        public void CreateSandwichByCategoryIdAPI() throws Exception{
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
             
            Sandwich s1 = new Sandwich("Sandwich complet", 4);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entite = new HttpEntity<>(this.toJsonString(s1), headers);
            ResponseEntity<Sandwich> response = restTemplate.postForEntity("/categories/" + c1.getId() + "/sandwichs", entite, Sandwich.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            
      
            URI location = response.getHeaders().getLocation();
            System.out.println(location.toString());
            response = restTemplate.getForEntity(location, Sandwich.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
            
        }
        
        
        @Test
        public void getOneSandwichAPI(){
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            Sandwich s1 = new Sandwich("Sandwich complet", 4);
            s1.setId(UUID.randomUUID().toString());
            s1.setCategory(c1);
            sr.save(s1);
            
            ResponseEntity<String> response = restTemplate.getForEntity("/sandwichs/" + s1.getId(), String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Sandwich complet");
        }
        
        
        
        @Test
        public void getAllSandwichsAPI(){
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            
            Sandwich s1 = new Sandwich("Sandwich complet", 4);
            s1.setId(UUID.randomUUID().toString());
            s1.setCategory(c1);
            sr.save(s1);
            
            Sandwich s2 = new Sandwich("Sandwich complet 2", 4);
            s2.setId(UUID.randomUUID().toString());
            s2.setCategory(c1);
            sr.save(s2);
            
            
            ResponseEntity<String> response = restTemplate.getForEntity("/sandwichs", String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Sandwich complet");
            assertThat(response.getBody()).contains("Sandwich complet 2");
        }
        
       
        @Test
        public void updateSandwichAPI() throws Exception{
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            
            Sandwich s1 = new Sandwich("Sandwich complet", 4);
            s1.setId(UUID.randomUUID().toString());
            s1.setCategory(c1);
            sr.save(s1);
            
            Sandwich s2 = new Sandwich("Sandwich complet 2", 7);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entite = new HttpEntity<>(this.toJsonString(s2), headers);
            restTemplate.put("/sandwichs/"+ s1.getId(), entite);
            ResponseEntity<String> response = restTemplate.getForEntity("/sandwichs/" + s1.getId(), String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Sandwich complet 2");
   
        }
       
        
        
       
        @Test
        public void deleteSandwichAPI(){
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            Sandwich s1 = new Sandwich("Sandwich complet", 4);
            s1.setId(UUID.randomUUID().toString());
               s1.setCategory(c1);
            sr.save(s1);
                   
            restTemplate.delete("/sandwichs/" + s1.getId());
            
            
            ResponseEntity<String> response = restTemplate.getForEntity("/sandwichs", String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(!response.getBody().contains("Sandwich Complet"));
        
        }
        
        
}
