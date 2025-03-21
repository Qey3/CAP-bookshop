package com.example.config;

import com.sap.cloud.sdk.cloudplatform.connectivity.DefaultDestinationLoader;
import com.sap.cloud.sdk.cloudplatform.connectivity.DefaultHttpDestination;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.security.BasicCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Profile("mocked")
public class DestinationConfiguration {

  @Autowired
  private Environment environment;

  @EventListener
  void applicationReady(ApplicationReadyEvent ready) {
    Integer port = 8095;
    String destinationName = environment.getProperty(
        "cds.remote.services.WarehouseService.destination.name");

    if (port != null && destinationName != null) {
      DefaultHttpDestination httpDestination = DefaultHttpDestination
          .builder("http://localhost:" + port)
          .basicCredentials(new BasicCredentials("admin", "admin"))
          .name(destinationName).build();

      DestinationAccessor.prependDestinationLoader(
          new DefaultDestinationLoader().registerDestination(httpDestination));
    }
  }

}
