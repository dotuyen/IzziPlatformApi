package asia.izzi.member.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "https://safeone-9f4e9.firebaseio.com", url = "https://safeone-9f4e9.firebaseio.com")
public interface SampleFeignService {

    @RequestMapping(path = "", method = RequestMethod.GET)
    ResponseEntity<String> findPartyByCustomerId();
}
