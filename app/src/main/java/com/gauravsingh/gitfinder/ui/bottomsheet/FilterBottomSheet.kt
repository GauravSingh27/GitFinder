package com.gauravsingh.gitfinder.ui.bottomsheet

import android.app.Dialog
import android.content.Context
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import com.gauravsingh.gitfinder.R
import kotlinx.android.synthetic.main.layout_filter_bottom_sheet.view.*


const val ORDER_BY_ASC = "asc"
const val ORDER_BY_DESC = "desc"
const val SORT_STAR = "stars"
const val SORT_FORK = "forks"
const val SORT_UPDATED = "updated"

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


        sort = arguments?.getString("selectedSort")
        orderBy = arguments?.getString("selectedOrderBy") ?: ORDER_BY_DESC

        contentView.apply {
            setOnClickListeners(this)
            performDefaultSelection(this, sort)
            performDefaultSelection(this, orderBy)
        }

        dialog.setContentView(contentView)
    }

    private fun setOnClickListeners(contentView: View) {

        contentView.rb_order_by_asc.setOnClickListener(this)
        contentView.rb_order_by_desc.setOnClickListener(this)
        contentView.rb_sort_star.setOnClickListener(this)
        contentView.rb_sort_forks.setOnClickListener(this)
        contentView.rb_sort_updated.setOnClickListener(this)
        contentView.tv_bottom_sheet_filter_reset.setOnClickListener(this)
        contentView.tv_bottom_sheet_filter_apply.setOnClickListener(this)
    }

    private fun performDefaultSelection(contentView: View, predicate: String?) {

        val radioButtonToBeSelected =
                when (predicate) {
                    SORT_STAR -> contentView.rb_sort_star
                    SORT_FORK -> contentView.rb_sort_forks
                    SORT_UPDATED -> contentView.rb_sort_updated
                    ORDER_BY_ASC -> contentView.rb_order_by_asc
                    ORDER_BY_DESC -> contentView.rb_order_by_desc
                    else -> null
                }
        radioButtonToBeSelected?.isChecked = true
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.tv_bottom_sheet_filter_reset -> resetFilters()
            R.id.tv_bottom_sheet_filter_apply -> applyFilters()
            R.id.rb_order_by_asc -> orderBy = ORDER_BY_ASC
            R.id.rb_order_by_desc -> orderBy = ORDER_BY_DESC
            R.id.rb_sort_star -> sort = SORT_STAR
            R.id.rb_sort_forks -> sort = SORT_FORK
            R.id.rb_sort_updated -> sort = SORT_UPDATED
            else -> dismiss()
        }
        v.rootView.tv_bottom_sheet_filter_apply.isEnabled = true
        v.rootView.tv_bottom_sheet_filter_reset.isEnabled = true
    }

    private fun applyFilters() {
        filterListener.applyFilter(sort, orderBy)
        dismiss()
    }

    private fun resetFilters() {
        filterListener.applyFilter(null, ORDER_BY_DESC)
        dismiss()
    }

    interface FilterListener {
        fun applyFilter(sortBy: String?, orderBy: String)
    }
}



