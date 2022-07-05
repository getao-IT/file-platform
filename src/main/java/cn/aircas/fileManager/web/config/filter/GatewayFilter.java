package cn.aircas.fileManager.web.config.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "gatewayFilter", urlPatterns = "/*")
@Order(2)
@Slf4j
public class GatewayFilter implements Filter {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${value.api.dispatch}")
    private String dispatchUrl;

    private static List<String> excludeUrl = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        excludeUrl = new ArrayList<>();
        excludeUrl.add("/geoai/v1/status");
        log.info("--------------------gatewayFilter初始化成功---------------------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            for (String url : excludeUrl) {
                if (servletRequest.getRequestURI().contains(url)) {
                    continue;
                } else {
                    HttpHeaders headers = new HttpHeaders();
                    HttpEntity<String> entity = new HttpEntity<>(null, headers);
                    UriComponents preFix = UriComponentsBuilder.fromHttpUrl(dispatchUrl).queryParam("prefix", "/file-platform").build().encode();
                    JSONObject body = restTemplate.exchange(preFix.toUri(), HttpMethod.POST, entity, JSONObject.class).getBody();
                    log.info("gateway执行结果：{} ", JSONObject.toJSONString(body));
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("ch代理接口调用异常：{} ", e.getMessage());
        }
    }

    @Override
    public void destroy() {
        log.info("--------------------gatewayFilter销毁---------------------");
    }
}
