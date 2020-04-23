package commanderpepper.advancetimer.ui

import it.sephiroth.android.library.numberpicker.NumberPicker
import it.sephiroth.android.library.numberpicker._OnNumberPickerChangeListener

interface TimeSelectionOnClickHandler {

    fun setHourListener() : _OnNumberPickerChangeListener.() -> Unit

    fun minuteDoOnProgressChanged(func: it.sephiroth.android.library.numberpicker._OnNumberPickerChangeListener.() -> Unit)

    fun secondDoOnProgressChanged(func: it.sephiroth.android.library.numberpicker._OnNumberPickerChangeListener.() -> Unit)


}