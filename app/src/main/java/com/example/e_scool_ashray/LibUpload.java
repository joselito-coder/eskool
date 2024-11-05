package com.example.e_scool_ashray;



 import android.content.Intent;
 import android.database.Cursor;
 import android.net.Uri;
import android.os.Bundle;
 import android.provider.OpenableColumns;
 import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

 import com.google.firebase.firestore.FirebaseFirestore;
 import com.google.firebase.storage.FirebaseStorage;
 import com.google.firebase.storage.StorageReference;


public class LibUpload extends AppCompatActivity {


    private static final int PICK_PDF_REQUEST = 1;

        private EditText editTextBookName, editTextEmail, editTextTagName;
        private Button buttonSelectPdf, buttonUpload;
        private TextView textViewSelectedPdf;
        private Uri pdfUri;

        private FirebaseAuth firebaseAuth;
        private FirebaseFirestore firestore;
        private StorageReference storageReference;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lib_upload);

            // Initialize views
            editTextBookName = findViewById(R.id.editTextBookName);
            editTextEmail = findViewById(R.id.editTextEmail);
            editTextTagName = findViewById(R.id.editTextTagName);
            buttonSelectPdf = findViewById(R.id.button_select_pdf);
            buttonUpload = findViewById(R.id.button_upload);
            textViewSelectedPdf = findViewById(R.id.textViewSelectedPdf);

            // Initialize Firebase
            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference("books");

            // Set the email EditText to the logged-in user's email
            if (firebaseAuth.getCurrentUser() != null) {
                editTextEmail.setText(firebaseAuth.getCurrentUser().getEmail());
            }

            // Set listeners
            buttonSelectPdf.setOnClickListener(v -> openFileChooser());
            buttonUpload.setOnClickListener(v -> uploadBook());
        }

        private void openFileChooser() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, PICK_PDF_REQUEST);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                pdfUri = data.getData();
                String fileName = getFileName(pdfUri);
                textViewSelectedPdf.setText("Selected PDF: " + fileName);
            }
        }

        private String getFileName(Uri uri) {
            String result = null;
            if (uri.getScheme().equals("content")) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
            if (result == null) {
                result = uri.getPath();
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
            return result;
        }

    private void uploadBook() {
        String bookName = editTextBookName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String tagName = editTextTagName.getText().toString().trim();

        if (pdfUri != null && !bookName.isEmpty() && !email.isEmpty() && !tagName.isEmpty()) {

            String documentName = bookName + "_" + email + "_" + tagName;
            StorageReference fileReference = storageReference.child(documentName + ".pdf");

            // Upload the PDF file
            fileReference.putFile(pdfUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                        // Create a combined document name
                        //String documentName = bookName + "_" + email + "_" + tagName;

                        // Create a book object
                        Book book = new Book(bookName, email, tagName, downloadUrl.toString());

                        // Save the book details to Firestore using the combined name
                        firestore.collection("books")
                                .document(documentName)  // Use combined name as document ID
                                .set(book)  // Use set() to create or overwrite the document
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(LibUpload.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                                    textViewSelectedPdf.setText("");
                                })
                                .addOnFailureListener(e -> Toast.makeText(LibUpload.this, "Firestore Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }))
                    .addOnFailureListener(e -> Toast.makeText(LibUpload.this, "Storage Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Please fill all fields and select a PDF", Toast.LENGTH_SHORT).show();
        }
    }



    // Book class to represent book details
        public static class Book {
            private String bookName;
            private String email;
            private String tagName;
            private String downloadUrl;

            public Book() {
                // Required empty constructor for Firestore
            }

            public Book(String bookName, String email, String tagName, String downloadUrl) {
                this.bookName = bookName;
                this.email = email;
                this.tagName = tagName;
                this.downloadUrl = downloadUrl;
            }
        }
    }
