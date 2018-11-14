package com.gluigp.kafka.ui.views

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route("", layout = MainUI::class)
class RootView : VerticalLayout() {

    init {
        h1("Kafka UI")
    }
}