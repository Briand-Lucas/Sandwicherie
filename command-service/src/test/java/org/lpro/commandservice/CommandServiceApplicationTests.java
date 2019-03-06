package org.lpro.commandservice;

import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lpro.commandservice.boundary.CommandResource;
import org.lpro.commandservice.entity.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommandServiceApplicationTests {

	 @Autowired
        private CommandResource cr;
         
        @Autowired
        private TestRestTemplate restTemplate;
        
        @Before
        public void setupContext(){
             cr.deleteAll();
        }
        
         @Test
        public void getOneCommandAPI(){
            Command c1 = new Command();
            c1.setId(UUID.randomUUID().toString());
            cr.save(c1);
            
            ResponseEntity<String> response = restTemplate.getForEntity("/categories/" + c1.getId(), String.class);
            
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).contains("Pain complet");
        }
        


}
