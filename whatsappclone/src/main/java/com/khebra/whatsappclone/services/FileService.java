package com.khebra.whatsappclone.services;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

        @Value("${application.file.uploads.media-output-path}")
        private String fileUploadPath;
        public String saveFile(
                @NonNull MultipartFile sourceFile,@NonNull String userId){
final String fileUploadSubPath="users"+ separator+userId;
return uploadFile(sourceFile,fileUploadSubPath);
        }

        private String uploadFile(@NonNull MultipartFile sourceFile,@NonNull String fileUploadSubPath) {
        final String finalUploadPath=fileUploadPath+separator+fileUploadSubPath;
        File targetFolder=new File(finalUploadPath);
        if(!targetFolder.exists()){
                boolean folderCreated=targetFolder.mkdirs();
                if (!folderCreated){
                        log.warn("failed to create the target folder,{}",targetFolder);
                return null;
                }
        }
        final String fileExtension=getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath=finalUploadPath+separator+currentTimeMillis()+fileExtension;
                Path targetPath= Paths.get(targetFilePath);
                try{
                        Files.write(targetPath,sourceFile.getBytes());
                        log.info("file saved {}",targetPath);
                        return targetFilePath;
                }catch (IOException e){
                        log.error("File was not saved {}",e.getMessage());
                }
                return null;
        }

        private String getFileExtension(String originalFilename) {
        if(originalFilename==null || originalFilename.isEmpty()){
                return "";
        }
        int lastDotIndex=originalFilename.lastIndexOf('.');
        if (lastDotIndex==-1) return "";
        return originalFilename.substring(lastDotIndex+1).toLowerCase();
        }
}
