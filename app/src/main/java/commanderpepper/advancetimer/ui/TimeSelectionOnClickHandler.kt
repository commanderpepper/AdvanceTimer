package commanderpepper.advancetimer.ui

import it.sephiroth.android.library.numberpicker.NumberPicker
import it.sephiroth.android.library.numberpicker._OnNumberPickerChangeListener

interface TimeSelectionOnClickHandler {

    fun setHourListener(numberPicker: NumberPicker): _OnNumberPickerChangeListener

    fun setMinuteListener(numberPicker: NumberPicker): _OnNumberPickerChangeListener

    fun setSecondListener(numberPicker: NumberPicker): _OnNumberPickerChangeListener


}