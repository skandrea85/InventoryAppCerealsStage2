package com.example.andrea.inventoryappcerealsstage2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andrea.inventoryappcerealsstage2.Data.CerealsContract.CerealsEntry;

public class CerealsCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link CerealsCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public CerealsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.listview, parent, false);

    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvProduct = (TextView) view.findViewById(R.id.name);
        TextView tvQuantity = (TextView) view.findViewById(R.id.Quantity);
        TextView tvPrice = (TextView) view.findViewById(R.id.price);
        // Extract properties from cursor
        int nameProduct = cursor.getColumnIndex(CerealsEntry.Column_PRODUCT_NAME);
        int quantity = cursor.getColumnIndex(CerealsEntry.Column_QUANTITY);
        int price = cursor.getColumnIndex(CerealsEntry.Column_PRICE);

        String Product = cursor.getString(nameProduct);
        String Quantity = cursor.getString(quantity);
        String Price = cursor.getString(price);

        String PriceText = "Price: " + Price + " €";
        String QuantityText = "In stock: " + Quantity + " qli";

        tvProduct.setText(Product);
        tvQuantity.setText((QuantityText));
        tvPrice.setText((PriceText));

        final int quantityColumnIndex = cursor.getColumnIndex(CerealsEntry.Column_QUANTITY);
        String currentQuantity = cursor.getString(quantityColumnIndex);
        final int quantityIntCurrent = Integer.valueOf(currentQuantity);

        final int productId = cursor.getInt(cursor.getColumnIndex(CerealsEntry._ID));
        ImageButton SellButton = view.findViewById(R.id.sellButton);

        //Sell button which decrease quantity in storage
        SellButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (quantityIntCurrent > 0) {
                    int newQuantity = quantityIntCurrent - 1;
                    Uri quantityUri = ContentUris.withAppendedId(CerealsEntry.CONTENT_URI, productId);

                    ContentValues values = new ContentValues();
                    values.put(CerealsEntry.Column_QUANTITY, newQuantity);
                    context.getContentResolver().update(quantityUri, values, null, null);
                } else {
                    Toast.makeText(context, "This game is out of stock!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
