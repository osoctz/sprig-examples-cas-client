package cn.metaq.cas.config;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @Author:
 * @Date:
 * @Description: CAS集成核心配置类
 */
@Configuration
@Slf4j
@ConditionalOnProperty(value = "cas.loginType", havingValue = "cas")
public class CasConfig {

  /**
   * 需要走cas拦截的地址（/* 所有地址都拦截）
   */
  @Value("${cas.urlPattern:/casLogin}")
  private String filterUrl;

  /**
   * 认证中心登录页面地址
   */
  @Value("${cas.server-url-prefix:https://localhost:8443/cas/login}")
  private String casServerUrl;

  /**
   *认证中心地址
   */
  @Value("${cas.authentication-url:https://localhost:8443/cas/}")
  private String authenticationUrl;

  /**
   * 应用地址，也就是自己的系统地址。
   */
  @Value("${cas.client-host-url:http://localhost:9090}")
  private String appServerUrl;

  @Bean
  public ServletListenerRegistrationBean servletListenerRegistrationBean() {
    log.info(" \n cas 单点登录配置 \n appServerUrl = " + appServerUrl + "\n casServerUrl = " + casServerUrl);
    log.info(" servletListenerRegistrationBean ");
    ServletListenerRegistrationBean listenerRegistrationBean = new ServletListenerRegistrationBean();
    listenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
    listenerRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return listenerRegistrationBean;
  }

  /**
   * 单点登录退出
   *
   * @return
   */
  @Bean
  public FilterRegistrationBean singleSignOutFilter() {
    log.info(" servletListenerRegistrationBean ");
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new SingleSignOutFilter());
    registrationBean.addUrlPatterns(filterUrl);
    registrationBean.addInitParameter("casServerUrlPrefix", casServerUrl);
    registrationBean.setName("CAS Single Sign Out Filter");
    registrationBean.setOrder(1);
    return registrationBean;
  }

  /**
   * 单点登录认证
   *
   * @return
   */
  @Bean
  public FilterRegistrationBean AuthenticationFilter() {
    log.info(" AuthenticationFilter ");
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new AuthenticationFilter());
    registrationBean.addUrlPatterns(filterUrl);
    registrationBean.setName("CAS Filter");
    registrationBean.addInitParameter("casServerLoginUrl", casServerUrl);
    registrationBean.addInitParameter("serverName", appServerUrl);
    registrationBean.setOrder(1);
    return registrationBean;
  }

  /**
   * 单点登录校验
   *
   * @return
   */
  @Bean
  public FilterRegistrationBean Cas30ProxyReceivingTicketValidationFilter() {
    log.info(" Cas30ProxyReceivingTicketValidationFilter ");
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
    registrationBean.addUrlPatterns(filterUrl);
    registrationBean.setName("CAS Validation Filter");
    registrationBean.addInitParameter("casServerUrlPrefix", authenticationUrl);
    registrationBean.addInitParameter("serverName", appServerUrl);
    registrationBean.setOrder(1);
    return registrationBean;
  }

  /**
   * 单点登录请求包装
   *
   * @return
   */
  @Bean
  public FilterRegistrationBean httpServletRequestWrapperFilter() {
    log.info(" httpServletRequestWrapperFilter ");
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new HttpServletRequestWrapperFilter());
    registrationBean.addUrlPatterns(filterUrl);
    registrationBean.setName("CAS HttpServletRequest Wrapper Filter");
    registrationBean.setOrder(1);
    return registrationBean;
  }

}


