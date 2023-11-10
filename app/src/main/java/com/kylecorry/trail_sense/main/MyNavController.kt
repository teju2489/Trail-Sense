package com.kylecorry.trail_sense.main

import android.os.Bundle
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyNavController(private val manager: FragmentManager, private val containerId: Int){

    private val routes = mutableMapOf<String, () -> Fragment>()
    private val fragmentNameMapping = mutableMapOf<String, String>()
    private var onRouteChangeListener: (String?) -> Unit = {}

    init {
        manager.addOnBackStackChangedListener {
            val lastEntryIdx = manager.backStackEntryCount - 1
            if (lastEntryIdx < 0) {
                currentRoute = null
                onRouteChangeListener(null)
                return@addOnBackStackChangedListener
            }
            val lastEntry = manager.getBackStackEntryAt(lastEntryIdx)
            val route = lastEntry.name
            if (route != currentRoute) {
                currentRoute = route
                onRouteChangeListener(route)
            }
        }
    }

    var currentRoute: String? = null
        private set

    fun navigate(route: String, data: Bundle? = null, addToBackStack: Boolean = true, resetBackStack: Boolean = false) {
        val fragmentLoader = routes[route] ?: return
        val fragment = fragmentLoader()
        fragment.arguments = data ?: Bundle()
        // TODO: Support transitions
        if (resetBackStack){
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        fragmentNameMapping[route] = fragment.javaClass.name

        manager.commit {
            replace(containerId, fragment)
            if (addToBackStack) {
                addToBackStack(route)
            }
        }

        if (currentRoute != route) {
            currentRoute = route
            onRouteChangeListener(route)
        }

    }

    fun back() {
        manager.popBackStack()
    }

    fun addRoute(route: String, action: () -> Fragment) {
        routes[route] = action
    }

    fun <T: Fragment> addRoute(route: String, fragmentClass: Class<T>){
        routes[route] = { fragmentClass.getDeclaredConstructor().newInstance() }
    }

    inline fun <reified T: Fragment> addRoute(route: String){
        addRoute(route, T::class.java)
    }

    fun reload(){
        navigate(currentRoute ?: return, addToBackStack = false)
    }

    fun setOnNavigationChangeListener(listener: (String?) -> Unit){
        onRouteChangeListener = listener
    }
}

fun BottomNavigationView.setupWithMyNavController(nav: MyNavController, mappings: Map<String, Int>, default: Int? = null){
    setOnItemSelectedListener { item ->
        val route = mappings.entries.firstOrNull { it.value == item.itemId }?.key
        if (route != null){
            nav.navigate(route, resetBackStack = true)
        }
        true
    }

    // TODO: Allow multiple listeners
    nav.setOnNavigationChangeListener { route ->
        menu.forEach { item ->
            if (mappings[route] == item.itemId || (route == null && default == item.itemId)){
                item.isChecked = true
            }
        }
    }}