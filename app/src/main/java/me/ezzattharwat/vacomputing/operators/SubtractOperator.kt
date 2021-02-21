
package me.ezzattharwat.vacomputing.operators


object SubtractOperator : Calculate {
  override fun apply(leftOperand: Float, rightOperand: Float): Float = leftOperand.minus(rightOperand)
}