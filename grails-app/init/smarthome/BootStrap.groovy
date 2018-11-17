package smarthome

class BootStrap {

    def init = { servletContext ->
        new Room(name: 'none').save()

        new Thread(new SubscriberService("UL")).start()
        new Thread(new PublisherService()).start()

    }
    def destroy = {
    }
}
