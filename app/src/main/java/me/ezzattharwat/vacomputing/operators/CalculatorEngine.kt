package me.ezzattharwat.vacomputing.operators

import me.ezzattharwat.vacomputing.error.ExceptionsType
import javax.inject.Inject


class CalculatorEngine @Inject constructor() {

    private var result = "0"
    var operator: String? = "+"
    private var isInInvalidState = false

    fun calculate(num1: Float, num2: Float): Float{

        val operation =  when (operator) {
            "+" -> AddOperator
            "-" -> SubtractOperator
            "*" -> MultiplyOperator
            "/" -> DivideOperator
            else -> throw ExceptionsType.UndefinedOperationException()
        }

            return operation.apply(num1, num2)

    }

    fun clear() {
        operator = "+"
        result = "0"
        isInInvalidState = false
    }
}