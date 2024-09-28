package com.pet.translator.adapter.dj

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.pet.translator.R
import com.pet.translator.adapter.dj.base.BaseAdapter
import com.pet.translator.adapter.dj.base.BaseViewHolder
import com.pet.translator.bean.dj.HelpQuestionBean

class RBHelpQuestionAdapter(
    context: Context,
    id: Int,
    data: List<HelpQuestionBean>
) :
    BaseAdapter<HelpQuestionBean>(context, id, data) {
    @SuppressLint("SuspiciousIndentation")
    override fun bindView(holder: BaseViewHolder, data: HelpQuestionBean) {


      val clFrame =   holder.getWidgetFromId<ConstraintLayout>(R.id.contact_item_cl_frame)
      val questionTitle =   holder.getWidgetFromId<TextView>(R.id.contact_tv_question)

        if(data?.isSelected==true){
            clFrame.setBackgroundResource(R.drawable.bg_radius_12_blue_while_contact)
            questionTitle.setTextColor(ContextCompat.getColor(context, R.color.color_blue))
        }else{
            clFrame.setBackgroundResource(R.drawable.bg_radius_12_grey_while)
            questionTitle.setTextColor(ContextCompat.getColor(context, R.color.colorA9A9A9))
        }

        questionTitle.text = data.name



    }
}