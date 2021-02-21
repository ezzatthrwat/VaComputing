package me.ezzattharwat.vacomputing.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.util.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import me.ezzattharwat.vacomputing.data.db.EquationEntity
import me.ezzattharwat.vacomputing.getOrAwaitValueTest
import me.ezzattharwat.vacomputing.operators.CalculatorEngine
import me.ezzattharwat.vacomputing.other.Constant
import me.ezzattharwat.vacomputing.repository.FakeRepo
import me.ezzattharwat.vacomputing.utils.InstantExecutorExtension
import org.junit.After
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock


@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class AppViewModelTest {

    @get:Rule
    var instantTaskExecutorRule =  InstantTaskExecutorRule()
    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()




    @Mock
    private lateinit var appViewModel: AppViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @BeforeEach
    fun setup() {
        appViewModel = AppViewModel(FakeRepo(), CalculatorEngine())
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `test calculation when num 1 equal null return error msg` () {
        val result =  appViewModel.doCalculation(num1 = null, num2 = 1f, operator = "+")
        assertThat(result.errorMassage).isEqualTo(Constant.NUM_1_NULL_MSG)
    }

    @Test
    fun `test calculation when num 2 equal null return error msg` () {
        val result =  appViewModel.doCalculation(num1 = 1f, num2 = null, operator = "+")
        assertThat(result.errorMassage).isEqualTo(Constant.NUM_2_NULL_MSG)
    }

    @Test
    fun `test calculation when num 1 equal 0 and operator equal div return error msg` () {
        val result =  appViewModel.doCalculation(num1 = 0f, num2 = 1f, operator = "/")
        assertThat(result.errorMassage).isEqualTo(Constant.CANT_DIVIDE)
    }

    @Test
    fun `test calculation when operator equal null return error msg` () {
        val result =  appViewModel.doCalculation(num1 = 1f, num2 = 1f, operator = "")
        assertThat(result.errorMassage).isEqualTo(Constant.OPERATOR_NULL_OR_EMPTY_MSG)
    }

    @Test
    fun `test insert equation with valid input, returns valid id`(){
        val insertedID = 1
        val equationEntity = EquationEntity(1f, 1f, "+", 2f, "location", true)
        equationEntity.id = 1

        appViewModel.insertEquation(equationEntity)

        val result = appViewModel.insertedID.getOrAwaitValueTest()
        assertThat(result.getContentIfNotHandled()?.toInt()).isEqualTo(insertedID)
    }


    @Test
    fun `test get all equations, returns List`(){

        runBlockingTest {
            val equationEntity = EquationEntity(1f, 1f, "+", 2f, "location", true)
            equationEntity.id = 1

            appViewModel.insertEquation(equationEntity)

            appViewModel.getAllEquations()

            val result = appViewModel.allEquationLiveData.getOrAwaitValueTest()
            assertThat(result.data).isNotEmpty()
        }

    }


}