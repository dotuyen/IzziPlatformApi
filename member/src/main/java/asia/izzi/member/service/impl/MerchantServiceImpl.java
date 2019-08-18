//package asia.izzi.member.service.impl;
//
//import asia.izzi.member.domain.model.Merchant;
//import asia.izzi.member.repository.MerchanRepository;
//import asia.izzi.member.repository.MerchantRepository;
//import asia.izzi.member.service.MerchantService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class MerchantServiceImpl implements MerchantService {
//    @Autowired
//    MerchanRepository merchantRepository;
//    @Override
//    public List<Merchant> getMerchants(String textSearch) {
//        return merchantRepository.findByNameContainingOrCodeContaining(textSearch,textSearch);
//    }
//}
