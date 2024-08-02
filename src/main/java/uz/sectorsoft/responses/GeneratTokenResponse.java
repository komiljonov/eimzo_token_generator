package uz.sectorsoft.responses;

public class GeneratTokenResponse {
    private String pkcs7;
    private String signature;

    // Getters and setters
    public String getPkcs7() {
        return pkcs7;
    }

    public void setPkcs7(String pkcs7) {
        this.pkcs7 = pkcs7;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}