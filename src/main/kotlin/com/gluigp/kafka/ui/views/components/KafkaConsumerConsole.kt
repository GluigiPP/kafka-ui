package com.gluigp.kafka.ui.views.components

import com.github.mvysny.karibudsl.v10.*
import com.gluigp.kafka.ui.service.KafkaAsyncConsumer
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField

class KafkaConsumerConsole(
        private val consumer: KafkaAsyncConsumer
) : VerticalLayout() {

    companion object {
        const val DEFAULT_HOST = "localhost:9092"
    }

    private lateinit var kafkaHost: TextField
    private lateinit var kafkaTopic: TextField
    private val eventsBox: TextArea

    init {
        setSizeFull(); isMargin = false; isPadding = false
        horizontalLayout {
            width = "100%"

            label("Kafka Server:")
            kafkaHost = textField { value = DEFAULT_HOST }
            label("Topic:")
            kafkaTopic = textField()

            val start = button("Consume") { element.setAttribute("theme", "primary") }
            val stop = button("Stop") { isEnabled = false }

            start.addClickListener {
                Notification("Start Consuming ${kafkaHost.value}::${kafkaTopic.value}", 3000).open()
                registerTopic()
                start.isEnabled = false
                stop.isEnabled = true
            }
            stop.addClickListener {
                Notification("Stopping Consumer", 3000).open()
                consumer.stopConsumer()
                start.isEnabled = true
                stop.isEnabled = false
            }
        }

        eventsBox = textArea {
            setSizeFull();isEnabled = false
            value = "Waiting until a message came."
        }
    }

    private fun registerTopic() {
        val ui = ui.get()
        consumer.startConsumer(kafkaHost.value, kafkaTopic.value) { _: String, body: String ->
            ui.access {
                if (eventsBox.value.startsWith("Waiting")) {
                    eventsBox.value = ""
                }
                eventsBox.value = "${eventsBox.value}\n$body"
            }
        }
    }
}