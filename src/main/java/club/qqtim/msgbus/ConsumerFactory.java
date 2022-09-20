package club.qqtim.msgbus;

import org.apache.kafka.clients.consumer.Consumer;

public interface ConsumerFactory<K, V> {

    Consumer<K, V> buildCustomer();
}
