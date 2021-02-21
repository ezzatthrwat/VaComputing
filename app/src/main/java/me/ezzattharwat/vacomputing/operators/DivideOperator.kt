
package me.ezzattharwat.vacomputing.operators


object DivideOperator : Calculate {
  override fun apply(leftOperand: Float, rightOperand: Float): Float = leftOperand.div(rightOperand)
}