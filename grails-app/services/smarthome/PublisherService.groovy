package smarthome

import com.rabbitmq.client.BuiltinExchangeType
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import grails.gorm.transactions.Transactional
import groovy.util.logging.Log
import smarthome.components.Config

import java.util.concurrent.TimeoutException
import java.util.logging.Level

@Log
@Transactional
class PublisherService implements Runnable {
    private final static String EXCHANGE_NAME = "ex_topic"
    private String topic = 'DL'
    private Channel channel

    PublisherService() {
        try {
            ConnectionFactory factory = new ConnectionFactory()
            factory.setHost(Config.getRabbitHost())
            factory.setUsername(Config.getRabbitLogin())
            factory.setPassword(Config.getRabbitPassword())

            Connection connection = factory.newConnection()
            channel = connection.createChannel()

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC)

//            channel.close()
//            connection.close()
        } catch (TimeoutException | IOException e) {
            log.log(Level.ALL, "PUBLISHER ERROR")
            e.printStackTrace()
        }
    }

    void message(String message, String routingKey) {
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"))
        println "[SENT] $message, $routingKey"
    }

    void message(String message) {
        channel.basicPublish(EXCHANGE_NAME, topic, null, message.getBytes("UTF-8"))
        println "[SENT] $message, $topic"
    }


    void setTopic(String topic) {
        this.topic = topic
    }

    void setDevice(long dev) {
        this.topic = 'DL.' + dev
    }

    @Override
    void run() {
        while (true) {
//            for(x in (1..10)) {
//                def light = [
//                        type    : 'lighting',
//                        lighting: x*10
//                ] as JSON
//                message(light as String, 'DL.110620182319')
//                message(light as String, 'DL.120620180022')
//                sleep(500)
//            }
//
//            for(x in (10..1)) {
//                def light = [
//                        type    : 'lighting',
//                        lighting: x*10
//                ] as JSON
//                message(light as String, 'DL.110620182319')
//                message(light as String, 'DL.120620180022')
//                sleep(500)
//            }
        }
    }
}
