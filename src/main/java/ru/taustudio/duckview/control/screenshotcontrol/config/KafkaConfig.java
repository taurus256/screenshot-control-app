package ru.taustudio.duckview.control.screenshotcontrol.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.taustudio.duckview.shared.JobDescription;

@Configuration
public class KafkaConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String boostrapServers;

  @Value("${spring.kafka.properties.sasl.jaas.config}")
  private String saslConfig;

  @Value("${spring.kafka.properties.sasl.mechanism}")
  private String saslMechanism;

  @Value("${spring.kafka.properties.security.protocol}")
  private String saslProtocol;

  @Bean
  public ProducerFactory<String, JobDescription> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put("sasl.jaas.config", saslConfig);
    props.put("sasl.mechanism", saslMechanism);
    props.put("security.protocol", saslProtocol);
    props.put("jaas.enabled", true);
    // See https://kafka.apache.org/documentation/#producerconfigs for more properties
    return props;
  }

  @Bean
  public KafkaTemplate<String, JobDescription> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
