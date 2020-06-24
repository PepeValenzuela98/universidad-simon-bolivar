package com.jaevsoftware.USB;

import com.jaevsoftware.USB.model.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsbApplication implements CommandLineRunner {


    @Autowired
    private IUploadFileService uploadService;

    public static void main(String[] args) {
        SpringApplication.run(UsbApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        uploadService.createDirectory();
        
        
    }

}
