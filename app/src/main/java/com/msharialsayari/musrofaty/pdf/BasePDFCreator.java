package com.msharialsayari.musrofaty.pdf;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.github.chrisbanes.photoview.PhotoView;
import com.msharialsayari.musrofaty.R;
import com.msharialsayari.musrofaty.base.BaseActivity;
import com.msharialsayari.musrofaty.utils.StringsUtils;
import com.msharialsayari.musrofaty.utils.pdf.PDFFooterView;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFBody;
import com.tejpratapsingh.pdfcreator.views.PDFFooterView;
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePDFCreator extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PDFCreatorActivity";

    private int headerLayoutHeight = 0;
    private int footerLayoutHeight = 0;
    private int selectedPreviewPage = 0;

    LinearLayout layoutPageParent;
    ConstraintLayout layoutPrintPreview;
    TextView textViewPageNumber;
    PhotoView imageViewPDFPreview;
    ProgressBar progressBar;
    ImageButton buttonNextPage, buttonPreviousPage;
    Toolbar my_toolbar;

    ArrayList<Bitmap> pagePreviewBitmapList = new ArrayList<>();

    File savedPDFFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfcreator_delegate);

        layoutPageParent = findViewById(R.id.layoutPdfPreview);
        layoutPrintPreview = findViewById(R.id.layoutPrintPreview);
        imageViewPDFPreview = layoutPrintPreview.findViewById(R.id.imagePreviewPdfPrescription);
        textViewPageNumber = layoutPrintPreview.findViewById(R.id.textViewPreviewPageNumber);
        progressBar = findViewById(R.id.progressBar);

        layoutPageParent.removeAllViews();

        my_toolbar = findViewById(R.id.my_toolbar);
        buttonNextPage = layoutPrintPreview.findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(this);
        buttonPreviousPage = layoutPrintPreview.findViewById(R.id.buttonPreviousPage);
        buttonPreviousPage.setOnClickListener(this);
        setUpToolbar();
    }

    private void setUpToolbar() {
        my_toolbar.setTitle(R.string.pdf_preview_title);
        my_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        my_toolbar.inflateMenu(R.menu.pdf_preview_menu);

        my_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.share) {
                    onShareIconClicked(savedPDFFile);
                    return true;
                }
                return false;
            }
        });


    }

    public void createPDF(String fileName, final PDFUtil.PDFUtilListener pdfUtilListener) {
        ArrayList<View> bodyViewList = new ArrayList<>();
        View header = null;
        if (getHeaderView() != null) {
            header = getHeaderView().getView();
            header.setTag(PDFHeaderView.class.getSimpleName());
            addViewToTempLayout(layoutPageParent, header);
        }

        if (getBodyViews() != null) {
            for (PDFView pdfView : getBodyViews().getChildViewList()) {
                View bodyView = pdfView.getView();
                bodyView.setTag(PDFBody.class.getSimpleName());
                bodyViewList.add(bodyView);
                addViewToTempLayout(layoutPageParent, bodyView);
            }
        }

        View footer = null;
        if (getFooterView(0) != null){
            footer = getFooterView(0).getView();
            footer.setTag(PDFFooterView.class.getSimpleName());
            addViewToTempLayout(layoutPageParent, footer);
        }

        createPDFFromViewList(header, bodyViewList, footer,fileName, new PDFUtil.PDFUtilListener() {
            @Override
            public void pdfGenerationSuccess(File savedPDFFile) {
                progressBar.setVisibility(View.GONE);
                try {
                    pagePreviewBitmapList.clear();
                    pagePreviewBitmapList.addAll(PDFUtil.pdfToBitmap(savedPDFFile));
                    layoutPrintPreview.setVisibility(View.VISIBLE);
                    selectedPreviewPage = 0;
                    imageViewPDFPreview.setImageBitmap(pagePreviewBitmapList.get(selectedPreviewPage));
                    textViewPageNumber.setText(StringsUtils.formatArabicDigits(getString(R.string.pdf_preview_pages,selectedPreviewPage + 1, pagePreviewBitmapList.size())));


                } catch (Exception e) {
                    e.printStackTrace();
                    imageViewPDFPreview.setVisibility(View.GONE);
                    textViewPageNumber.setVisibility(View.GONE);
                    buttonNextPage.setVisibility(View.GONE);
                    buttonPreviousPage.setVisibility(View.GONE);
                }
                BasePDFCreator.this.savedPDFFile = savedPDFFile;
                pdfUtilListener.pdfGenerationSuccess(savedPDFFile);
            }

            @Override
            public void pdfGenerationFailure(Exception exception) {
                progressBar.setVisibility(View.GONE);
                pdfUtilListener.pdfGenerationFailure(exception);
            }
        });
    }

    /**
     * Creates a paginated PDF page views from list of views those are already rendered on screen
     * (Only rendered views can give height)
     *
     * @param tempViewList list of views to create pdf views from, view should be already rendered to screen
     */
    private void createPDFFromViewList(final View headerView, @NonNull final ArrayList<View> tempViewList, final View footerView , @NonNull final String filename, final PDFUtil.PDFUtilListener pdfUtilListener) {
        tempViewList.get(tempViewList.size() - 1).post(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                // Clean temp folder
                final FileManager fileManager = FileManager.getInstance();
                fileManager.cleanTempFolder(getApplicationContext());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final List<View> pdfPageViewList = new ArrayList<>();
                        layoutPageParent.removeAllViews();

                        if (headerView != null) {
                            // If item is a page header, store its height so we can add it to all pages without waiting to render it every time
                            headerLayoutHeight = headerView.getHeight();
                        }

                        if (footerView != null) {
                            // If item is a page header, store its height so we can add it to all pages without waiting to render it every time
                            footerLayoutHeight = footerView.getHeight();
                        }

                        int pageIndex = 1;
                        for (View viewItem : tempViewList) {

                            LinearLayout currentPDFLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_pdf_page, null);
                            currentPDFLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                            pdfPageViewList.add(currentPDFLayout);

                            // Add page header again
                            if (headerLayoutHeight > 0) {
                                // If height is available, only then add header
                                LinearLayout layoutHeader = getHeaderView().getView();
                                addViewToTempLayout(layoutPageParent, layoutHeader);
                                layoutPageParent.removeView(layoutHeader);
                                currentPDFLayout.addView(layoutHeader);
                            }


                            // Add page Body again
                            layoutPageParent.removeView(viewItem);
                            currentPDFLayout.addView(viewItem);


                            // Add page footer again
                            if (footerLayoutHeight > 0) {
                                // If height is available, only then add footer
                                LinearLayout layoutFooter = getFooterView(pageIndex).getView();
                                addViewToTempLayout(layoutPageParent, layoutFooter);
                                layoutPageParent.removeView(layoutFooter);
                                currentPDFLayout.addView(layoutFooter);
                            }

                            pageIndex = pageIndex + 1;

                        }

                        PDFUtil.getInstance().generatePDF(pdfPageViewList, fileManager.createTempFileWithName(getApplicationContext(), filename + ".pdf", false).getAbsolutePath(), pdfUtilListener);
                    }
                });
            }
        });
    }

    private void addViewToTempLayout(LinearLayout layoutPageParent, View viewToAdd) {
        layoutPageParent.addView(viewToAdd);
    }

    @Override
    public void onClick(View v) {
        if (progressBar.getVisibility() != View.VISIBLE) {
            if (v == buttonNextPage) {
                if (selectedPreviewPage == pagePreviewBitmapList.size() - 1) {
                    return;
                }
                selectedPreviewPage = selectedPreviewPage + 1;
                imageViewPDFPreview.setImageBitmap(pagePreviewBitmapList.get(selectedPreviewPage));
                textViewPageNumber.setText(StringsUtils.formatArabicDigits(getString(R.string.pdf_preview_pages,selectedPreviewPage + 1, pagePreviewBitmapList.size())));

            } else if (v == buttonPreviousPage) {
                if (selectedPreviewPage == 0) {
                    return;
                }
                selectedPreviewPage = selectedPreviewPage - 1;
                imageViewPDFPreview.setImageBitmap(pagePreviewBitmapList.get(selectedPreviewPage));
                textViewPageNumber.setText(StringsUtils.formatArabicDigits(getString(R.string.pdf_preview_pages,selectedPreviewPage + 1, pagePreviewBitmapList.size())));
            }
        }
    }

    protected abstract PDFHeaderView getHeaderView();

    protected abstract PDFBody getBodyViews();

    protected abstract void onShareIconClicked(File savedPDFFile);

    protected abstract PDFHeaderView getFooterView(int page);
}
