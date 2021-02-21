package me.ezzattharwat.vacomputing.operators

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CalculatorEngineTest {

    private val engine = CalculatorEngine()

    @Before
    fun setup() {
        engine.clear()
    }

    @Test
    fun testForAddition() {
        engine.operator = "+"
        val result =  engine.calculate(1f,1f)
        assertThat(result).isEqualTo(2f)
    }

    @Test
    fun testForSubtraction() {
        engine.operator = "-"
        val result =  engine.calculate(-1f,1f)
        assertThat(result).isEqualTo(-2f)
    }

    @Test
    fun testForMultiplication() {
        engine.operator = "*"
        val result =  engine.calculate(1000f,1000f)
        assertThat(result).isEqualTo(1000000f)
    }

    @Test
    fun testForDivision() {
        engine.operator = "/"
        val result =  engine.calculate(1f,1f)
        assertThat(result).isEqualTo(1f)

    }
}