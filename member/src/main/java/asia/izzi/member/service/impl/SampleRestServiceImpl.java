package asia.izzi.member.service.impl;

import asia.izzi.member.service.SampleRestService;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class SampleRestServiceImpl implements SampleRestService {
    static final String URL_EMPLOYEES = "https://safeone-9f4e9.firebaseio.com";
    // HttpHeaders

    public void testCallApiOtherService() {
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON}));

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("my_other_key", "my_other_value");


        HttpEntity<String> entity = new HttpEntity<String>(headers);


        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<String> response = restTemplate.exchange(URL_EMPLOYEES, //
                HttpMethod.GET, entity, String.class);

        String result = response.getBody();
        System.out.println(result);
    }


}
