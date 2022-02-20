package com.bitp3453.bitis1g1.projectrequest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddRequestActivity extends AppCompatActivity implements OnMapReadyCallback {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private boolean hasImage = false;
    private String currentPhotoPath;
    private FirebaseFirestore db;
    private EditText edtname, edtphonenumber, edtquantity, edtrequestid, edtdescription;
    private String name, phonenumber, quantity, requestid, description;
    private Button btnSubmit;
    private TextView txtLocation;
    private double timestamp, coordinate;

    //for location services
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String token;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.);

        edtname = findViewById(R.id.);
        edtphonenumber = findViewById(R.id.);
        edtquantity = findViewById(R.id.);
        edtrequestid = findViewById(R.id.);
        edtdescription = findViewById(R.id.);
        btnSubmit = findViewById(R.id.);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        txtLocation = findViewById(R.id.);

        CancellationTokenSource cts = new CancellationTokenSource();

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    Boolean fineLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if (fineLocationGranted != null && fineLocationGranted) {
                        fusedLocationProviderClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if (location != null) {
                                            txtLocation.setText(String.format(Locale.getDefault(), "%f. %f", location.getLatitude(), location.getLongitude()));
                                            coordinate = location.getCoordinate();
                                        }
                                    }
                                });
                    }
                });

        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.add_request_map);
        mapFragment.getMapAsync(this);
    }

    public void addDonationImage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            System.out.println("Test1");
            try {
                photoFile = createImageFile();
                System.out.println("Test2");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                System.out.println("Test3");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "my.edu.utem.ftmk.bitp3453.achifapp",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void submitRequest(View view) {
        name = edtname.getText().toString();
        phonenumber = edtphonenumber.getText().toString();
        description = edtdescription.getText().toString();
        requestid = edtrequestid.getText().toString();
        quantity = edtquantity.getText().toString();

        if (TextUtils.isEmpty(name)) {
            edtname.setError("Please enter request name");
        } else if (TextUtils.isEmpty(phonenumber)) {
            edtphonenumber.setError("Please enter phone number");
        } else if (TextUtils.isEmpty(description)) {
            edtdescription.setError("Please enter description");
        } else if (TextUtils.isEmpty(requestid)) {
            edtrequestid.setError("Please enter request Id");
        } else if (TextUtils.isEmpty(quantity)) {
            edtquantity.setError("Please enter quantity");
        }else {
            addToFirestore(name, phonenumber, description, requestid, quantity);
        }
    }

    public void addToFirestore(String name, String phonenumber, String description, String requestid, String quantity) {
        db = FirebaseFirestore.getInstance();

        CollectionReference dbDonation = db.collection("request");

        //create new donation
        Request request = new Request(name, phonenumber, description, requestid, quantity, coordinate);

        //add to Firestore
        dbRequest.add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddRequestActivity.this, "Donation successfully added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddRequestActivity.this, "Fail to add a new donation. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
