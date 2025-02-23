//package com.programacion.distribuida.authors.lifecycle;
//
//import com.ecwid.consul.v1.ConsulClient;
//import com.ecwid.consul.v1.agent.model.NewService;
//import io.helidon.config.Config;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.enterprise.context.Initialized;
//import jakarta.enterprise.event.Observes;
//import jakarta.enterprise.context.Destroyed;
//import jakarta.inject.Inject;
//import org.eclipse.microprofile.config.ConfigProvider;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//
//import java.net.InetAddress;
//import java.util.Arrays;
//import java.util.UUID;
//
//@ApplicationScoped
//public class ConsulLifeCycle {
//    private String serviceId;
//    private ConsulClient consulClient;
//
//    @Inject
//    @ConfigProperty(name="consul.host", defaultValue = "127.0.0.1")
//    String consulIp;
//
//    @Inject
//    @ConfigProperty(name="consul.port", defaultValue = "8500")
//    Integer consulPort;
//
//    @Inject
//    @ConfigProperty(name="helidon.port", defaultValue = "8080")
//    Integer appPort;
//
//    void init(@Observes @Initialized(ApplicationScoped.class) Object init) throws Exception {
//
//        consulClient = new ConsulClient(consulIp, consulPort);
//        serviceId = "app-authors-" + UUID.randomUUID().toString();
//
//        var ipAddress = InetAddress.getLocalHost();
//
//        NewService service = new NewService();
//        service.setId(serviceId);
//        service.setName("app-authors");
//        service.setAddress(ipAddress.getHostAddress());
//        service.setPort(appPort);
//
//        service.setTags(Arrays.asList(
//                "traefik.enable=true",
//                "traefik.http.routers.router-app-authors.rule=PathPrefix(`/app-authors`)",
//                "traefik.http.routers.router-app-authors.middlewares=middleware-app-authors",
//                "traefik.http.middlewares.middleware-app-authors.stripPrefix.prefixes=/app-authors"
//        ));
//
//        NewService.Check check = new NewService.Check();
//        check.setHttp("http://" + ipAddress.getHostAddress() + ":" + appPort + "/health");
//        check.setInterval("10s");
//        check.setDeregisterCriticalServiceAfter("20s");
//        service.setCheck(check);
//
//        consulClient.agentServiceRegister(service);
//        System.out.println("Service registered in Consul: " + serviceId);
//    }
//
//    void destroy(@Observes @Destroyed(ApplicationScoped.class) Object init) {
//        if (consulClient != null && serviceId != null) {
//            consulClient.agentServiceDeregister(serviceId);
//            System.out.println("Service deregistered from Consul: " + serviceId);
//        }
//    }
//
//
//}