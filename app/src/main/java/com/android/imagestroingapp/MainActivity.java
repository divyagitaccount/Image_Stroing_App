package com.android.imagestroingapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    ImageView image;
    Button selectImage, uploadImage;
    StorageReference reference;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.image);
        selectImage = findViewById(R.id.selectImage);
        uploadImage = findViewById(R.id.uploadImage);

        reference = FirebaseStorage.getInstance().getReference().child("images");

        // Create an ActivityResultLauncher for image selection
        ActivityResultLauncher<String> imageSelector = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imageUri = result;
                        image.setImageURI(imageUri);
                    }
                });

        selectImage.setOnClickListener(view -> imageSelector.launch("image/*"));

        uploadImage.setOnClickListener(view -> {
            if (imageUri != null) {
                StorageReference storageReference = reference.child(String.valueOf(System.currentTimeMillis()));
                storageReference.putFile(imageUri).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                        image.setImageResource(R.drawable.ic_baseline_image_24);
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
