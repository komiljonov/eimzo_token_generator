package uz.sectorsoft;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class CertificateKeyInfo {
  protected String alias;

  protected String password;

  protected X509Certificate[] chain;

  protected PrivateKey privateKey;

  public X509Certificate getCertificate() {
    return (this.chain.length > 0) ? this.chain[0] : null;
  }

  public String getAlias() {
    return this.alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public X509Certificate[] getCertificateChain() {
    return this.chain;
  }

  public void setCertificateChain(X509Certificate[] chain) {
    this.chain = chain;
  }

  public PrivateKey getPrivateKey() {
    return this.privateKey;
  }

  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }
}