package uz.sectorsoft;

import org.springframework.http.MediaType;

import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.security.Provider;
import java.security.Security;

import uz.sectorsoft.responses.GeneratTokenResponse;
import uz.sectorsoft.responses.GenerateTokenData;
import uz.yt.pkix.jcajce.provider.YTProvider;

@RestController
public class TokenController {
    Provider PROVIDER;

    public TokenController() {
        this.PROVIDER = Security.getProvider("BC");
        if (this.PROVIDER == null) {
            BouncyCastleProvider bcp = new BouncyCastleProvider();
            YTProvider.configure((ConfigurableProvider) bcp);
            Security.addProvider((Provider) bcp);
            PROVIDER = (Provider) bcp;

        }
    }

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public GeneratTokenResponse handleGenerate(@RequestBody GenerateTokenData tokenData) {
        System.out.println(tokenData.getPfxFilePath());
        System.out.println(tokenData.getPassword());
        System.out.println(tokenData.getAlias());
        System.out.println(tokenData.getData());
        System.out.println(tokenData.getAttached());

        if (tokenData.getPfxFilePath() != null) {
            System.out.println("Generating");
            try {
                GeneratTokenResponse response = PKCS12.signDocument(this.PROVIDER, tokenData.getPfxFilePath(),
                        tokenData.getPassword(), tokenData.getAlias(),
                        tokenData.getData().getBytes(), tokenData.getAttached());
                return response;
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Error");
                return null;
            }
        }
        return null;
    }
}