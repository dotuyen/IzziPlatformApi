package asia.izzi.member.service.impl;

import asia.izzi.member.service.ExcelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ExcelFileServiceImpl implements ExcelFileService {

    @Autowired
    private Environment env;

    @Override
    /* todo: viec ghi file nen viet rieng ra 1 Service khác - ExcelFileService, khong de chung xu ly trong customerService,
        CustomerService chi cung cap danh sách data de   ExcelFileService ghi ra file
    */
    public String writeFile(MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        String uploadPath = env.getProperty("app.path.upload") + File.separator + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File serverFile = new File(uploadPath + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(file.getBytes());
        stream.close();
        return uploadPath + File.separator + name;
    }

}
