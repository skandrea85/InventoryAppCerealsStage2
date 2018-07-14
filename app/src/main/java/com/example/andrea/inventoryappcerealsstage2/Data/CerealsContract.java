package com.example.andrea.inventoryappcerealsstage2.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class CerealsContract {
    private CerealsContract() {
    }

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.cereals";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_CEREALS = "cereals";

    public static final class CerealsEntry implements BaseColumns {

        /**
         * The content URI to access the pet data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CEREALS);
        /**
         * The MIME type of the {for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CEREALS;

        /**
         * The MIME type of the  for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CEREALS;

        public final static String TABLE_NAME = "cereals";
        public final static String _ID = BaseColumns._ID;
        public final static String Column_PRODUCT_NAME = "product";
        public final static String Column_PRICE = "price";
        public final static String Column_QUANTITY = "quantity";
        public final static String Column_PARTNER_NAME = "partner";
        public final static String Column_PARTNER_CONTACT = "contact";

    }
}