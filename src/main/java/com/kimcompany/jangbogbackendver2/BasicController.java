package com.kimcompany.jangbogbackendver2;

import com.kimcompany.jangbogbackendver2.Aws.S3Service;
import com.kimcompany.jangbogbackendver2.File.FileService;
import com.kimcompany.jangbogbackendver2.Util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.kimcompany.jangbogbackendver2.Util.UtilService.getFiles;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BasicController {

    private final FileService fileService;

    @RequestMapping(value = "/file/upload",method = RequestMethod.POST)
    public ResponseEntity<?> uploadImg(MultipartHttpServletRequest request){
        List<File> files = new ArrayList<>();
        JSONObject response = new JSONObject();
        try {
            files = getFiles(request.getFiles("upload"));
        }catch (Exception e){
            if(files.isEmpty()){
                response.put("message", "파일이 없습니다");
                return ResponseEntity.internalServerError().body(response);
            }
        }
        List<String> urls = fileService.uploadImg(files);
        response.put("message", urls);
        return ResponseEntity.ok().body(response);
    }

}
