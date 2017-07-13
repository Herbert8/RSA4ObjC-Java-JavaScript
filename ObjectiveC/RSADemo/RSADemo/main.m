//
//  main.m
//  TestRSA2
//
//  Created by 巴宏斌 on 2017/7/11.
//  Copyright © 2017年 巴宏斌. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RSAEncryptorKeyHelper.h"

int main(int argc, const char * argv[]) {
    @autoreleasepool {

        NSString *s = [[NSBundle mainBundle] pathForResource:@"rsa_1024_pub" ofType:@"pem"];
        NSLog(@"%@", s);


//        NSString *publicKeyFilePath = [[NSBundle mainBundle] pathForResource:@"rsa_public_key.pem" ofType:nil];
//        NSString *privateKeyFilePath = [[NSBundle mainBundle] pathForResource:@"rsa_private_key.pem" ofType:nil];

        RSAEncryptorKeyHelper *handler = [RSAEncryptorKeyHelper new];
        //两种方式导入
        // [handler importKeyWithType:KeyTypePublic andPath:publicKeyFilePath];
        //[handler importKeyWithType:KeyTypePrivate andPath:privateKeyFilePath];

//        [handler importKeyWithType:KeyTypePrivate andkeyString:private_key_string];
//        [handler importKeyWithType:KeyTypePublic andkeyString:public_key_string];

        [handler importKeyWithType:KeyTypePublic andPath:@"/Volumes/Doc/MyCode/RSA4ObjC-Java-JavaScript/Certification/rsa_1024_pub.pem"];
        [handler importKeyWithType:KeyTypePrivate andPath:@"/Volumes/Doc/MyCode/RSA4ObjC-Java-JavaScript/Certification/rsa_1024_priv.pem"];


        NSString* sig = [handler signDataWithSHA1:UTF8DataFromString(@"需要签名的字符串sha1")];
        NSLog(@"签名后的字符串：%@", sig);
        BOOL isMatch = [handler verifyData:UTF8DataFromString(@"需要签名的字符串sha1")
                              withSHA1Sign:sig];
        NSLog(@"SHA1 验证签名结果：%@", isMatch ? @"成功" : @"失败");


        NSString* sigMd5 = [handler signDataWithMD5:UTF8DataFromString(@"需要签名字符串md5")];
        NSLog(@"签名后的字符串：%@", sigMd5);
        BOOL isMatchMd5 = [handler verifyData:UTF8DataFromString(@"需要签名字符串md5")
                                  withMD5Sign:sigMd5];
        NSLog(@"MD5 验证签名结果：%@", isMatchMd5 ? @"成功" : @"失败");

        NSLog(@"============================加解密测试======================");
        NSString *oriStr = @"abc中文中文xyz!!!222";
        NSLog(@"原始字符串：%@", oriStr);
        NSString *encStr = [handler encryptWithPublicKeyForString:oriStr];
        NSLog(@"加密后：%@", encStr);
        NSString* decStr = [handler decryptWithPrivatecKeyForBase64String:encStr];
        NSLog(@"解密后：%@", decStr);

    }
    return 0;
}
