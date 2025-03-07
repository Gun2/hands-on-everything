package com.github.gun2.authapp.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AccessTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(AccessToken.class) != null
                && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        AccessToken accessTokenAnnotation = parameter.getParameterAnnotation(AccessToken.class);
        String headerName = accessTokenAnnotation.headerName();
        String tokenType = accessTokenAnnotation.tokenType();
        String authorizationHeader = webRequest.getHeader(headerName);
        if (authorizationHeader != null && authorizationHeader.startsWith(tokenType)) {
            return authorizationHeader.substring(tokenType.length() + 1);
        }
        return null;
    }
}

