package com.example.e_scool_ashray;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Classroom extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private EditText editTextMessage;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        listenForMessages();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    editTextMessage.setText("");
                }
            }
        });
    }

    private void sendMessage(String message) {
        String userEmail = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        ChatMessage chatMessage = new ChatMessage(userEmail, message);

        db.collection("chatMessages")
                .add(chatMessage)
                .addOnSuccessListener(documentReference -> {
                    // Message sent successfully
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    private void listenForMessages() {
        db.collection("chatMessages")
                .orderBy("timestamp")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle the error
                        return;
                    }

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            ChatMessage message = dc.getDocument().toObject(ChatMessage.class);
                            chatMessages.add(message);
                            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                            recyclerView.scrollToPosition(chatMessages.size() - 1);
                        }
                    }
                });
    }
}
