package org.lpro.categoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lpro.categoryservice.boundary.CategoryResource;
import org.lpro.categoryservice.entity.Category;
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
        private TestRestTemplate restTemplate;
        
        @Before
        public void setupContext(){
             cr.deleteAll();
        }

        
        @Test
        public void getOneAPI(){
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            
            ResponseEntity<String> response = restTemplate.getForEntity("/categories/" + c1.getId(), String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Pain complet");
        }
        
        @Test
        public void getAllAPI(){
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
        public void notFoundAPI() throws Exception {
            ResponseEntity<String> response = restTemplate.getForEntity("/categories/42", String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            
        }
        
        @Test
        public void postAPI() throws Exception{
            Category c1 = new Category("Pain complet", "Description : Pain complet");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entite = new HttpEntity<>(this.toJsonString(c1), headers);
            ResponseEntity<?> response = restTemplate.postForEntity("/categories", entite, ResponseEntity.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            
            URI location = response.getHeaders().getLocation();
            response = restTemplate.getForEntity(location, String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
        
        private String toJsonString(Object o) throws Exception{
            ObjectMapper map = new ObjectMapper();
            return map.writeValueAsString(o);
        }
        
        @Test
        public void putAPI(){
            
        }
        
        
        @Test
        public void deleteAPI(){
            
        }
        
}
