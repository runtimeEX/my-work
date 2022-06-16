package com.kafka.conf.message;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "locale")
public class LocaleProperties {

  private Boolean enabled = true;

  private String cookieName = "NG_TRANSLATE_LANG_KEY";

  private String paramName = "language";

  private String[] basenames = null;

  private String defaultEncoding = "UTF-8";

  private Integer cacheSeconds = 3600;

  public Boolean getEnabled() {
    return enabled;
  }

  public LocaleProperties setEnabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  public String getCookieName() {
    return cookieName;
  }

  public LocaleProperties setCookieName(String cookieName) {
    this.cookieName = cookieName;
    return this;
  }

  public String getParamName() {
    return paramName;
  }

  public LocaleProperties setParamName(String paramName) {
    this.paramName = paramName;
    return this;
  }

  public String[] getBasenames() {
    return basenames;
  }

  public LocaleProperties setBasenames(String[] basenames) {
    this.basenames = basenames;
    return this;
  }

  public String getDefaultEncoding() {
    return defaultEncoding;
  }

  public LocaleProperties setDefaultEncoding(String defaultEncoding) {
    this.defaultEncoding = defaultEncoding;
    return this;
  }

  public Integer getCacheSeconds() {
    return cacheSeconds;
  }

  public LocaleProperties setCacheSeconds(Integer cacheSeconds) {
    this.cacheSeconds = cacheSeconds;
    return this;
  }
}