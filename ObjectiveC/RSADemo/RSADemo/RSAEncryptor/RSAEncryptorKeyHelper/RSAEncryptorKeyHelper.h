//
//  HBRSAHandler.h
//  iOSRSAHandlerDemo
//
//  Created by wangfeng on 15/10/19.
//  Copyright (c) 2015年 HustBroventure. All rights reserved.
//


#import <Foundation/Foundation.h>
typedef enum {
    KeyTypePublic = 0,
    KeyTypePrivate
}KeyType;

// UTF8 Data - NSString
#define UTF8DataFromString(str) [str dataUsingEncoding:NSUTF8StringEncoding]
#define StringFromUTF8Data(data) [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]

// Base64 - NSData
#define Base64StringFromData(data) [data base64EncodedStringWithOptions:0]
#define DataFromBase64String(base64str) [[NSData alloc] initWithBase64EncodedString:base64str \
                                options:NSDataBase64DecodingIgnoreUnknownCharacters]


@interface RSAEncryptorKeyHelper : NSObject


/**
 导入密钥文件，这里使用的是 PEM 格式

 -----BEGIN RSA PRIVATE KEY-----
 MIICXgIBAAKBgQDl+61Tl6eG74xPw0f0Hx1CuHlQPbN7Tq1hrvLD/9Qsc9OmouVu
 PeVSIjMSvyxiCo/IEaZApziGlFR+h4BTYlr3MCPswRiwUlA3BTTkx63htyVCII1i
 qaByJraizPjqrwAm/3Wf2ejdlWIfLXyoSU2I/07z8faqQOj93Lf+pK1SCQIDAQAB
 AoGAELfG1bjEeM6ZCINNi7VnTx5R0z9o+KGzgNJS22SRa9nPnOFihC0DqoDJ7abv
 9wCFiymdSD3fogjOS5RWA7m2mvYhvdF76OAVUQSfT7EbO4BsP4/BMSnUzvF7hpHT
 SDvA10pdt+5IJnzKXR6cVu0q0ela3jfIRAFTA4eZ3mSKtsECQQD0lHhxZPB1RtcZ
 r0W5Din2khHzuBxPpRwE/jROZSCeC3SMFy8bFpDqPvNCS3hPzhPC3DI3Sxz3z2Oc
 T5mcEpxlAkEA8Li5/soxI9nQBHAcIgbb4bCi9LEdWXIo3r1fYCulJb9xJl0+BKyk
 h9cxdHa/QyFcggx3NzR7yh63IPNbckxK1QJBAMURlHKZyH5WlB7n8SAI35sDW1+r
 a3RpL0h+oxq/tSZjSIdQbu4+ool8K3mxYcG75IRP9E5FQZQTEMn0AWuWO1kCQQCd
 yojpEgJ6HJm1fwOB7nsNiqqWuM6jRiiyMPIm6RUdFEuIOVCg4cYL4Sxiyj//Mgwg
 lylCP53xVIb4C21jE81tAkEAyvywS4FpV2tZNybDjkAlfg7dVWa7HDnmDGh44DlZ
 JgGd0JfXHOL9WG9F+QAZUYJfmCk/N4qGljY1ogx8MX78iQ==
 -----END RSA PRIVATE KEY-----

 -----BEGIN PUBLIC KEY-----
 MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDl+61Tl6eG74xPw0f0Hx1CuHlQ
 PbN7Tq1hrvLD/9Qsc9OmouVuPeVSIjMSvyxiCo/IEaZApziGlFR+h4BTYlr3MCPs
 wRiwUlA3BTTkx63htyVCII1iqaByJraizPjqrwAm/3Wf2ejdlWIfLXyoSU2I/07z
 8faqQOj93Lf+pK1SCQIDAQAB
 -----END PUBLIC KEY-----


 @param type 密钥类型（公钥/私钥）
 @param path 密钥文件路径
 @return 是否成功
 */
- (BOOL)importKeyWithType:(KeyType)type andPath:(NSString*)path;


/**
 导入密钥字符串，Base64 格式
 
 内容为密钥文件删除了头尾信息后的 Base64 格式

 这些信息需要删除
 -----BEGIN RSA PRIVATE KEY-----
 -----END RSA PRIVATE KEY-----

 -----BEGIN PUBLIC KEY-----
 -----END PUBLIC KEY-----

 @param type 密钥类型（公钥/私钥）
 @param keyString 密钥 Base64 字符串
 @return 是否成功
 */
- (BOOL)importKeyWithType:(KeyType)type andkeyString:(NSString *)keyString;

// ============================== 签名及校验 ====================================


/**
 使用 SHA1 算法进行签名

 @param data 原始数据
 @return 签名后的数据转为 Base64 返回
 */
- (NSString *)signDataWithSHA1:(NSData *)data;


/**
 使用 MD5 算法进行签名

 @param data 原始数据
 @return 签名后的数据转为 Base64 返回
 */
- (NSString *)signDataWithMD5:(NSData *)data;


/**
 SHA1 签名校验

 @param originalData 原始数据
 @param signString 签名后得到的 Base64 字符串
 @return 校验结果
 */
- (BOOL)verifyData:(NSData *)originalData withSHA1Sign:(NSString *)signString;


/**
 MD5 签名校验

 @param originalData 原始数据
 @param signString 签名后得到的 Base64 字符串
 @return 校验结果
 */
- (BOOL)verifyData:(NSData *)originalData withMD5Sign:(NSString *)signString;


// ============================== 加密解密 =====================================
/**
 使用公钥为数据加密

 @param originData 原始数据
 @return 加密后的数据
 */
- (NSData *)encryptWithPublicKeyForData:(NSData *)originData;

/**
 使用公钥为字符串加密

 @param originStr 要加密的字符串
 @return 加密后的数据，做 Base64 编码后返回
 */
- (NSString *)encryptWithPublicKeyForString:(NSString *)originStr;

/**
 使用私钥为数据解密

 @param encryptedData 加密后的数据
 @return 解密后的数据
 */
- (NSData *)decryptWithPrivatecKeyForData:(NSData*)encryptedData;


/**
 使用私钥为数据解密，密文格式加密后的数据的 Base64 编码

 @param encryptedBase64String 密文格式加密后的数据的 Base64 编码
 @return 解密后得到的字符串
 */
- (NSString *)decryptWithPrivatecKeyForBase64String:(NSString *)encryptedBase64String;


@end
