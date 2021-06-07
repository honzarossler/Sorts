package cz.janrossler.sorts.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import cz.janrossler.sorts.R;

public abstract class Template {
    public static final String DIALOG_SEARCH_RESULT = "dialog_search_result";

    @NonNull
    public static <T> DialogInterface getDialogFor(Activity activity, @NonNull String type, T data){
        DialogInterface dialog;

        switch (type){
            case DIALOG_SEARCH_RESULT:
                BottomSheetDialog builderSearchResult = new BottomSheetDialog(activity, R.style.TransparentDialogTheme);
                View view = activity.getLayoutInflater()
                        .inflate(R.layout.dialog_search_result, null);
                ImageView image_animated = view.findViewById(R.id.image_animated);
                TextView text_number = view.findViewById(R.id.text_number);
                TextView text_amount = view.findViewById(R.id.text_amount);

                Glide.with(activity)
                        .load(R.drawable.ic_searching)
                        .placeholder(R.drawable.ic_searching)
                        .into(image_animated);

                try{
                    if(!(data instanceof BinarySearchTree.SearchResult))
                        throw new Exception("Unsupported format");

                    BinarySearchTree.SearchResult result = (BinarySearchTree.SearchResult) data;
                    text_number.setText(
                            activity.getString(R.string.dialog_message_result_number)
                                    .replace("%num%", String.valueOf(result.value)));
                    text_amount.setText(
                            activity.getString(R.string.dialog_message_result_amount)
                                    .replace("%amount%", String.valueOf(result.amount)));

                    builderSearchResult.setContentView(view);
                }catch (Exception e){
                    e.printStackTrace();
                    builderSearchResult.setTitle(activity.getString(R.string.dialog_title_unsupported));
                }

                dialog = builderSearchResult;
                break;
            default:
                AlertDialog.Builder builderUnknown = new AlertDialog.Builder(activity);
                builderUnknown.setTitle(activity.getString(R.string.dialog_title_unsupported));
                builderUnknown.setMessage(activity.getString(R.string.dialog_message_unsupported));
                builderUnknown.setPositiveButton(activity.getString(R.string.action_ok), null);
                dialog = builderUnknown.create();
                break;
        }

        return dialog;
    }
}
