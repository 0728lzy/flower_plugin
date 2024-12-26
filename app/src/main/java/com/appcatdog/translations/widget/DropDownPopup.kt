package com.appcatdog.translations.widget

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import com.appcatdog.translations.R
import com.appcatdog.translations.ext.thrillClickListener
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.appcatdog.translations.databinding.ItemPopupDropDownBinding
import com.appcatdog.translations.databinding.PopupDropDownBinding

class DropDownPopup(
    private val targetView: View,
) : PopupWindow(targetView.context) {

    companion object {
        @JvmStatic
        fun build(
            view: View,
        ): DropDownPopup {
            return DropDownPopup(view)
        }
    }

    @JvmOverloads
    fun showDown(offsetX: Int = 0, offsetY: Int = 0, gravity: Int = Gravity.START) {
        val scale = targetView.context.resources.displayMetrics.density
        val offset = (9f * scale + 0.5f).toInt()
        width = targetView.width * 2 + offset * 2
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        showAsDropDown(targetView, offsetX - offset, offsetY, gravity)
    }

    private var binding: PopupDropDownBinding

    var data: MutableList<String>
        get() = mutableListOf()
        set(value) {
            binding.listView.bindingAdapter.models = value
        }

    var selectPosition: Int = -1
    var selectCallback: ((data: String, position: Int) -> Unit)? = null

    init {
        setBackgroundDrawable(
            GradientDrawable().apply {
                setColor(
                    ContextCompat.getColor(
                        targetView.context,
                        R.color.transparent
                    )
                )
            }
        )

        View.inflate(targetView.context, R.layout.popup_drop_down, null).apply {
            binding = PopupDropDownBinding.bind(this)
            // binding.listView.setAdapter(adapter())
            // binding.listView.setLoadingLayoutVisible(false)
            R.layout.item_popup_drop_down
            binding.listView.linear().setup {
                addType<String>(R.layout.item_popup_drop_down)
                onBind {
                    getBinding<ItemPopupDropDownBinding>().apply {
                        val data = getModel<String>()
                        tvName.text = data

                        root.thrillClickListener {
                            selectCallback?.invoke(data, modelPosition)
                            dismiss()
                        }
                    }
                }

            }
            contentView = this
        }
        isFocusable = true
        isOutsideTouchable = true
        inputMethodMode = INPUT_METHOD_NEEDED
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
    }

}