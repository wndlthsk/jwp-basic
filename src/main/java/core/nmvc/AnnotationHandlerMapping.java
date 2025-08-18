package core.nmvc;

import com.google.common.collect.Sets;
import core.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.Map;

import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Maps;

import core.annotation.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    /**
     * 애플리케이션 시작 시 초기화 메소드
     * 1. @Controller 클래스들을 스캔
     * 2. 각 클래스의 @RequestMapping 메소드를 찾아서
     *    handlerExecutions(Map)에 등록
     */
    public void initialize() {
        // @Controller 붙은 클래스 스캔해서 인스턴스 생성
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // @RequestMapping 붙은 메소드들 찾기
        Set<Method> methods = getRequestMappingMethods(controllers.keySet());

        // 찾은 메소드들을 (url, http method) → HandlerExecution 형태로 등록
        for (Method method : methods) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            logger.debug("register handlerExecution : url is {}, method is {}", rm.value(), method);

            // HandlerKey = 요청 URL + HTTP Method
            // HandlerExecution = 실행할 메소드 + 인스턴스
            handlerExecutions.put(
                createHandlerKey(rm),
                new HandlerExecution(controllers.get(method.getDeclaringClass()), method)
            );
        }
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

    @SuppressWarnings("unchecked")
    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> requestMappingMethods = Sets.newHashSet();
        for (Class<?> clazz : controllers) {
            requestMappingMethods.addAll(
                ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class))
            );
        }
        return requestMappingMethods;
    }

    /**
     * 실제 요청이 들어왔을 때 실행할 핸들러(HandlerExecution) 찾는 메소드
     * 1. 요청 URI + HTTP 메소드 읽음
     * 2. handlerExecutions(Map)에서 대응되는 HandlerExecution 리턴
     */
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}
