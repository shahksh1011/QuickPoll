package com.example.kshitij.quickpoll.ui.survey;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kshitij.quickpoll.R;
import com.example.kshitij.quickpoll.Utilities.Constants;
import com.example.kshitij.quickpoll.Utilities.GpsUtils;
import com.example.kshitij.quickpoll.adapters.SurveyAdapter;
import com.example.kshitij.quickpoll.data.model.Question;
import com.example.kshitij.quickpoll.data.model.Survey;
import com.example.kshitij.quickpoll.enums.SurveyVariables;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SurveyFragment extends Fragment implements SurveyAdapter.SurveyFragmentInterface {

    private SurveyViewModel surveyViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Survey> locationBasedSurvey;
    ArrayList<Survey> list;
    RecyclerView recyclerView;
    SurveyAdapter surveyAdapter;
    List<String> surveysCompleteList;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    HashMap<String, Object> surveyCompletedMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        surveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        final View root = inflater.inflate(R.layout.survey_fragment, container, false);
        setHasOptionsMenu(true);
        surveysCompleteList = new ArrayList<String>();
        locationBasedSurvey = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        surveyCompletedMap = new HashMap<>();
        currentUser = mAuth.getCurrentUser();
        recyclerView = root.findViewById(R.id.recyvlerview_surveys);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        new GpsUtils(getContext()).turnGPSOn(isGPSEnable -> {
            // turn on GPS
            isGPS = isGPSEnable;
        });

        if (!isGPS) {
            Toast.makeText(getContext(), "Please turn on GPS", Toast.LENGTH_SHORT).show();
        }else {
            getUserLocation();
        }
        db.collection("surveysComplete")
                .document(currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.getData()!=null)
                                surveyCompletedMap = (HashMap<String, Object>) snapshot.getData();
                            Log.d("Something", currentUser.getUid().toString());
                            loadSurveys(root);
//                            Log.d("ComplteSize", String.valueOf(surveysCompleteList.size()));
                        }
                    }
                });
        list = new ArrayList<>();
        return root;
    }

    private void loadSurveys(View root){
        db.collection("surveys")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                    Survey survey = new Survey(document.getId(),
                                            document.getString(String.valueOf(SurveyVariables.createdBy)),
                                            document.getString(String.valueOf(SurveyVariables.description)),
                                            document.getDate(String.valueOf(SurveyVariables.createdDate)),
                                            document.getDate(String.valueOf(SurveyVariables.expiry)),
                                            (List<?>) document.get(String.valueOf(SurveyVariables.questions)),
                                            document.getString(String.valueOf(SurveyVariables.surveyName)),
                                            document.getString(String.valueOf(SurveyVariables.timeToComplete)),
//                                            document.get(String.valueOf(SurveyVariables.location))
                                            null
                                    );
//                                Survey survey = new Survey(document.getId(),
//                                        document.getString(String.valueOf(SurveyVariables.createdBy)))

                                if (!surveyCompletedMap.containsKey(survey.getSurveyId()))
                                        list.add(survey);
                                if (survey.location!=null){
//                                    if(isInLocationRange(survey.location.getLatitude(), survey.location.getLongitude(), currLat, currLong, survey.location.getRadius()))
//                                        locationBasedSurvey.add(survey);
                                }

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            Log.d(TAG, "List Size" + list.size());
                            if (list.size() < 1)
                                Toast.makeText(getContext(), "No more surveys available", Toast.LENGTH_LONG).show();

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            surveyAdapter = new SurveyAdapter(getContext(), list, SurveyFragment.this, root);
                            recyclerView.setAdapter(surveyAdapter);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.survey_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_LONG).show();
        CharSequence title = item.getTitle();
        if (getResources().getString(R.string.menu_location_specific_surveys).equals(title)) {
            Toast.makeText(getContext(), "Location", Toast.LENGTH_LONG).show();
        } else if (getResources().getString(R.string.menu_all_surveys).equals(title)) {
            Toast.makeText(getContext(), "All", Toast.LENGTH_LONG).show();
        }
        return true;

    }

    @Override
    public void onSurveyClick(Survey survey, View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("survey", survey);
        Navigation.findNavController(view).navigate(R.id.surveyDescriptionFragment,bundle);
    }

//    private boolean isInLocationRange(double centerLat, double centerLng, double currLat, double currLng, int radius){
//        float[] results = new float[1];
//        Location.distanceBetween(centerLat, centerLng, currLat, currLng, results);
//        float distanceInMeters = results[0];
//        return distanceInMeters<(radius*1000);
//
//    }


    private double longitude, latitude;
    private boolean isGPS = false;
    public void getUserLocation(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_REQUEST);
        }else{
           // mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("Location1", "Location: "+latitude+" "+longitude);
                }
            });
        }

        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("Location2", "Location: "+latitude+" "+longitude);
                        /*if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }*/
                    }
                }
            }
        };
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.d("Location3", "Location: "+latitude+" "+longitude);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

}
