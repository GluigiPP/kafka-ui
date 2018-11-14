package com.gluigp.kafka.ui.views

import com.github.mvysny.karibudsl.v10.div
import com.github.mvysny.karibudsl.v10.icon
import com.github.mvysny.karibudsl.v10.tab
import com.github.mvysny.karibudsl.v10.tabs
import com.gluigp.kafka.ui.service.KafkaAsyncConsumer
import com.gluigp.kafka.ui.views.components.KafkaConsumerConsole
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.Route

@Route("topics", layout = MainUI::class)
class TopicView : VerticalLayout() {

    private val tabs: Tabs
    private lateinit var plusTab: Tab
    private val consoleTabs = mutableListOf<KafkaConsumerConsole>()
    private val content: Div

    init {
        setSizeFull()

        tabs = tabs {
            width = "100%"
            plusTab = tab { icon(VaadinIcon.PLUS_CIRCLE) }

            addSelectedChangeListener {
                selectTabListener()
            }
        }
        content = div { setSizeFull() }
        addNewConsole()
    }

    private fun selectTabListener() {
        if (tabs.selectedTab == plusTab) {
            addNewConsole()
            return
        }
        consoleTabs.forEach { it.isVisible = false }
        consoleTabs[tabs.selectedIndex].apply { isVisible = true }
    }

    private fun addNewConsole() {
        consoleTabs.forEach { it.isVisible = false }
        val console = KafkaConsumerConsole(KafkaAsyncConsumer())
        consoleTabs.add(console)
        content.add(console)

        tabs.remove(plusTab)
        tabs.tab { label = "Console ${consoleTabs.size}"; tabs.selectedTab = this }
        tabs.add(plusTab)
    }
}