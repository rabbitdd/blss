package main.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.security.CustomAuthorityGranter;
import main.security.JAASLoginModule;
import main.security.XMLReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.jaas.AbstractJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.DefaultJaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;

import javax.security.auth.login.AppConfigurationEntry;
import javax.sql.rowset.spi.XmlReader;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JAASConfiguration {

  private final XMLReader xmlReader;

  @Bean
  public InMemoryConfiguration configuration(XMLReader xmlReader){
    Map<String, XMLReader> options = new HashMap<>();
    log.info("configuration");
    options.put("xmlReader", xmlReader);
    AppConfigurationEntry[] configurationEntries = new AppConfigurationEntry[]{
            new AppConfigurationEntry(JAASLoginModule.class.getCanonicalName(), AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)
    };
    Map<String, AppConfigurationEntry[]> map = new HashMap<>();
    map.put("SPRINGSECURITY", configurationEntries);
    return new InMemoryConfiguration(map);
  }

  @Bean
  public AbstractJaasAuthenticationProvider jaasAuthenticationProvider(javax.security.auth.login.Configuration configuration) {
    DefaultJaasAuthenticationProvider provider = new DefaultJaasAuthenticationProvider();
    log.info("provider");
    provider.setConfiguration(configuration);
    provider.setAuthorityGranters(new AuthorityGranter[]{new CustomAuthorityGranter(xmlReader)});
    return provider;
  }
}
