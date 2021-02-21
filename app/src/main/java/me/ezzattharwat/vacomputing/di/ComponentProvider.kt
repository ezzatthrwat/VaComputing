package me.ezzattharwat.vacomputing.di

fun interface ComponentProvider<T> {
    fun getComponent(): T
}