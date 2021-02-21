package me.ezzattharwat.vacomputing.error

import java.lang.RuntimeException
sealed class ExceptionsType{
    class UndefinedOperationException : RuntimeException("Operation is undefined.")
}
