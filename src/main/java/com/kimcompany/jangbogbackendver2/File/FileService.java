package com.kimcompany.jangbogbackendver2.File;

import com.kimcompany.jangbogbackendver2.Aws.S3Service;
import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final S3Service s3Service;

    public List<String> uploadImg(List<File>files){
        Map<String, Boolean> result = uploadCore(files);
        List<String> urls = new ArrayList<>();
        for(Map.Entry<String, Boolean> b:result.entrySet()){
            if(b.getValue()){
                urls.add(PropertiesText.awsUrl+b.getKey());
            }
        }
        return urls;
    }

    public Map<String,Boolean> uploadCore(List<File>files){
        Map<String, Boolean> result = new HashMap<>();
        for(File f:files){
            try {
                s3Service.uploadImage(f, "supermans3/images");
                f.delete();
                result.put(f.getName(), true);
            }catch (Exception e){
                log.info("사진 업로드 실패:{} \n 사유:{}", f.getName(), e.getMessage());
                result.put(f.getName(), false);
            }
        }
        return result;
    }
}
