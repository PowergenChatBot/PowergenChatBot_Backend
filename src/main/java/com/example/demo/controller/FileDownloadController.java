package com.example.demo.controller;

import com.example.demo.common.FileDownloadConfig;
import com.example.demo.dto.DownURL;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/download")
public class FileDownloadController {

    @ApiOperation(value = "링크로 파일 로컬에 다운로드하기", response = List.class)
    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody DownURL downURL){

        String url = downURL.getUrl();
        String dir="c:\\작업공유\\" + downURL.getFileName();

        FileDownloadConfig fileDownloadConfig = new FileDownloadConfig();
        String returnValue = fileDownloadConfig.downloadFile(url, dir);

        if(returnValue.contains("성공")){
            return new ResponseEntity<String>(returnValue, HttpStatus.OK);
        }else{
            return new ResponseEntity<String>(returnValue, HttpStatus.EXPECTATION_FAILED);
        }
    }


}
