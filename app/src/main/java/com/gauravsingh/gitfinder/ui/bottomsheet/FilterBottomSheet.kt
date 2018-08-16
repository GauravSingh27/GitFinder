package com.gauravsingh.gitfinder.ui.bottomsheet

import android.app.Dialog
import android.content.Context
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.gauravsingh.gitfinder.R
import kotlinx.android.synthetic.main.layout_filter_bottom_sheet.view.*


class FilterBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var filterListener: FilterListener
    private var sort: String? = null
    private lateinit var orderBy: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        filterListener = activity as FilterListener
    }

    override fun setupDialog(dialog: Dialog, style: Int) {

        val contentView = View.inflate(context, R.layout.layout_filter_bottom_sheet, null)


        contentView.rb_order_by_asc.setOnClickListener(this)
        contentView.rb_order_by_desc.setOnClickListener(this)
        contentView.rb_sort_star.setOnClickListener(this)
        contentView.rb_sort_forks.setOnClickListener(this)
        contentView.rb_sort_updated.setOnClickListener(this)
        contentView.tv_bottom_sheet_filter_reset.setOnClickListener(this)
        contentView.tv_bottom_sheet_filter_apply.setOnClickListener(this)


        performDefaultSelection(contentView)

        dialog.setContentView(contentView)
    }

    private fun performDefaultSelection(contentView: View) {

        sort = arguments?.getString("selectedSort")
        orderBy = arguments?.getString("selectedOrderBy") ?: ORDER_BY_DESC

        var radioButtonToBeSelected =
                when (sort) {
                    SORT_STAR -> contentView.rb_sort_star
                    SORT_FORK -> contentView.rb_sort_forks
                    SORT_UPDATED -> contentView.rb_sort_updated
                    else -> null
                }
        radioButtonToBeSelected?.isChecked = true

        radioButtonToBeSelected = when (orderBy) {
            ORDER_BY_ASC -> contentView.rb_order_by_asc
            else -> contentView.rb_order_by_desc
        }

        radioButtonToBeSelected?.isChecked = true
    }

    override fun onClick(v: View) {
        v.rootView.tv_bottom_sheet_filter_apply.isEnabled = true
        v.rootView.tv_bottom_sheet_filter_reset.isEnabled = true
        when (v.id) {
            R.id.tv_bottom_sheet_filter_reset -> {
                filterListener.applyFilter(null, ORDER_BY_DESC)
                dismiss()
            }
            R.id.tv_bottom_sheet_filter_apply -> {
                filterListener.applyFilter(sort, orderBy)
                dismiss()
            }
            R.id.rb_order_by_asc -> orderBy = ORDER_BY_ASC
            R.id.rb_order_by_desc -> orderBy = ORDER_BY_DESC
            R.id.rb_sort_star -> sort = SORT_STAR
            R.id.rb_sort_forks -> sort = SORT_FORK
            R.id.rb_sort_updated -> sort = SORT_UPDATED
            else -> dismiss()
        }
    }

    interface FilterListener {
        fun applyFilter(sortBy: String?, orderBy: String)
    }

}

const val ORDER_BY_ASC = "asc"
const val ORDER_BY_DESC = "desc"
const val SORT_STAR = "stars"
const val SORT_FORK = "forks"
const val SORT_UPDATED = "updated"



