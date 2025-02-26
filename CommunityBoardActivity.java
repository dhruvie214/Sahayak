package com.example.sahayakapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CommunityBoardActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText postEditText;
    private ImageView selectedImage;
    private Uri imageUri;
    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_board);

        dbHelper = new DBHelper(this);
        postEditText = findViewById(R.id.postEditText);
        selectedImage = findViewById(R.id.selectedImage);
        Button selectImageButton = findViewById(R.id.selectImageButton);
        Button postButton = findViewById(R.id.postButton);
        recyclerView = findViewById(R.id.recyclerView);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        loadPosts();

        selectImageButton.setOnClickListener(v -> openImagePicker());
        postButton.setOnClickListener(v -> createPost());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            selectedImage.setImageURI(imageUri);
        }
    }

    private void createPost() {
        String postText = postEditText.getText().toString().trim();
        String imageUrl = (imageUri != null) ? imageUri.toString() : "";
        int userId = 1; // TODO: Replace with actual logged-in user ID

        if (postText.isEmpty() && imageUrl.isEmpty()) {
            Toast.makeText(this, "Post cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = dbHelper.addPost(postText, imageUrl, userId);
        if (inserted) {
            Toast.makeText(this, "Post created successfully!", Toast.LENGTH_SHORT).show();
            postEditText.setText("");
            selectedImage.setImageDrawable(null);
            imageUri = null;
            loadPosts();
        } else {
            Toast.makeText(this, "Failed to create post.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPosts() {
        postList.clear();
        postList.addAll(dbHelper.getAllPosts());
        postAdapter.notifyDataSetChanged();
    }
}
