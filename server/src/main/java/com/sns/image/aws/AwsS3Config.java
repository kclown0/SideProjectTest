//package com.sns.image.aws;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AwsS3Config {
//
//    @Value("${cloud.aws.credentials.access-key}") //IAM계정의 accessKey 값
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secret-Key}")  //IAM게정의 secretKey 값
//    private String secretKey;
//
//    @Value("${cloud.aws.region.static}")  //사용하는 region 명
//    private String region;
//
//
//    @Bean
//    public AmazonS3Client amazonS3Client() {
//        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey,secretKey);
//        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .build();
//    }
//}
