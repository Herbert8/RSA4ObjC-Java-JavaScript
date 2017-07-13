// Create the encryption object and set the key.
var crypt = new JSEncrypt();

const MY_PUBLIC_KEY = '-----BEGIN PUBLIC KEY-----' +
                      'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDl+61Tl6eG74xPw0f0Hx1CuHlQ' +
                      'PbN7Tq1hrvLD/9Qsc9OmouVuPeVSIjMSvyxiCo/IEaZApziGlFR+h4BTYlr3MCPs' +
                      'wRiwUlA3BTTkx63htyVCII1iqaByJraizPjqrwAm/3Wf2ejdlWIfLXyoSU2I/07z' +
                      '8faqQOj93Lf+pK1SCQIDAQAB' +
                      '-----END PUBLIC KEY-----';

const MY_PRIVATE_KEY = '-----BEGIN RSA PRIVATE KEY-----' +
                       'MIICXgIBAAKBgQDl+61Tl6eG74xPw0f0Hx1CuHlQPbN7Tq1hrvLD/9Qsc9OmouVu' +
                       'PeVSIjMSvyxiCo/IEaZApziGlFR+h4BTYlr3MCPswRiwUlA3BTTkx63htyVCII1i' +
                       'qaByJraizPjqrwAm/3Wf2ejdlWIfLXyoSU2I/07z8faqQOj93Lf+pK1SCQIDAQAB' +
                       'AoGAELfG1bjEeM6ZCINNi7VnTx5R0z9o+KGzgNJS22SRa9nPnOFihC0DqoDJ7abv' +
                       '9wCFiymdSD3fogjOS5RWA7m2mvYhvdF76OAVUQSfT7EbO4BsP4/BMSnUzvF7hpHT' +
                       'SDvA10pdt+5IJnzKXR6cVu0q0ela3jfIRAFTA4eZ3mSKtsECQQD0lHhxZPB1RtcZ' +
                       'r0W5Din2khHzuBxPpRwE/jROZSCeC3SMFy8bFpDqPvNCS3hPzhPC3DI3Sxz3z2Oc' +
                       'T5mcEpxlAkEA8Li5/soxI9nQBHAcIgbb4bCi9LEdWXIo3r1fYCulJb9xJl0+BKyk' +
                       'h9cxdHa/QyFcggx3NzR7yh63IPNbckxK1QJBAMURlHKZyH5WlB7n8SAI35sDW1+r' +
                       'a3RpL0h+oxq/tSZjSIdQbu4+ool8K3mxYcG75IRP9E5FQZQTEMn0AWuWO1kCQQCd' +
                       'yojpEgJ6HJm1fwOB7nsNiqqWuM6jRiiyMPIm6RUdFEuIOVCg4cYL4Sxiyj//Mgwg' +
                       'lylCP53xVIb4C21jE81tAkEAyvywS4FpV2tZNybDjkAlfg7dVWa7HDnmDGh44DlZ' +
                       'JgGd0JfXHOL9WG9F+QAZUYJfmCk/N4qGljY1ogx8MX78iQ==' +
                       '-----END RSA PRIVATE KEY-----';


// crypt.setKey(MY_KEY); //You can use also setPrivateKey and setPublicKey, they are both alias to setK
crypt.setPublicKey(MY_PUBLIC_KEY);

//Eventhough the methods are called setPublicKey and setPrivateKey, remember
//that they are only alias to setKey, so you can pass them both a private or
//a public openssl key, just remember that setting a public key allows you to only encrypt.

var text = 'test中文测试xyz!!!';
// Encrypt the data with the public key.
var enc = crypt.encrypt(text);

console.log('enc =', enc);
// Now decrypt the crypted text with the private key.
crypt.setPrivateKey(MY_PRIVATE_KEY);
var dec = crypt.decrypt(enc);


console.log('dec =', dec);

// Now a simple check to see if the round-trip worked.
if (dec === text){
    alert('It works!!!');
} else {
    alert('Something went wrong....');
}
    