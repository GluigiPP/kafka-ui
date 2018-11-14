package com.gluigp.kafka.ui.service

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.*
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

class KafkaAsyncConsumer {

    private var consumerThread: ConsumerThread? = null

    fun startConsumer(host: String, topic: String, listener: (key: String, body: String) -> Unit) {
        consumerThread?.let { it.stop() }

        val props = Properties()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = host
        props[ConsumerConfig.GROUP_ID_CONFIG] = UUID.randomUUID().toString()
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name

        val consumer = KafkaConsumer<String, String>(props)

        consumer.subscribe(listOf(topic))
        consumerThread = ConsumerThread(consumer, listener)
        consumerThread!!.start()
    }

    fun stopConsumer() {
        consumerThread?.stop()
    }
}

private class ConsumerThread(
        val consumer: KafkaConsumer<String, String>,
        val listener: (key: String, body: String) -> Unit) : Runnable {

    private var worker: Thread? = null
    private val running = AtomicBoolean(false)

    fun start() {
        listener.invoke("SYSTEM", "Starting a new consumer.")
        worker = Thread(this)
        worker!!.start()
    }

    fun stop() {
        listener.invoke("SYSTEM", "Stopping the current consumer it can take a few seconds.")
        running.set(false)
        worker?.join()
    }

    override fun run() {
        running.set(true)
        consumer.use { consumer ->
            while (running.get()) {
                val consumerRecords = consumer.poll(Duration.ofSeconds(5))
                consumerRecords.forEach { record ->
                    listener.invoke(record.key(), record.value())
                }
                consumer.commitAsync()
            }
        }
    }
}