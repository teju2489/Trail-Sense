package com.kylecorry.trail_sense.main

import android.content.Intent
import android.os.Bundle
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyNavController(private val manager: FragmentManager, private val containerId: Int) {

    private val routes = mutableMapOf<String, () -> Fragment>()
    private val fragmentNameMapping = mutableMapOf<String, String>()

    private val routeChangeListeners = mutableListOf<(String?) -> Unit>()
    private val listenerLock = Any()

    private fun onRouteChange(route: String?) {
        val listeners = synchronized(listenerLock) {
            routeChangeListeners.toList()
        }
        listeners.forEach { listener ->
            listener(route)
        }
    }

    init {
        manager.addOnBackStackChangedListener {
            val lastEntryIdx = manager.backStackEntryCount - 1
            if (lastEntryIdx < 0) {
                currentRoute = null
                onRouteChange(null)
                return@addOnBackStackChangedListener
            }
            val lastEntry = manager.getBackStackEntryAt(lastEntryIdx)
            val route = lastEntry.name
            if (route != currentRoute) {
                currentRoute = route
                onRouteChange(route)
            }
        }
    }

    var currentRoute: String? = null
        private set

    fun navigate(
        route: String,
        data: Bundle? = null,
        addToBackStack: Boolean = true,
        resetBackStack: Boolean = false,
        transition: Int? = null
    ) {
        val fragmentLoader = routes[route] ?: return
        val fragment = fragmentLoader()
        fragment.arguments = data ?: Bundle()
        // TODO: Support transitions
        if (resetBackStack) {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        fragmentNameMapping[route] = fragment.javaClass.name

        manager.commit {
            replace(containerId, fragment)
            if (transition != null) {
                setTransition(transition)
            }
            if (addToBackStack) {
                addToBackStack(route)
            }
        }

        if (currentRoute != route) {
            currentRoute = route
            onRouteChange(route)
        }
    }

    fun up() {
        val components = currentRoute?.split("/")

        if (components == null || components.size <= 1) {
            back()
            return
        }

        val remaining = components.dropLast(1).toMutableList()

        while (remaining.isNotEmpty()) {
            val parent = remaining.joinToString("/")
            if (routes.containsKey(parent)) {
                // TODO: Remove this from the back stack
                navigate(parent, addToBackStack = false)
                return
            }
            remaining.removeLast()
        }

        // No parent found, just go back
        back()
    }

    fun back() {
        manager.popBackStack()
    }

    fun addRoute(route: String, action: () -> Fragment) {
        routes[route] = action
    }

    fun <T : Fragment> addRoute(route: String, fragmentClass: Class<T>) {
        routes[route] = { fragmentClass.getDeclaredConstructor().newInstance() }
    }

    inline fun <reified T : Fragment> addRoute(route: String) {
        addRoute(route, T::class.java)
    }

    fun reload() {
        navigate(currentRoute ?: return, addToBackStack = false)
    }

    fun addOnNavigationChangeListener(listener: (String?) -> Unit) {
        synchronized(listenerLock) {
            routeChangeListeners.add(listener)
        }
    }

    fun removeOnNavigationChangeListener(listener: (String?) -> Unit) {
        synchronized(listenerLock) {
            routeChangeListeners.remove(listener)
        }
    }

    fun navigateDeepLink(intent: Intent) {
        val route = intent.getStringExtra("route") ?: return
        val args = intent.getBundleExtra("route_args")
        // TODO: Simulate back stack
        navigate(route, args, resetBackStack = true)
    }

    companion object {
        fun populateDeepLink(intent: Intent, route: String, args: Bundle? = null) {
            intent.putExtra("route", route)
            intent.putExtra("route_args", args)
        }

        fun hasDeepLink(intent: Intent): Boolean {
            return intent.hasExtra("route")
        }
    }

}

fun BottomNavigationView.setupWithMyNavController(
    nav: MyNavController,
    mappings: Map<String, Int>,
    default: Int? = null
) {
    setOnItemSelectedListener { item ->
        val route = mappings.entries.firstOrNull { it.value == item.itemId }?.key
        if (route != null) {
            nav.navigate(route, resetBackStack = true)
        }
        true
    }

    nav.addOnNavigationChangeListener { route ->
        val baseRoute = route?.split("/")?.firstOrNull()
        menu.forEach { item ->
            if (mappings[baseRoute] == item.itemId || (route == null && default == item.itemId)) {
                item.isChecked = true
            }
        }
    }
}