package edu.upc.dsa;

import io.swagger.jaxrs.config.BeanConfig;
import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

public class Main {

    public static final String BASE_URI = "http://0.0.0.0:8080/dsaApp/";
    private static final Logger logger = Logger.getLogger(Main.class);

    public static HttpServer startServer() {
        // Registra todos los servicios REST del paquete edu.upc.dsa.services
        final ResourceConfig rc = new ResourceConfig().packages("edu.upc.dsa.services");
        rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
        rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/dsaApp");
        beanConfig.setContact("support@example.com");
        beanConfig.setDescription("REST API for the EETAC campus shop (Minim 1)");
        beanConfig.setLicense("Apache 2.0");
        beanConfig.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");
        beanConfig.setResourcePackage("edu.upc.dsa.services");
        beanConfig.setTermsOfServiceUrl("http://www.example.com/resources/eula");
        beanConfig.setTitle("Minim 1 - Shop API");
        beanConfig.setVersion("1.0.0");
        beanConfig.setScan(true);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();

        // Swagger UI servida desde el webjar: http://localhost:8080/swagger/
        server.getServerConfiguration().addHttpHandler(
                new CLStaticHttpHandler(Main.class.getClassLoader(), "/META-INF/resources/webjars/swagger-ui/3.52.5/"),
                "/swagger/");

        logger.info("Jersey app started at " + BASE_URI);
        logger.info("Swagger UI: http://localhost:8080/swagger/?url=/dsaApp/swagger.json");
        logger.info("Hit enter to stop it...");
        System.in.read();
        server.shutdownNow();
    }
}
