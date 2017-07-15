# 当异构系统遇上 RSA


#### 背景
　　网络通信在安全性比较敏感的环境，有可能会用到 [RSA](https://en.wikipedia.org/wiki/RSA_(cryptosystem)) 算法，这里有 [RSA 的中文说明 ](https://zh.wikipedia.org/wiki/RSA%E5%8A%A0%E5%AF%86%E6%BC%94%E7%AE%97%E6%B3%95)。
  
　　但是在异构系统间通信，使用 RSA 加密的时候，由于各系统对于 RSA 的实现有些细节差异，比如：对证书和密钥格式的支持不一致，而且证书格式与扩展名关系又比较乱，所以会导致一些问题。

#### 整体思路
　　证书格式方面，迁就 JavaScript，在 Objective-C 和 Java 端增加对相应格式的处理。
　　
#### 一、JavaScript
**主要职责：** 作为 Client 在 Web 页面使用，使用公钥对数据进行加密。

　　我们使用了 JavaScript 的 RSA 库 [JSEncrypt](http://travistidwell.com/jsencrypt/)。由于 JS 对二进制数据的支持不太方便，所以 JSEncrypt 对于公钥和私钥的支持，都使用了 PEM 格式。

#### 二、Objective-C
**主要职责：** 作为 Client 在 iOS 或 macOS 设备上使用，使用公钥对数据进行加密。

　　其实在 Apple 的 Core Foundation 中，对 RSA 有比较标准的支持，但是不知道什么原因，不支持 PEM 格式密钥的处理。由于 JS 端处理能力有限，所以最后采用的是在 Objective-C 端集成 [OpenSSL](https://www.openssl.org/) 的方式来处理。

**参考内容：**
- 对于 Core Foundation 标准库调用的方式参考了[《iOS中使用RSA加密》](http://www.jianshu.com/p/74a796ec5038)
- 对于集成 OpenSSL 的方案参考了[《iOS RSA加解密签名和验证》](https://github.com/HustBroventure/iOSRSAHandler)，他的代码在 [iOSRSAHandler](https://github.com/HustBroventure/iOSRSAHandler)。这份代码是对于 OpenSSL 中 C 函数的封装，核心算法部分没什么问题，但是对外提供的接口以及中文处理方面，细节有些问题，做了些调整。

#### 三、Java
**主要职责：**
- Android：作为 Android Client 使用公钥对数据进行加密。 
- JavaEE Server：作为 Server 使用私钥对数据进行解密。  

**参考内容：**
　　Java 中对于 RSA 也有比较良好的支持，主体代码参考了[《RSA Encryption Example》](https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/)。但是这份代码也是不支持读取 PEM 格式的私钥，所以参考了 [Stack Overflow](https://stackoverflow.com/questions/11787571/how-to-read-pem-file-to-get-private-and-public-key)。

