package com.example.andrea.inventoryappcerealsstage2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.andrea.inventoryappcerealsstage2.Data.CerealsContract.CerealsEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ImageButton mIncrement;
    private ImageButton mDecrement;
    /**
     * int for quantity check
     */
    private int givenQuantity;
    private ImageButton phone;
    private static final int CEREALS_LOADER = 0;
    private EditText NameProductEditText;

    private EditText QuantityEditText;

    private EditText PriceEditText;

    private EditText PartnerNameEditText;

    private EditText PartnerContactEditText;
    private Uri mCurrentCerealsUri;

    private boolean mCerealHasChanged = false;
    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mCerealHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        mCurrentCerealsUri = intent.getData();

        if (mCurrentCerealsUri == null) {

            setTitle(getString(R.string.title_new_cereals));
            invalidateOptionsMenu();
        } else {
            setTitle("Edit product");
            getLoaderManager().initLoader(CEREALS_LOADER, null, this);
        }


        mIncrement = findViewById(R.id.increment);
        mDecrement = findViewById(R.id.decrement);
        phone = findViewById(R.id.callPhone);

        NameProductEditText = (EditText) findViewById(R.id.edit_name_product);
        QuantityEditText = (EditText) findViewById(R.id.quantity);
        PriceEditText = (EditText) findViewById(R.id.price);

        PartnerNameEditText = (EditText) findViewById(R.id.edit_name_partner);

        PartnerContactEditText = (EditText) findViewById(R.id.edit_Partner_contact);

        NameProductEditText.setOnTouchListener(mTouchListener);
        QuantityEditText.setOnTouchListener(mTouchListener);
        PriceEditText.setOnTouchListener(mTouchListener);
        PartnerNameEditText.setOnTouchListener(mTouchListener);
        PartnerContactEditText.setOnTouchListener(mTouchListener);
        mIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quantity = QuantityEditText.getText().toString();
                if (TextUtils.isEmpty(quantity)) {

                    Toast.makeText(EditorActivity.this, getString(R.string.quntity_empty), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    givenQuantity = Integer.parseInt(quantity);
                    QuantityEditText.setText(String.valueOf(givenQuantity + 1));
                }


            }
        });

        //decrease quantity
        mDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = QuantityEditText.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    Toast.makeText(EditorActivity.this, getString(R.string.quntity_empty), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    givenQuantity = Integer.parseInt(quantity);
                    //To validate if quantity is greater than 0
                    if ((givenQuantity - 1) >= 0) {
                        QuantityEditText.setText(String.valueOf(givenQuantity - 1));
                    } else {
                        Toast.makeText(EditorActivity.this, getString(R.string.quntity0), Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PhoneNum = PartnerContactEditText.getText().toString().trim();
                orderByPhone(PhoneNum);

            }
        });

    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mCerealHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void saveProduct() {
        String nameString = NameProductEditText.getText().toString().trim();
        String quantityString = QuantityEditText.getText().toString().trim();
        String priceString = PriceEditText.getText().toString().trim();
        String partnerName = PartnerNameEditText.getText().toString().trim();
        String partnerContact = PartnerContactEditText.getText().toString().trim();

        if (mCurrentCerealsUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(quantityString) &&
                TextUtils.isEmpty(partnerContact) && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(partnerName)) {
            Toast.makeText(this, (getString(R.string.empty_field)), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(nameString)) {
            NameProductEditText.setError(getString(R.string.empty_field));
            return;
        }

        if (TextUtils.isEmpty(quantityString)) {
            QuantityEditText.setError(getString(R.string.empty_field));
            return;
        }

        if (TextUtils.isEmpty(priceString)) {
            PriceEditText.setError(getString(R.string.empty_field));
            return;
        }

        if (TextUtils.isEmpty(partnerName)) {
            PartnerNameEditText.setError(getString(R.string.empty_field));
            return;
        }

        if (TextUtils.isEmpty(partnerContact)) {
            PartnerContactEditText.setError(getString(R.string.empty_field));
            return;
        }

        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int quantity = Integer.parseInt(quantityString);
        int price = Integer.parseInt(priceString);
        int contact = Integer.parseInt(partnerContact);
        ContentValues values = new ContentValues();
        values.put(CerealsEntry.Column_PRODUCT_NAME, nameString);
        values.put(CerealsEntry.Column_QUANTITY, quantity);
        values.put(CerealsEntry.Column_PRICE, price);
        values.put(CerealsEntry.Column_PARTNER_NAME, partnerName);
        values.put(CerealsEntry.Column_PARTNER_CONTACT, contact);

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        // Insert a new pet into the provider, returning the content URI for the new pet.

        if (mCurrentCerealsUri == null) {
            Uri newUri = getContentResolver().insert(CerealsEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_product_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, getString(R.string.editor_insert_product_successful), Toast.LENGTH_SHORT).show();
            }

        } else { // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentPetUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentCerealsUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
                        Toast.LENGTH_SHORT).show();
            }


        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentCerealsUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveProduct();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!mCerealHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        // TODO: Implement this method

        // Only perform the delete if this is an existing pet.
        if (mCurrentCerealsUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentCerealsUri, null, null);

            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_product_successful),
                        Toast.LENGTH_SHORT).show();
            }

            // Close the activity
            finish();

        }

        // Show a toast message depending on whether or not the delete was successful.

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                CerealsEntry._ID,
                CerealsEntry.Column_PRODUCT_NAME,
                CerealsEntry.Column_QUANTITY,
                CerealsEntry.Column_PRICE,
                CerealsEntry.Column_PARTNER_CONTACT,
                CerealsEntry.Column_PARTNER_NAME};

        return new CursorLoader(this,
                mCurrentCerealsUri,
                projection,
                null,
                null,
                null


        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)


        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(CerealsEntry.Column_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(CerealsEntry.Column_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(CerealsEntry.Column_PRICE);
            int partnerNameIndex = cursor.getColumnIndex(CerealsEntry.Column_PARTNER_NAME);
            int partnerContactIndex = cursor.getColumnIndex(CerealsEntry.Column_PARTNER_CONTACT);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            String partnerName = cursor.getString(partnerNameIndex);
            int partnerContact = cursor.getInt(partnerContactIndex);
            // Update the views on the screen with the values from the database
            NameProductEditText.setText(name);
            QuantityEditText.setText(Integer.toString(quantity));
            PriceEditText.setText(Integer.toString(price));
            PartnerNameEditText.setText(partnerName);
            PartnerContactEditText.setText(Integer.toString(partnerContact));

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        NameProductEditText.setText("");
        QuantityEditText.setText("");
        PriceEditText.setText("");
        PartnerNameEditText.setText(""); // Select "Unknown" gender
        PartnerContactEditText.setText("");


    }

    private void orderByPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

