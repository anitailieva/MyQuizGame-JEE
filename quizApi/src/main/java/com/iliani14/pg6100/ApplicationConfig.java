package com.iliani14.pg6100;


import com.iliani14.pg6100.api.CategoryRest;
import io.swagger.jaxrs.config.BeanConfig;;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


//this defines the entry point of REST definitions. Can be only one.
@ApplicationPath("/api")
public class ApplicationConfig extends Application {


  private final Set<Class<?>> classes;


  public ApplicationConfig() {

    /*
      We use SWAGGER to create automatically create documentation
      for the REST service.
      This documentation will be served as a swagger.json file by
      the REST service itself.
      The web page under "webapp" are copied&pasted from the "dist"
      folder in
      https://github.com/swagger-api/swagger-ui
     */
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setVersion("0.0.2");
    beanConfig.setSchemes(new String[]{"http"});
    beanConfig.setHost("localhost:8080");
    beanConfig.setBasePath("/myquiz/api");
    beanConfig.setResourcePackage("com.iliani14.pg6100");

    //AWFUL NAME: this "set" is the one does actually init Swagger...
    beanConfig.setScan(true);

    /*
      Here we define which classes provide REST APIs
     */
    HashSet<Class<?>> c = new HashSet<>();
    c.add(CategoryRest.class);

    //add further configuration to activate SWAGGER
    c.add(io.swagger.jaxrs.listing.ApiListingResource.class);
    c.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);


    classes = Collections.unmodifiableSet(c);
  }

  @Override
  public Set<Class<?>> getClasses() {
    return classes;
  }

}