#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
* `ContextLoaderListener ` 
  * 서블릿 컨텍스트 초기화 시점에 데이터베이스 스크립트를 실행하는 역할을 하는 `ServletContextListener` 구현체이다.
  * 웹앱 시작 시점에 jwp.sql 안에 있는 테이블 생성, 초기 데이터 삽입 같은 sql명령어들이 데이터베이스에 자동으로 적용된다.
* `DispatcherServlet`
  * 프론트 컨트롤러 패턴을 구현한 서블릿
  * 모든 요청을 받아서 적절한 컨트롤러에 위임하고, 그 결과를 렌더링하는 중앙 관제탑 역할의 서블릿이다.
  * 서버 시작시 uri-controller 매핑
  * `service`: 요청 uri 확인 -> 매핑된 컨틀롤러 찾기 -> 컨트롤러 실행 -> 모델and뷰 반환 -> view 렌더링 -> 응답  


    1. 서블릿 컨테이너는 웹 애플리케이션의 상태를 관리하는 `ServletContext`를 생성한다.
    2. `ServletContext`가 초기화되면 컨텍스트 초기화 이벤트가 발생된다.'
    3. 등록된 `ServletContextListener`의 콜백 메소드가 호출된다. `contextInitialized()`
    4. `jwp.sql` 파일에서 데이터베이스 테이블을 초기화한다.
    5. 서블릿 컨테이너는 클라이언트로부터의 최초 요청시 `DispatcherServlet` 인스턴스를 생성한다. `@WebServlet`의 `loadOnStartup` 속성으로 설정 가능.
    6. `DispatcherServlet` 인스턴스의 `init()` 메소드를 호출해 초기화 작업을 진행한다.
    7. `init()` 메소드 안에서 `RequestMapping` 객체를 생성한다.
    8. `RequestMapping` 인스턴스의 `initMapping()` 메소드를 호출한다. `initMapping()`메소드에서는 요청 url과 Controller 인스턴스를 매핑시킨다.



#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
* 요청 처리할 서블릿에 접근하기 전에 먼저 ResourceFilter와 CharacterEncodingFilter의 doFilter() 메소드가 실행된다. ResourceFilter의 경우 해당 요청이 정적 자원 요청이 아니기 때문에 서블릿으로 요청을 위임한다.
* 요청처리는 '/' 으로 매핑되어 있는 DispatcherServlet이므로 이 서블릿의 servcie()가 실행된다.
  ```
  @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(req.getRequestURI());
        ModelAndView mav;
        try {
            mav = controller.execute(req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }
  ```
* service() 메소드는 요청받은 url을 분석해 해당 Controller 객체를 RequestMapping에서 가져온다. 요청 URL은 '/'이며, 이와 연결되어 있는 HomeController가 반환된다.
* service() 메소드는 HomeController의 execute() 메소드에게 작업을 위임한다. 요청에 대한 실질적인 작업은 HomeController의 execute() 메소드가 실행한다. 반환값은 ModelAndView이다.
  ```
    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("home.jsp").addObject("questions", questionDao.findAll());
    }
    ```
* service() 메소드는 반환 받은 ModelAndView의 모델 데이터를 뷰의 render() 메소드에 전달한다. 이 요청에서 View는 jspView이다. 
JspView는 render()메소드로 전달된 모델 데이터를 home.jsp에 전달해 HTML을 생성하고 응답하며 작업을 끝낸다.
  ```
  @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        Set<String> keys = model.keySet();
        for (String key : keys) {
            request.setAttribute(key, model.get(key));
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }
  ```

#### 7. next.web.qna package의 ShowController는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* 
