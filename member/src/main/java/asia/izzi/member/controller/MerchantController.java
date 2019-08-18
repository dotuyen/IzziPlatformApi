package asia.izzi.member.controller;


import asia.izzi.member.domain.model.Merchant;
import asia.izzi.member.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant")
public class MerchantController {

//    @Autowired
//    UserService userService;

    @Autowired
    MerchantRepository merchantRepository;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAllMerchant() {
        return new ResponseEntity<>(merchantRepository.findAll(), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createRandomMerchant() {

        Merchant merchant = Merchant.builder().name("test").code("test").build();
        return new ResponseEntity<>(merchantRepository.save(merchant), HttpStatus.OK);
    }

//    @GetMapping("/search")
//    public List<User> getMerchants(@RequestParam("name")  String name, @RequestParam("phone")  String phone,
//                                   @RequestParam("email")  String email ){
//        return userService.getUser(phone);
//    }
}