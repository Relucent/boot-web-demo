package yyl.demo.common.resolver;

import java.util.Iterator;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.github.relucent.base.util.collect.Mapx;

import yyl.demo.common.model.MapReference;



/**
 * _Mapx适配器
 */
public class MapReferenceArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return MapReference.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        Mapx map = new Mapx();
        for (Iterator<String> names = webRequest.getParameterNames(); names.hasNext();) {
            String name = names.next();
            map.put(name, webRequest.getParameter(name));
        }
        return new MapReference(map);
    }
}
