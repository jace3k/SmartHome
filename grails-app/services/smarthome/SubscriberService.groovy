package smarthome

import com.rabbitmq.client.*
import grails.gorm.transactions.Transactional
import groovy.util.logging.Log
import org.grails.web.json.JSONException
import org.grails.web.json.JSONObject
import smarthome.components.Config

import java.util.concurrent.TimeoutException

@Log
@Transactional
class SubscriberService implements Runnable {
    public String topic
    private String exchange_name = "ex_topic"

    SubscriberService(String topic) {
        this.topic = topic
    }

    @Override
    void run() {
        System.out.println(">> Starting Subscriber")
        if (Config.getCredentials()) {
            connect()
        } else {
            System.out.println("Authentication from server file failed.")
        }
    }

    private void connect() {
        ConnectionFactory factory = new ConnectionFactory()
        factory.setHost(Config.getRabbitHost())
        factory.setUsername(Config.getRabbitLogin())
        factory.setPassword(Config.getRabbitPassword())
        factory.setConnectionTimeout(5000)

        try {
            Connection connection = factory.newConnection()
            Channel channel = connection.createChannel()

            channel.exchangeDeclare(exchange_name, BuiltinExchangeType.TOPIC)
            String queueName = channel.queueDeclare().getQueue()
            channel.queueBind(queueName, exchange_name, topic)

            System.out.println(">> Subscriber started. Waiting for messages")

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8")
                    def json = null
                    try {
                        json = new JSONObject(message)
                    } catch (JSONException ignored) {
                        json = message
                    }

                    println "[RECEIVED] $json, ${envelope.routingKey}"
                    Measurement.withTransaction {
                        saveMeasurement(json)
                    }

                }
            }
            channel.basicConsume(queueName, true, consumer)
        } catch (IOException | TimeoutException e) {
            e.printStackTrace()
        }
    }

    private static void saveMeasurement(def json) {
        def device = Device.findByDevice_id(json['ID'] as long)
        if(!device) {
            device = new Device(device_id: json['ID'] as long, room: Room.findByName('none'), assigned: false).save()
        }
        if (device.assigned) {
            def type = Type.findByName(json['type'] as String)
            if (!type) {
                type = new Type(name: json['type']).save()
            }
            long date
            if(json['date']) {
                date = Date.parse('hh:mm:ss', json['date'] as String, TimeZone.getTimeZone('UTC')).getTime()
            } else {
                date = new Date().getTime()
            }

            def params = [
                    type  : type,
                    value : json['value'],
                    room  : device.room,
                    device: device,
                    time  : date
            ]

            Measurement m = new Measurement(params)
            if (!m.save()) {
                println "[ERROR] Saving measurement error. Measurement: $m"
            }
        } else {
            println "[INFO] Unassigned device"
        }
    }
}
