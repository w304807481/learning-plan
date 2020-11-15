package com.github.opensharing.framework.springboot.upload;

import java.io.File;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * UploadController
 *
 * @author jwen
 * Date 2020-09-16
 */
@RequestMapping
@RestController
public class UploadController {

    @RequestMapping("/upload")
    public String upload(@RequestParam("filename") MultipartFile filename) throws IOException {
        String dest = this.getClass().getResource("/").getPath() + File.separator + Math.random() + ".upload";
        System.out.println("dest:" + dest);
        filename.transferTo(new File(dest));
        return "File is being uploaded to " + dest;
    }
}
