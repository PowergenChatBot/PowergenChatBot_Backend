package com.example.demo.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;


public class FileDownloadConfig {

    public String downloadFile(String url, String dir){
        try {
            downloadToFile(new URL(url), new File(dir));
            return "파일 저장 성공";
        } catch (IOException e) {
            return "저장 도중 에러 발생";
        }
    }

    /** 정해진 file로 url의 내용을 저장한다. (저장되는 파일명은 url과 무관함)  **/
    private void downloadToFile(URL url, File savedFile) throws IOException {
        if (url==null) throw new IllegalArgumentException("url is null.");
        if (savedFile==null) throw new IllegalArgumentException("savedFile is null.");
        if (savedFile.isDirectory()) throw new IllegalArgumentException("savedFile is a directory.");
        downloadTo(url, savedFile, false);
    }

    /** 정해진 디렉토리로 url의 내용을 저장한다. (저장되는 파일명이 url에 따라서 달라짐) **/
    private void downloadToDir(URL url, File dir) throws IOException {
        if (url==null) throw new IllegalArgumentException("url is null.");
        if (dir==null) throw new IllegalArgumentException("directory is null.");
        if (!dir.exists()) throw new IllegalArgumentException("directory is not existed.");
        if (!dir.isDirectory()) throw new IllegalArgumentException("directory is not a directory.");
        downloadTo(url, dir, true);
    }

    private void downloadTo(URL url, File targetFile, boolean isDirectory) throws IOException{

        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            File saveFilePath=null;

            if (isDirectory) {
                if (disposition != null) {
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    String fileURL=url.toString();
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
                    int questionIdx=fileName.indexOf("?");
                    if (questionIdx>=0) {
                        fileName=fileName.substring(0, questionIdx);
                    }
                    fileName=URLDecoder.decode(fileName);
                }
                saveFilePath = new File(targetFile, fileName);
            }
            else {
                saveFilePath=targetFile;
            }

            InputStream inputStream = httpConn.getInputStream();

            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
            System.out.println("File downloaded to " + saveFilePath);
        } else {
            System.err.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
    }

}
