package uz.sectorsoft;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.util.encoders.Hex;

import uz.sectorsoft.responses.GeneratTokenResponse;
import uz.yt.cams.pki.DocumentSigner;
import uz.yt.cams.pki.DocumentVerifier;
import uz.yt.cams.pki.dto.Pkcs7Info;
import uz.yt.cams.pki.dto.Pkcs7SignerInfo;

public class PKCS12 {

    public static GeneratTokenResponse signDocument(Provider PROVIDER, String pfxFilePath, String password,
            String alias, byte[] data, boolean attached)
            throws Exception {
        // Provider PROVIDER;

        // PROVIDER = Security.getProvider("BC");
        // if (PROVIDER == null) {
        // BouncyCastleProvider bcp = new BouncyCastleProvider();

        // YTProvider.configure((ConfigurableProvider) bcp);
        // Security.addProvider((Provider) bcp);
        // PROVIDER = (Provider) bcp;
        // }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new FileInputStream(pfxFilePath);
        keyStore.load(fis, password.toCharArray());
        fis.close();

        CertificateKeyInfo cki = fetchKeyInfo(keyStore, alias, password, PROVIDER);
        X509Certificate signerCertificate = cki.getCertificate();

        DocumentSigner signer = new DocumentSigner(PROVIDER, cki.getCertificateChain(), cki.getPrivateKey());
        byte[] pkcs7 = signer.getPkcs7(data, attached);

        DocumentVerifier verifier = new DocumentVerifier(PROVIDER, null);
        Pkcs7Info info;
        if (attached) {
            info = verifier.verifyPkcs7Attached(pkcs7);
        } else {
            info = verifier.verifyPkcs7Detached(pkcs7, data);
        }

        byte[] signature = null;
        for (Pkcs7SignerInfo signerInfo : info) {
            if (signerInfo.getSignerId().getSerialNumber().equals(signerCertificate.getSerialNumber())) {
                signature = signerInfo.getSignature();
                break;
            }
        }

        String pkcs7Base64 = Base64.getEncoder().encodeToString(pkcs7);
        String signatureHex = Hex.toHexString(signature);

        // return pkcs7Base64 + "\n\n" + signatureHex;
        GeneratTokenResponse response = new GeneratTokenResponse();

        response.setPkcs7(pkcs7Base64);
        response.setSignature(signatureHex);

        System.out.println(pkcs7Base64);

        return response;
    }

    private static CertificateKeyInfo fetchKeyInfo(KeyStore keyStore, String alias, String password, Provider PROVIDER)
            throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, IOException,
            CertificateEncodingException, CertificateException {
        CertificateKeyInfo keyInfo = new CertificateKeyInfo();
        keyInfo.setPrivateKey((PrivateKey) keyStore.getKey(alias, password.toCharArray()));

        Certificate[] chain = keyStore.getCertificateChain(alias);
        X509Certificate[] certs = new X509Certificate[chain.length];

        int i = 0;
        for (Certificate c : chain) {
            X509CertificateHolder holder = new X509CertificateHolder(c.getEncoded());
            certs[i] = (new JcaX509CertificateConverter()).setProvider(password).setProvider(PROVIDER)
                    .getCertificate(holder);
            i++;
        }

        keyInfo.setCertificateChain(certs);
        keyInfo.setAlias(alias);
        keyInfo.setPassword(password);

        return keyInfo;
    }
}