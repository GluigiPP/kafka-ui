package com.gluigp.kafka.ui.views

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.RouterLayout
import kotlin.reflect.KClass

@Push
class MainUI : VerticalLayout(), RouterLayout {

    private val content: VerticalLayout
    private val menu: Tabs
    private val routes = mutableMapOf<Tab, KClass<out Component>>()

    init {
        setSizeFull(); isMargin = false; isSpacing = false; isPadding = false

        menu = tabs {
            width = "100%"
            tab { label = "Home"; routes[this] = RootView::class }
            tab { label = "Topics";routes[this] = TopicView::class }
            addSelectedChangeListener { navigateToView(routes[selectedTab] as KClass<out Component>) }
        }

        content = verticalLayout { setSizeFull(); isMargin = false; isSpacing = false; isPadding = false }

        UI.getCurrent().addAfterNavigationListener { event ->
            val view = event.activeChain.first()::class
            menu.selectedTab = routes.filter { it.value == view }.keys.first()
        }
    }

    override fun showRouterLayoutContent(content: HasElement?) {
        this.content.apply {
            removeAll()
            element.appendChild(content!!.element)
        }
    }
}