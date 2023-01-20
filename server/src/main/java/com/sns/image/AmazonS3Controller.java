//package com.sns.image;
//
//import com.sns.image.aws.AwsS3Service;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/s3")
//public class AmazonS3Controller {
//
//    private final AwsS3Service awsS3Service;
//
//    /**
//     * Amazon S3에 파일 업로드
//     * @return 성공 시 200 Success 와 함께 업로드 된 파일의 파일명 리스트 반환
//     */
//
//    @PostMapping("/upload")
//    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
//        return awsS3Service.upload(multipartFile,"static");
//    }
//}
