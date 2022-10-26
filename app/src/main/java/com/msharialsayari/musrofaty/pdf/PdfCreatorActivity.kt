package com.msharialsayari.musrofaty.pdf

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.LayoutDirection
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.msharialsayari.musrofaty.R
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SenderModel
import com.msharialsayari.musrofaty.business_layer.domain_layer.model.SmsModel
import com.msharialsayari.musrofaty.notifications.makeStatusNotification
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.SharedPreferenceManager
import com.msharialsayari.musrofaty.utils.StringsUtils
import com.tejpratapsingh.pdfcreator.utils.PDFUtil.PDFUtilListener
import com.tejpratapsingh.pdfcreator.views.PDFBody
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView
import com.tejpratapsingh.pdfcreator.views.basic.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PdfCreatorActivity : BasePDFCreator() {

    private val viewModel: PdfCreatorViewModel by viewModels()

    companion object {
         const val SENDER_ID = "SENDER_ID"
         const val FILTER_TIME_OPTION = "FILTER_TIME_OPTION"
         const val FILTER_WORD = "FILTER_WORD"
         const val START_TIME = "START_TIME"
         const val END_TIME = "END_TIME"

        fun startPdfCreatorActivity(activity: Activity, pdfBundle: PdfCreatorViewModel.PdfBundle) {
            val bundle = Bundle()

            bundle.putInt(SENDER_ID, pdfBundle.senderId)
            bundle.putInt(FILTER_TIME_OPTION, pdfBundle.filterTimeId)
            bundle.putString(FILTER_WORD, pdfBundle.filterWord)
            bundle.putLong(START_TIME, pdfBundle.startDate)
            bundle.putLong(END_TIME, pdfBundle.endDate)

            activity.startActivity(Intent(activity, PdfCreatorActivity::class.java).putExtras(bundle)
            )
        }
    }

    private lateinit var smsList: List<SmsModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        makeStatusNotification(
            getString(R.string.notification_generate_pdf_file_title),
            getString(R.string.notification_generate_excel_file_starting_message),
            this)

        initObserver()
        intent?.extras?.let {
            viewModel.senderId     = it.getInt(SENDER_ID, 0)
            viewModel.filterTimeId = it.getInt(FILTER_TIME_OPTION, 0)
            viewModel.filterWord   = it.getString(FILTER_WORD)?:""
            viewModel.startDate    = it.getLong(START_TIME,0L)
            viewModel. endDate      = it.getLong(END_TIME,0L)
            viewModel.getAllBanksSms()
        }




    }

    private fun initObserver() {
        viewModel.event.observe(this) { initEvent(it) }
    }

    private fun initEvent(event: PdfCreatorViewModel.PdfCreatorEvent?) {
        when (event) {
            is PdfCreatorViewModel.PdfCreatorEvent.OnGetSmsSuccess -> createPdfView(
                event.list,
                this
            )
            is PdfCreatorViewModel.PdfCreatorEvent.OnLoading -> {}
            else -> {}
        }

    }

    private fun createPdfView(list: List<SmsModel>, activity: Activity) {
        smsList = list
        createPDF(getString(R.string.app_name), object : PDFUtilListener {
            override fun pdfGenerationSuccess(savedPDFFile: File) {
                makeStatusNotification(
                    getString(R.string.notification_generate_pdf_file_title),
                    getString(R.string.notification_generate_excel_file_done_message),
                    activity)

            }

            override fun pdfGenerationFailure(exception: Exception) {
                Toast.makeText(
                    this@PdfCreatorActivity,
                    getString(R.string.file_not_created),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


    override fun getBodyViews(): PDFBody {
        val pdfBody = PDFBody()

        smsList.forEach {
            pdfBody.addView(createBodyItemView(sms = it))
        }
        return pdfBody

    }

    override fun onShareIconClicked(savedPDFFile: File?) {
        sharePdfFile(savedPDFFile)
        val horizontalView = PDFHorizontalView(applicationContext)
        horizontalView.view.layoutDirection = getLayoutDirection()

    }

    override fun getFooterView(page: Int): PDFHeaderView {
        val footerView = PDFHeaderView(applicationContext)
        val horizontalView = PDFHorizontalView(applicationContext)
        val waterMarkView = PDFHorizontalView(applicationContext)

        footerView.view.layoutDirection = getLayoutDirection()
        horizontalView.view.layoutDirection = getLayoutDirection()
        waterMarkView.view.layoutDirection = getLayoutDirection()


        val waterMarkParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        waterMarkParam.gravity = Gravity.START
        waterMarkView.setLayout(waterMarkParam)
        waterMarkView.view.layoutDirection = getLayoutDirection()
        val imageView = PDFImageView(applicationContext)
        val imageLayoutParam = LinearLayout.LayoutParams(80, 80, 0F)
        imageView.setImageScale(ImageView.ScaleType.CENTER_INSIDE)
        imageView.setImageResource(R.drawable.ic_water_marker_light_mode)
        imageView.setLayout(imageLayoutParam)
        waterMarkView.addView(imageView)

        val pdfAppNameTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.SMALL)
        val appNameParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        appNameParam.setMargins(10, 0, 10, 0)
        pdfAppNameTextView.setText(getString(R.string.app_name))
        pdfAppNameTextView.setLayout(appNameParam)
        pdfAppNameTextView.view.setTypeface(pdfAppNameTextView.view.typeface, Typeface.BOLD)
        waterMarkView.view.gravity = Gravity.CENTER_VERTICAL
     //   waterMarkView.addView(pdfAppNameTextView)
        horizontalView.addView(waterMarkView)

        val pdfTextViewPage = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.SMALL)
        pdfTextViewPage.setText(
            StringsUtils.formatArabicDigits(
                getString(
                    R.string.page_number,
                    page
                )
            )
        )
        pdfTextViewPage.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1F
            )
        )
        pdfTextViewPage.view.gravity = Gravity.END
        horizontalView.view.gravity = Gravity.CENTER_VERTICAL
        horizontalView.addView(pdfTextViewPage)



        footerView.addView(horizontalView)

        return footerView
    }

    override fun getHeaderView(): PDFHeaderView {
        val headerView = PDFHeaderView(applicationContext)
        val verticalView = PDFVerticalView(applicationContext)
        verticalView.view.layoutDirection = getLayoutDirection()


        val pdfBankNameTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H1)
        val bankNameWord = SpannableString(
            String.format(
                SharedPreferenceManager.getLanguage(this),
                SenderModel.getDisplayName(this, smsList[0].senderModel)
            )
        )
        val bankNameParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1F
        )
        bankNameParam.setMargins(20, 20, 20, 20)
        bankNameWord.setSpan(
            ForegroundColorSpan(Color.DKGRAY),
            0,
            bankNameWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        pdfBankNameTextView.text = bankNameWord
        pdfBankNameTextView.setLayout(bankNameParam)
        pdfBankNameTextView.view.gravity = Gravity.CENTER_VERTICAL
        pdfBankNameTextView.view.setTypeface(pdfBankNameTextView.view.typeface, Typeface.BOLD)
        verticalView.addView(pdfBankNameTextView)


        val lineSeparatorView1 =
            PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.BLACK)
        verticalView.addView(lineSeparatorView1)




        headerView.addView(verticalView)

        return headerView

    }

    private fun createBodyItemView(sms: SmsModel): PDFVerticalView {

        val verticalView = PDFVerticalView(applicationContext)
        val horizontalView = PDFHorizontalView(applicationContext)
        verticalView.view.layoutDirection = getLayoutDirection()
        val verticalViewParam =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
        verticalView.setLayout(verticalViewParam)
        horizontalView.view.layoutDirection = getLayoutDirection()

        val horizontalParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        horizontalParam.gravity = Gravity.START
        horizontalView.view.gravity = Gravity.START
        horizontalView.setLayout(horizontalParam)
        val pdfSmsDateLabelTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H2)
        val smsDateLabelWord = SpannableString(
            String.format(
                SharedPreferenceManager.getLanguage(this),
                getString(R.string.excel_sms_date_header)
            )
        )
        val smsDateLabelParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        smsDateLabelParam.setMargins(10, 10, 10, 0)
        smsDateLabelWord.setSpan(
            ForegroundColorSpan(Color.DKGRAY),
            0,
            smsDateLabelWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        pdfSmsDateLabelTextView.text = smsDateLabelWord
        pdfSmsDateLabelTextView.setLayout(smsDateLabelParam)
        pdfSmsDateLabelTextView.view.gravity = Gravity.CENTER_VERTICAL
        pdfSmsDateLabelTextView.view.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        pdfSmsDateLabelTextView.view.setTypeface(
            pdfSmsDateLabelTextView.view.typeface,
            Typeface.BOLD
        )
        horizontalView.addView(pdfSmsDateLabelTextView)


        val pdfSmsDateTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H2)
        val smsDateWord = SpannableString(DateUtils.getDateByTimestamp(sms.timestamp))
        val smsDateParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        smsDateParam.setMargins(10, 10, 10, 0)
        smsDateWord.setSpan(
            ForegroundColorSpan(Color.DKGRAY),
            0,
            smsDateWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        pdfSmsDateTextView.text = smsDateWord
        pdfSmsDateTextView.setLayout(smsDateParam)
        pdfSmsDateTextView.view.gravity = Gravity.CENTER_VERTICAL
        pdfSmsDateTextView.view.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        pdfSmsDateTextView.view.setTypeface(pdfSmsDateTextView.view.typeface, Typeface.NORMAL)
        horizontalView.addView(pdfSmsDateTextView)


        verticalView.addView(horizontalView)

        val pdfSmsBodyTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H2)
        val smsBodyWord = SpannableString(sms.body)
        val smsBodyParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        smsBodyParam.setMargins(10, 10, 10, 10)
        smsBodyWord.setSpan(
            ForegroundColorSpan(Color.DKGRAY),
            0,
            smsBodyWord.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        pdfSmsBodyTextView.text = smsBodyWord
        pdfSmsBodyTextView.setLayout(smsBodyParam)
        pdfSmsBodyTextView.view.gravity = Gravity.CENTER_VERTICAL
        pdfSmsBodyTextView.view.setTypeface(pdfSmsBodyTextView.view.typeface, Typeface.NORMAL)
        verticalView.addView(pdfSmsBodyTextView)

        val lineSeparatorView1 =
            PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.BLACK)
        verticalView.addView(lineSeparatorView1)

        return verticalView


    }

    private fun sharePdfFile(file: File?) {
        file?.let {
            val share = Intent(Intent.ACTION_SEND)
            val fileURI = FileProvider.getUriForFile(
                this,
                applicationContext.packageName.toString() + ".provider",
                file
            )
            share.type = "application/pdf";
            share.putExtra(Intent.EXTRA_STREAM, fileURI)
            try {
                startActivity(share)
            } catch (e: Exception) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }


    private fun getLayoutDirection(): Int {
        return if (SharedPreferenceManager.isArabic(this)) {
            LayoutDirection.RTL
        } else {
            LayoutDirection.LTR
        }

    }

}