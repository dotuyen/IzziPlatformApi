package asia.izzi.member.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelFileService {
    String writeFile(MultipartFile file) throws Exception;
}
